package crypto;
import crypto.CipherAlgorithm;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.nio.file.Files;

@Command(name = "decrypt-file", description = "Decrypts a single file")
public class DecryptFileCommand implements Runnable {

    @Option(names = {"-i", "--input"}, required = true, description = "Input encrypted file")
    File inputFile;

    @Option(names = {"-o", "--output"}, required = true, description = "Output decrypted file")
    File outputFile;

    @Option(names = {"-p", "--passphrase"}, required = true, description = "Passphrase")
    String passphrase;

    public void run() {
        try {
            byte[] encrypted = Files.readAllBytes(inputFile.toPath());
            String filename = MetaCryptor.extractFilename(encrypted);
            byte[] ciphertext = MetaCryptor.stripMetadata(encrypted);
            byte[] key = KeyGen.deriveKey(passphrase);
            byte[] plaintext = CryptoManager.decrypt(ciphertext, key, CipherAlgorithm.CUSTOM); // или KUZNECHIK / RSA
            Files.write(outputFile.toPath(), plaintext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}