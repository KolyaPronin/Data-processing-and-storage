package Client;

import Server.Finder;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConnectionClientPart {
    private static Socket clientSocket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static boolean finder;
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.print("Введите имя: ");
                String name = reader.readLine();
                if (name == null || name.trim().isEmpty()) continue;
                if ("exit".equalsIgnoreCase(name.trim())) {
                    System.out.println("Клиент завершает работу...");
                    return;
                }

                finder = new Finder().find(name);
                if(finder){
                    System.out.println("Сертификат найден локально, запрос на сервер сделан не будет...");
                    continue;
                }

                clientSocket = new Socket(serverHost, serverPort);
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                byte[] nameBytes = name.getBytes("US-ASCII");
                out.write(nameBytes);
                out.writeByte(0);
                out.flush();

                if (delaySec > 0) {
                    try { Thread.sleep(delaySec * 1000L); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                }

                if (exitBeforeRead) {
                    System.out.println("Клиент завершает работу без чтения ответа (имитация аварии)...");
                    return;
                }

                int certLen = in.readInt();
                byte[] certBytes = new byte[certLen];
                in.readFully(certBytes);
                Files.write(Paths.get(name + ".crt"), certBytes);

                int pubLen = in.readInt();
                byte[] pubBytes = new byte[pubLen];
                in.readFully(pubBytes);
                Files.write(Paths.get(name + ".pub"), pubBytes);

                int privLen = in.readInt();
                byte[] privBytes = new byte[privLen];
                in.readFully(privBytes);
                Files.write(Paths.get(name + ".key"), privBytes);

                System.out.println("Файлы .crt, .pub и .key успешно сохранены для " + name);

            } catch (IOException e) {
                System.err.println(e);
            } finally {
                try {
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                        in.close();
                        out.close();
                    }
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }
}
