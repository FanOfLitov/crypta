package crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RandomnessTest {
    @Test
    public void testKeyEntropy() throws Exception {
        byte[] key = KeyGen.deriveKey("some secure passphrase");
        assertTrue(KeyVer.passesEntropyTest(key), "Key does not pass entropy test");
    }

    @Test
    public void testEncryptedRandomness() throws Exception {
        String message = "entropy passphrase";
        byte[] key = KeyGen.deriveKey("entropy passphrase");
        byte[] encrypted = CryptoManager.encrypt(message.getBytes(), key, CipherAlgorithm.CUSTOM);
        assertTrue(KeyVer.passesEntropyTest(encrypted), "Encrypted data is not random enough");
    }
}
