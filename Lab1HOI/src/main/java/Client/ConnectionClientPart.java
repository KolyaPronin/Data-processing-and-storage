package Client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionClientPart {
    private static final BlockingQueue<String> genQueue = new LinkedBlockingQueue<>();
    private static int delaySec = 0;
    private static boolean exitBeforeRead = false;
    private static String serverHost = "localhost";
    private static int serverPort = 4004;

    public void client() {
        try {
            String d = System.getProperty("client.delaySec");
            if (d != null) delaySec = Integer.parseInt(d);
            String ebr = System.getProperty("client.exitBeforeRead");
            if (ebr != null) exitBeforeRead = Boolean.parseBoolean(ebr);
            String h = System.getProperty("client.serverHost");
            if (h != null) serverHost = h;
            String p = System.getProperty("client.serverPort");
            if (p != null) serverPort = Integer.parseInt(p);
        } catch (Exception ignored) {}

        Thread inputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
                while (true) {

                    System.out.println("Ждём ввода имени...");
                    String name = reader.readLine();
                    System.out.println("Прочитали имя: " + name);

                    if (name == null || name.trim().isEmpty()) continue;
                    if ("exit".equalsIgnoreCase(name.trim())) {
                        System.out.println("Клиент завершает работу...");
                        System.exit(0);
                    }
                    genQueue.offer(name);
                }
            } catch (IOException e) {
                System.err.println("Ошибка ввода: " + e.getMessage());
            }
        }, "input-thread");
        inputThread.setDaemon(true);
        inputThread.start();

        int genWorkers = Math.max(1, ClientConfig.handlerThreads);
        for (int i = 0; i < genWorkers; i++) {
            Thread worker = new Thread(() -> {
                while (true) {
                    try {
                        String name = genQueue.take();
                        processRequest(name);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "gen-worker-" + i);
            worker.start();
        }
    }

    private void processRequest(String name) {
        try (Socket clientSocket = new Socket(serverHost, serverPort);
             DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            byte[] nameBytes = name.getBytes("US-ASCII");
            out.write(nameBytes);
            out.writeByte(0);
            out.flush();

            if (delaySec > 0) {
                try {
                    Thread.sleep(delaySec * 1000L);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }

            if (exitBeforeRead) {
                System.out.println("Клиент завершает работу без чтения ответа (имитация аварии)...");
                return;
            }

            int certLen = in.readInt();
            if (certLen <= 0 || certLen > 10_000_000) throw new IOException("Некорректная длина сертификата: " + certLen);
            byte[] certBytes = in.readNBytes(certLen);
            Files.write(Paths.get(name + ".crt"), certBytes);

            int pubLen = in.readInt();
            if (pubLen <= 0 || pubLen > 10_000_000) throw new IOException("Некорректная длина публичного ключа: " + pubLen);
            byte[] pubBytes = in.readNBytes(pubLen);
            Files.write(Paths.get(name + ".pub"), pubBytes);

            int privLen = in.readInt();
            if (privLen <= 0 || privLen > 10_000_000) throw new IOException("Некорректная длина приватного ключа: " + privLen);
            byte[] privBytes = in.readNBytes(privLen);
            Files.write(Paths.get(name + ".key"), privBytes);

            System.out.println("Файлы .crt, .pub и .key успешно сохранены для " + name);

        } catch (IOException e) {
            System.err.println("Ошибка клиента при работе с " + name + ": " + e.getMessage());
        }
    }
}
