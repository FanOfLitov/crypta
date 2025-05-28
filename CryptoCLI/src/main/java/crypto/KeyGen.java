package crypto;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class KeyGen {
    public static byte[] deriveKey(String passphrase) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] salt = "CryptoCLISalt".getBytes("UTF-8");  // Статическая соль — для теста
        byte[] input = (passphrase + Arrays.toString(salt)).getBytes("UTF-8");
        byte[] hash = digest.digest(input);
        for (int i = 0; i < 10000; i++) {
            hash = digest.digest(hash); // усиление
        }
        return Arrays.copyOf(hash, 32);
    }
}
