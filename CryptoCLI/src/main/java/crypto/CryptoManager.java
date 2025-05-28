package crypto;

import java.util.Arrays;

public class CryptoManager {
    public static byte[] encrypt(byte[] data, byte[] key, CipherAlgorithm algo) throws Exception {
        switch (algo) {
            case CUSTOM:
                return customEncrypt(data, key);
            case KUZNECHIK:
                return KuznechikCipher.encrypt(data, key);
            case RSA:
                return RSAUtil.encrypt(data, key);
            default:
                throw new IllegalArgumentException("Unsupported algorithm");
        }
    }

    public static byte[] decrypt(byte[] data, byte[] key, CipherAlgorithm algo) throws Exception {
        switch (algo) {
            case CUSTOM:
                return customDecrypt(data, key);
            case KUZNECHIK:
                return KuznechikCipher.decrypt(data, key);
            case RSA:
                return RSAUtil.decrypt(data, key);
            default:
                throw new IllegalArgumentException("Unsupported algorithm");
        }
    }

    private static byte[] customEncrypt(byte[] data, byte[] key) {
        byte[] result = Arrays.copyOf(data, data.length);
        for (int round = 0; round < 2; round++) {
            result = substitute(result, key);
            result = permute(result, key);
        }
        return result;
    }

    private static byte[] customDecrypt(byte[] data, byte[] key) {
        byte[] result = Arrays.copyOf(data, data.length);
        for (int round = 0; round < 2; round++) {
            result = inversePermute(result, key);
            result = inverseSubstitute(result, key);
        }
        return result;
    }

    private static byte[] substitute(byte[] input, byte[] key) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) (input[i] ^ key[i % key.length]);
        }
        return result;
    }

    private static byte[] inverseSubstitute(byte[] input, byte[] key) {
        return substitute(input, key);
    }

    private static byte[] permute(byte[] input, byte[] key) {
        byte[] result = Arrays.copyOf(input, input.length);
        for (int i = 0; i < result.length - 1; i++) {
            int j = (key[i % key.length] & 0xFF) % result.length;
            byte tmp = result[i];
            result[i] = result[j];
            result[j] = tmp;
        }
        return result;
    }

    private static byte[] inversePermute(byte[] input, byte[] key) {
        byte[] result = Arrays.copyOf(input, input.length);
        for (int i = result.length - 2; i >= 0; i--) {
            int j = (key[i % key.length] & 0xFF) % result.length;
            byte tmp = result[i];
            result[i] = result[j];
            result[j] = tmp;
        }
        return result;
    }
}
