package crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CryptoManagerTest {
    @Test
    public void testBasicEncryptionDecryption() throws Exception {
        String message = "The quick brown fox jumps over the lazy dog";
        byte[] key = KeyGen.deriveKey("supersecret");
        byte[] encrypted = CryptoManager.encrypt(message.getBytes("UTF-8"), key, CipherAlgorithm.CUSTOM);
        byte[] decrypted = CryptoManager.decrypt(encrypted, key, CipherAlgorithm.CUSTOM);
        assertEquals(message, new String(decrypted, "UTF-8"));
    }

    @Test
    public void testEmptyInput() throws Exception {
        byte[] key = KeyGen.deriveKey("emptypass");
        byte[] encrypted = CryptoManager.encrypt(new byte[0], key, CipherAlgorithm.CUSTOM);
        byte[] decrypted = CryptoManager.decrypt(encrypted, key, CipherAlgorithm.CUSTOM);
        assertEquals(0, decrypted.length);
    }

    @Test
    public void testDifferentKeysFail() throws Exception {
        String message = "Sensitive information";
        byte[] key1 = KeyGen.deriveKey("key1");
        byte[] key2 = KeyGen.deriveKey("key2");
        byte[] encrypted = CryptoManager.encrypt(message.getBytes("UTF-8"), key1, CipherAlgorithm.CUSTOM);
        byte[] decrypted = CryptoManager.decrypt(encrypted, key2, CipherAlgorithm.CUSTOM);
        assertNotEquals(message, new String(decrypted, "UTF-8"));
    }
}