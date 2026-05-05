package Server;

import org.bouncycastle.operator.OperatorCreationException;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import java.security.cert.CertificateException;

public class ConnectionServerPart {

    private static Selector selector;
    private static ServerSocketChannel serverChannel;

    private static final BlockingQueue<String> genQueue = new LinkedBlockingQueue<String>();
    private static final Map<String, ConcurrentLinkedQueue<SocketChannel>> waiters = new ConcurrentHashMap<String, ConcurrentLinkedQueue<SocketChannel>>();
    private static final Set<String> enqueued = ConcurrentHashMap.newKeySet();
    private static final BlockingQueue<SendItem> sendQueue = new LinkedBlockingQueue<SendItem>();

    private static class SendItem {
        final SocketChannel channel;
        final ByteBuffer buffer;
        SendItem(SocketChannel ch, ByteBuffer buf) { this.channel = ch; this.buffer = buf; }
    }

    public void server() throws CertificateException, OperatorCreationException {
        try {
            try {
                serverChannel = ServerSocketChannel.open();
                serverChannel.configureBlocking(false);
                serverChannel.bind(new InetSocketAddress(ServerConfig.port));
                selector = Selector.open();
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("Сервер работает");

                // sender thread
                new Thread(() -> {
                    while (true) {
                        try {
                            SendItem item = sendQueue.take();
                            SocketChannel ch = item.channel;
                            ByteBuffer buf = item.buffer;
                            try {
                                int written = ch.write(buf);
                                if (buf.hasRemaining()) {
                                    sendQueue.offer(new SendItem(ch, buf));
                                } else {
                                    try { ch.close(); } catch (IOException ignored) {}
                                }
                            } catch (IOException ioEx) {
                                System.err.println(ioEx);
                                try { ch.close(); } catch (IOException ignored) {}
                            }
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }, "sender-thread").start();

                int genWorkers = Math.max(1, ServerConfig.handlerThreads);
                for (int i = 0; i < genWorkers; i++) {
                    new Thread(() -> {
                        while (true) {
                            try {
                                String name = genQueue.take();
                                try {
                                    KeyPairAndCert data = Cache.getOrCreate(name).get();
                                    ConcurrentLinkedQueue<SocketChannel> q = waiters.get(name);
                                    if (q != null) {
                                        byte[] certBytes = data.getCertificate().getEncoded();
                                        byte[] pubKeyBytes = data.getPublicKey().getEncoded();
                                        byte[] prKeyBytes = data.getPrivateKey().getEncoded();

                                        ByteBuffer payload = ByteBuffer.allocate(4 + certBytes.length + 4 + pubKeyBytes.length + 4 + prKeyBytes.length);
                                        payload.putInt(certBytes.length).put(certBytes);
                                        payload.putInt(pubKeyBytes.length).put(pubKeyBytes);
                                        payload.putInt(prKeyBytes.length).put(prKeyBytes);
                                        payload.flip();

                                        SocketChannel ch;
                                        while ((ch = q.poll()) != null) {
                                            sendQueue.offer(new SendItem(ch, payload.duplicate()));
                                        }
                                    }
                                } catch (Exception e) {
                                    System.err.println(e);
                                } finally {
                                    enqueued.remove(name);
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }, "gen-worker-" + i).start();
                }

                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                while (true) {
                    selector.select();
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        try {
                            if (key.isAcceptable()) {
                                SocketChannel ch = serverChannel.accept();
                                if (ch == null) continue;
                                ch.configureBlocking(false);
                                key.selector().wakeup();
                                ch.register(selector, SelectionKey.OP_READ, new ByteArrayOutputStream());
                            } else if (key.isReadable()) {
                                SocketChannel ch = (SocketChannel) key.channel();
                                ByteArrayOutputStream acc = (ByteArrayOutputStream) key.attachment();
                                readBuffer.clear();
                                int n = ch.read(readBuffer);
                                if (n == -1) {
                                    try { ch.close(); } catch (IOException ignored) {}
                                    continue;
                                }
                                readBuffer.flip();
                                while (readBuffer.hasRemaining()) {
                                    byte b = readBuffer.get();
                                    if (b == 0) {
                                        String name = acc.toString("US-ASCII");
                                        System.out.println("начал ставить имя в очередь генерации...");
                                        waiters.computeIfAbsent(name, k -> new ConcurrentLinkedQueue<SocketChannel>()).add(ch);
                                        if (enqueued.add(name)) {
                                            genQueue.offer(name);
                                            System.out.println("Имя " + name + " поставлено в очередь генерации");
                                        }
                                        key.interestOps(0);
                                        break;
                                    } else {
                                        acc.write(b);
                                    }
                                }
                            }
                        } catch (IOException ioEx) {
                            System.err.println(ioEx);
                            try { key.channel().close(); } catch (IOException ignored) {}
                        }
                    }
                }

            } finally {
                System.out.println("Сервер закрыт!");
                try { if (serverChannel != null) serverChannel.close(); } catch (IOException ignored) {}
                try { if (selector != null) selector.close(); } catch (IOException ignored) {}
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
