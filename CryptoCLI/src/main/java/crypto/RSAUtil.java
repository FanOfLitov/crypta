package crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAUtil {
    public static class RSAKeyPair {
        public final BigInteger publicKey;
        public final BigInteger privateKey;
        public final BigInteger modulus;

        public RSAKeyPair(BigInteger pub, BigInteger priv, BigInteger mod) {
            this.publicKey = pub;
            this.privateKey = priv;
            this.modulus = mod;
        }
    }

    public static RSAKeyPair generateKeyPair(int bitLength) {
        SecureRandom rnd = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength / 2, rnd);
        BigInteger q = BigInteger.probablePrime(bitLength / 2, rnd);
        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(65537);
        BigInteger d = e.modInverse(phi);
        return new RSAKeyPair(e, d, n);
    }

    public static byte[] encrypt(byte[] data, byte[] keyBytes) {
        BigInteger e = new BigInteger(1, keyBytes);
        BigInteger m = new BigInteger(1, data);
        BigInteger n = getDefaultModulus();
        return m.modPow(e, n).toByteArray();
    }

    public static byte[] decrypt(byte[] data, byte[] keyBytes) {
        BigInteger d = new BigInteger(1, keyBytes);
        BigInteger c = new BigInteger(1, data);
        BigInteger n = getDefaultModulus();
        return c.modPow(d, n).toByteArray();
    }

    // Replace with generated modulus for proper use
    private static BigInteger getDefaultModulus() {
        return new BigInteger("00d3f52fb1b3c6ea1ccfbb2fded3c115c7", 16); // placeholder
    }
}