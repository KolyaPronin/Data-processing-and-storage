package Server;

import openSSL.CertificateGenerator;
import openSSL.KeyGenerator;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.*;

public class Cache {
    public static final Map<String, CompletableFuture<KeyPairAndCert>> cache = new ConcurrentHashMap<String, CompletableFuture<KeyPairAndCert>>();
    public static ExecutorService generationPool = Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors()));

    public static synchronized void configureGenerationPool(int threads) {
        if (threads <= 0) return;
        ExecutorService old = generationPool;
        generationPool = Executors.newFixedThreadPool(threads);
        if (old != null) {
            old.shutdown();
        }
    }

    public static CompletableFuture<KeyPairAndCert> getOrCreate(String name) {
        return cache.computeIfAbsent(name, k -> CompletableFuture.supplyAsync(() -> {
            try {
                KeyPair pair = new KeyGenerator().Keys();
                X509Certificate cert = new CertificateGenerator().x509Generator(k, pair);
                return new KeyPairAndCert(pair.getPublic(), pair.getPrivate(), cert);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, generationPool));
    }
}
