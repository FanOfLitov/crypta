package crypto;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptDecryptTest {
    @Test
    public void testTextFile() throws Exception {
        testEncryptDecrypt("src/main/resources/sample.txt");
    }

    @Test
    public void testJsonFile() throws Exception {
        testEncryptDecrypt("src/main/resources/sample.json");
    }

    @Test
    public void testImageFile() throws Exception {
        testEncryptDecrypt("src/main/resources/sample.jpg");
    }

    @Test
    public void testVideoFile() throws Exception {
        testEncryptDecrypt("src/main/resources/sample.mp4");
    }

    private void testEncryptDecrypt(String path) throws Exception {
        byte[] original = Files.readAllBytes(Paths.get(path));
        byte[] key = KeyGen.deriveKey("resourcetest");
        byte[] encrypted = CryptoManager.encrypt(original, key, CipherAlgorithm.CUSTOM);
        byte[] decrypted = CryptoManager.decrypt(encrypted, key, CipherAlgorithm.CUSTOM);
        assertArrayEquals(original, decrypted);
    }
}