
// ---------- EncryptFileCommand.java ----------
package crypto;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.nio.file.Files;

@Command(name = "encrypt-file", description = "Encrypts a single file")
public class EncryptFileCommand implements Runnable {

    @Option(names = {"-i", "--input"}, required = true, description = "Input file")
    File inputFile;

    @Option(names = {"-o", "--output"}, required = true, description = "Output encrypted file")
    File outputFile;

    @Option(names = {"-p", "--passphrase"}, required = true, description = "Passphrase")
    String passphrase;

    public void run() {
        try {
            byte[] plaintext = Files.readAllBytes(inputFile.toPath());
            byte[] key = KeyGen.deriveKey(passphrase);
            byte[] ciphertext = CryptoManager.encrypt(plaintext, key, CipherAlgorithm.CUSTOM);
            Files.write(outputFile.toPath(), MetaCryptor.embedMetadata(ciphertext, inputFile.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
