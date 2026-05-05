package openSSL;

import java.security.*;

public class KeyGenerator {
    public KeyPair Keys() throws NoSuchAlgorithmException {
        KeyPairGenerator object = KeyPairGenerator.getInstance("RSA");
        object.initialize(8192, new SecureRandom());
        return object.genKeyPair();
    }
}
