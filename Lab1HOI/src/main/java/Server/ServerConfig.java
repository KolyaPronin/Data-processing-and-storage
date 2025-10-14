package Server;

public class ServerConfig {
    public static int port = 4004;
    public static int handlerThreads = Math.max(4, Runtime.getRuntime().availableProcessors());
    public static String issuerCN = "CN=TestIssuer";
    public static String signingKeyPath = null; // путь к приватному ключу CA (PKCS#8 PEM)
}
