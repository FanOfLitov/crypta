package crypto;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.nio.file.*;

@Command(name = "decrypt-dir", description = "Decrypts all encrypted files in a directory")
public class DecryptDirCommand implements Runnable {

    @Option(names = {"-i", "--input"}, required = true, description = "Input directory")
    File inputDir;

    @Option(names = {"-o", "--output"}, required = true, description = "Output directory")
    File outputDir;

    @Option(names = {"-p", "--passphrase"}, required = true, description = "Passphrase")
    String passphrase;

    public void run() {
        try {
            byte[] key = KeyGen.deriveKey(passphrase);
            Files.walk(inputDir.toPath()).filter(Files::isRegularFile).forEach(path -> {
                try {
                    byte[] data = Files.readAllBytes(path);
                    String originalName = MetaCryptor.extractFilename(data);
                    byte[] encrypted = MetaCryptor.stripMetadata(data);
                    byte[] decrypted = CryptoManager.decrypt(encrypted, key, CipherAlgorithm.CUSTOM);
                    Path outputPath = outputDir.toPath().resolve(originalName);
                    Files.createDirectories(outputPath.getParent());
                    Files.write(outputPath, decrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
