package Server;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class KeyPairAndCert {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final X509Certificate certificate;

    public KeyPairAndCert(PublicKey pubKey, PrivateKey prKey, X509Certificate cert){
        this.publicKey = pubKey;
        this.privateKey = prKey;
        this.certificate = cert;
    }

    public PublicKey getPublicKey() { return publicKey; }
    public PrivateKey getPrivateKey() { return privateKey; }
    public X509Certificate getCertificate() { return certificate; }
}
