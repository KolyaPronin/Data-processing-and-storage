package Server;

import openSSL.CertificateGenerator;
import openSSL.KeyGenerator;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class ConnectionServerPart {

    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static DataInputStream in;
    private static DataOutputStream out;

    public void server() throws CertificateException, OperatorCreationException {
        try {
            try {
                serverSocket = new ServerSocket(4004);
                System.out.println("Сервер работает");

                clientSocket = serverSocket.accept();
                try {
                    in = new DataInputStream(clientSocket.getInputStream());
                    out = new DataOutputStream(clientSocket.getOutputStream());

                    ByteArrayOutputStream nameBuf = new ByteArrayOutputStream();
                    byte b;
                    while ((b = in.readByte()) != 0) {
                        nameBuf.write(b);
                    }
                    String word = nameBuf.toString("US-ASCII");

                    KeyPairAndCert data = Cache.getOrCreate(word);

                    byte[] certBytes = data.getCertificate().getEncoded();
                    byte[] pubKeyBytes = data.getPublicKey().getEncoded();
                    byte[] prKeyBytes = data.getPrivateKey().getEncoded();

                    out.writeInt(certBytes.length);
                    out.write(certBytes);

                    out.writeInt(pubKeyBytes.length);
                    out.write(pubKeyBytes);

                    out.writeInt(prKeyBytes.length);
                    out.write(prKeyBytes);

                    out.flush();

                } finally {
                    clientSocket.close();
                    in.close();
                    out.close();
                }

            } finally {
                System.out.println("Сервер закрыт!");
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
