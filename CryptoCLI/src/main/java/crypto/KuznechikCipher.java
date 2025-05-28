package crypto;

public class KuznechikCipher {
    private static final byte[] S_BOX = new byte[] {
            (byte)0xFC, (byte)0xEE, (byte)0xDD, (byte)0x11, (byte)0xCF, (byte)0x6E, (byte)0x31, (byte)0x16,
            (byte)0xFB, (byte)0xC4, (byte)0xFA, (byte)0xDA, (byte)0x23, (byte)0xC5, (byte)0x04, (byte)0x4D
            // ... full S-box needed
    };

    public static byte[] encrypt(byte[] data, byte[] key) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = S_BOX[(data[i] ^ key[i % key.length]) & 0xFF];
        }
        return result;
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        byte[] inverseSBox = new byte[256];
        for (int i = 0; i < S_BOX.length; i++) {
            inverseSBox[S_BOX[i] & 0xFF] = (byte) i;
        }
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte)(inverseSBox[data[i] & 0xFF] ^ key[i % key.length]);
        }
        return result;
    }
}
