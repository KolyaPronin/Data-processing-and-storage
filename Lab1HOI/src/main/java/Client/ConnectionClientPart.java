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

    public void client() {
        try {
            clientSocket = new Socket("localhost", 4004);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Введите имя: ");
            String name = reader.readLine();
            finder = new Finder().find(name);
            if(finder){
                System.out.println("Сертификат найден локально, запрос на сервер сделан не будет...");
                return;
            }


            byte[] nameBytes = name.getBytes("US-ASCII");
            out.write(nameBytes);
            out.writeByte(0);
            out.flush();

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
