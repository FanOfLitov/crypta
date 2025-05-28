package crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KeyGenTest {
    @Test
    public void testKeyLength() throws Exception {
        byte[] key = KeyGen.deriveKey("some pass");
        assertEquals(32, key.length, "Key should be 256 bits");
    }

    @Test
    public void testKeyDeterminism() throws Exception {
        byte[] key1 = KeyGen.deriveKey("consistent");
        byte[] key2 = KeyGen.deriveKey("consistent");
        assertArrayEquals(key1, key2, "Key derivation must be deterministic");
    }
}