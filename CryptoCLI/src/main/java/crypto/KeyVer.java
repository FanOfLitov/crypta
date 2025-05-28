package crypto;

public class KeyVer {
    // Very basic key quality test based on entropy estimation (placeholder for NIST tests)
    public static boolean passesEntropyTest(byte[] data) {
        int[] counts = new int[256];
        for (byte b : data) counts[b & 0xFF]++;
        double entropy = 0.0;
        for (int count : counts) {
            if (count == 0) continue;
            double p = count / (double) data.length;
            entropy += -p * (Math.log(p) / Math.log(2));
        }
        return entropy > 7.9; // close to 8 bits/byte (high entropy)
    }
}