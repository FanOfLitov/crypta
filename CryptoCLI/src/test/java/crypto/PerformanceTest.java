package crypto;

import org.junit.jupiter.api.Test;

public class PerformanceTest {
    @Test
    public void testEncryptionSpeed() throws Exception {
        byte[] data = new byte[5 * 1024 * 1024]; // 5 MB
        for (int i = 0; i < data.length; i++) data[i] = (byte) i;
        byte[] key = KeyGen.deriveKey("speedpass");

        long start = System.nanoTime();
        byte[] encrypted = CryptoManager.encrypt(data, key, CipherAlgorithm.CUSTOM);
        long end = System.nanoTime();

        double seconds = (end - start) / 1_000_000_000.0;
        double mbps = (data.length * 8) / (seconds * 1_000_000);

        System.out.printf("Speed: %.2f Mbps\n", mbps);
        assert mbps > 2.0 : "Encryption speed is too slow";
    }
}