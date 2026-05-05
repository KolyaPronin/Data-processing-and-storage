package openSSL;

import Server.ServerConfig;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;

public class SigningKeyHolder {
    private static volatile PrivateKey signingKey;

    public static synchronized void initFromFile() {
        if (ServerConfig.signingKeyPath == null) return;
        try (PemReader pemReader = new PemReader(
                new InputStreamReader(
                        new FileInputStream(ServerConfig.signingKeyPath),
                        StandardCharsets.UTF_8))) {
            byte[] content = pemReader.readPemObject().getContent();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(content);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            signingKey = kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить приватный ключ CA: " + e.getMessage(), e);
        }
    }


    public static synchronized void ensureKeyAvailable() {
        try {
            if (ServerConfig.signingKeyPath == null) {
                ServerConfig.signingKeyPath = "ca_key.pem";
            }
            java.io.File f = new java.io.File(ServerConfig.signingKeyPath);
            if (!f.exists()) {
                // Генерация нового ключа RSA 8192 бит
                KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
                gen.initialize(8192, new SecureRandom());
                KeyPair kp = gen.generateKeyPair();
                byte[] pkcs8 = kp.getPrivate().getEncoded();

                // Сохраняем приватный ключ в PEM формате с UTF-8
                try (PemWriter w = new PemWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(f),
                                StandardCharsets.UTF_8))) {
                    w.writeObject(new PemObject("PRIVATE KEY", pkcs8));
                }
            }
            initFromFile();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обеспечить наличие ключа CA: " + e.getMessage(), e);
        }
    }


    public static PrivateKey getSigningKey() {
        if (signingKey == null) {
            throw new IllegalStateException("Ключ для подписи не загружен. Укажите --signingKey=<path>");
        }
        return signingKey;
    }
}
