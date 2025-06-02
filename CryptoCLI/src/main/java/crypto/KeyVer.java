package crypto;

public class KeyVer {
    public static boolean passesEntropyTest(byte[] data) {
        int[] counts = new int[256];
        for (byte b : data) counts[b & 0xFF]++;
        double entropy = 0.0;
        for (int count : counts) {
            if (count == 0) continue;
            double p = count / (double) data.length;
            entropy += -p * (Math.log(p) / Math.log(2));
        }
        System.out.printf("Entropy: %.4f bits/byte\n", entropy);
        return entropy > 7.5; // relaxed threshold
    }
}
