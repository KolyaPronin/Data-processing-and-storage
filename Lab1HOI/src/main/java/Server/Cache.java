package Server;

import openSSL.CertificateGenerator;
import openSSL.KeyGenerator;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    public static final  Map<String, KeyPairAndCert> cache = new ConcurrentHashMap<String, KeyPairAndCert>();

    public static KeyPairAndCert getOrCreate(String name) {
        return cache.computeIfAbsent(name, k -> {
            try {
                KeyPair pair = new KeyGenerator().Keys();
                X509Certificate cert = new CertificateGenerator().x509Generator(k);
                return new KeyPairAndCert(pair.getPublic(), pair.getPrivate(), cert);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
