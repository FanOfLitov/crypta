package crypto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class KeyGen {
    private static final int ITERATIONS = 100_000;
    private static final int BLOCK_SIZE = 64;

    public static byte[] deriveKey(String password) throws Exception {
        byte[] salt = "FixedCryptoSalt".getBytes(StandardCharsets.UTF_8);
        byte[] key= generateKeyFromPassword(password, salt, 32);
        Files.createDirectories(Paths.get("test_output"));
        Files.write(Paths.get("test_output/derived_key.bin"), key);
        return key;
    }

    private static byte[] generateKeyFromPassword(String password, byte[] salt, int keySizeBytes) {
        try {
            return pbkdf2HmacSha256(password.getBytes(StandardCharsets.UTF_8), salt, keySizeBytes);
        } catch (Exception e) {
            System.err.println("KeyGen error: " + e.getMessage());
            return null;
        }
    }

    private static byte[] pbkdf2HmacSha256(byte[] password, byte[] salt, int keyLength) throws NoSuchAlgorithmException {
        int hLen = 32;
        int l = (int) Math.ceil((double) keyLength / hLen);
        int r = keyLength - (l - 1) * hLen;

        byte[] derivedKey = new byte[keyLength];
        int destPos = 0;

        for (int i = 1; i <= l; i++) {
            byte[] t = f(password, salt, i);
            int length = (i == l) ? r : hLen;
            System.arraycopy(t, 0, derivedKey, destPos, length);
            destPos += length;
        }
        return derivedKey;
    }

    private static byte[] f(byte[] password, byte[] salt, int blockIndex) throws NoSuchAlgorithmException {
        byte[] intBlock = ByteBuffer.allocate(4).putInt(blockIndex).array();
        byte[] saltAndBlock = unionArrays(salt, intBlock);

        byte[] u = hmacSha256(password, saltAndBlock);
        byte[] output = u.clone();

        for (int i = 1; i < ITERATIONS; i++) {
            u = hmacSha256(password, u);
            for (int j = 0; j < output.length; j++) {
                output[j] ^= u[j];
            }
        }
        return output;
    }

    private static byte[] hmacSha256(byte[] key, byte[] message) throws NoSuchAlgorithmException {
        if (key.length > BLOCK_SIZE) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            key = digest.digest(key);
        }
        if (key.length < BLOCK_SIZE) {
            key = Arrays.copyOf(key, BLOCK_SIZE);
        }

        byte[] oKeyPad = new byte[BLOCK_SIZE];
        byte[] iKeyPad = new byte[BLOCK_SIZE];

        for (int i = 0; i < BLOCK_SIZE; i++) {
            oKeyPad[i] = (byte) (key[i] ^ 0x5c);
            iKeyPad[i] = (byte) (key[i] ^ 0x36);
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        digest.update(iKeyPad);
        byte[] innerHash = digest.digest(message);

        digest.reset();
        digest.update(oKeyPad);
        return digest.digest(innerHash);
    }

    private static byte[] unionArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);

        return result;
    }
}