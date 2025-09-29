package openSSL;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class CertificateGenerator {
    public X509Certificate x509Generator(String commonName) throws NoSuchAlgorithmException, OperatorCreationException, CertificateException {
        PublicKey publicKey = new KeyGenerator().Keys().getPublic();
        PrivateKey privateKey = new KeyGenerator().Keys().getPrivate();
        X500Name subject = new X500Name("CN=" + commonName);

        Date notBefore = Date.from(Instant.now());
        Date notAfter = Date.from(Instant.now().plus(365, ChronoUnit.DAYS));
        BigInteger serialNumber = new BigInteger("159101464251294296696568545928305368678");

        SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());

        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
                subject,
                serialNumber,
                notBefore,
                notAfter,
                subject,
                subPubKeyInfo
        );
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);
        X509CertificateHolder holder  = builder.build(signer);
        return new JcaX509CertificateConverter().getCertificate(holder);
    }
}
