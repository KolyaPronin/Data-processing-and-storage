package Client;

public class ClientConfig {
    public static int port = 4004;
    public static int handlerThreads = Math.max(4, Runtime.getRuntime().availableProcessors());
    public static String issuerCN = "CN=TestIssuer";
    public static String signingKeyPath = null;
}
