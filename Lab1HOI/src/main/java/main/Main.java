package main;

import Client.ConnectionClientPart;
import Server.ConnectionServerPart;
import org.bouncycastle.operator.OperatorCreationException;
import java.security.cert.CertificateException;

public class Main {
    public static void main(String[] args) {
        new Thread(()-> {
            try {
                new ConnectionServerPart().server();
            } catch (CertificateException | OperatorCreationException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            new ConnectionClientPart().client();
        }).start();

    }
}