package crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CryptoManagerSubstitutionTest {
    @Test
    public void testSubstitutionReversibility() throws Exception {
        byte[] input = "TestSubstitution".getBytes();
        byte[] key = KeyGen.deriveKey("reversible");
        byte[] substituted = CryptoManager.encrypt(input, key, CipherAlgorithm.CUSTOM);
        byte[] recovered = CryptoManager.decrypt(substituted, key, CipherAlgorithm.CUSTOM);
        assertArrayEquals(input, recovered);
    }
}
