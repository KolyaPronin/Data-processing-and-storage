package main;

import Client.ConnectionClientPart;
import Server.ConnectionServerPart;
import Server.Cache;
import Server.ServerConfig;
import org.bouncycastle.operator.OperatorCreationException;
import java.security.cert.CertificateException;

public class Main {
    public static void main(String[] args) {
        int port = 4004;
        int genThreads = Runtime.getRuntime().availableProcessors();
        int handlerThreads = Math.max(4, Runtime.getRuntime().availableProcessors());
        String mode = "both"; // server | client | both
        String issuerCN = null;
        String signingKey = null;

        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.substring("--port=".length()));
            } else if (arg.startsWith("--genThreads=")) {
                genThreads = Integer.parseInt(arg.substring("--genThreads=".length()));
            } else if (arg.startsWith("--handlerThreads=")) {
                handlerThreads = Integer.parseInt(arg.substring("--handlerThreads=".length()));
            } else if (arg.startsWith("--issuerCN=")) {
                issuerCN = arg.substring("--issuerCN=".length());
            } else if (arg.startsWith("--signingKey=")) {
                signingKey = arg.substring("--signingKey=".length());
            } else if (arg.startsWith("--mode=")) {
                mode = arg.substring("--mode=".length());
            }
        }

        ServerConfig.port = port;
        ServerConfig.handlerThreads = handlerThreads;
        if (issuerCN != null) ServerConfig.issuerCN = issuerCN;
        if (signingKey != null) ServerConfig.signingKeyPath = signingKey;
        Cache.configureGenerationPool(genThreads);
        openSSL.SigningKeyHolder.ensureKeyAvailable();

        if ("server".equalsIgnoreCase(mode) || "both".equalsIgnoreCase(mode)) {
            Thread serverThread = new Thread(() -> {
                try {
                    new ConnectionServerPart().server();
                } catch (CertificateException | OperatorCreationException e) {
                    throw new RuntimeException(e);
                }
            }, "server-main");
            if ("both".equalsIgnoreCase(mode)) {
                serverThread.setDaemon(true);
            }
            serverThread.start();
        }

        if ("client".equalsIgnoreCase(mode)) {
            new Thread(() -> new ConnectionClientPart().client(), "client-main").start();
        } else if ("both".equalsIgnoreCase(mode)) {
            new ConnectionClientPart().client();
            System.exit(0);
        }
    }
}

