package crypto;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.nio.file.*;

@Command(name = "encrypt-dir", description = "Encrypts all files in a directory")
public class EncryptDirCommand implements Runnable {

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
                    byte[] encrypted = CryptoManager.encrypt(data, key, CipherAlgorithm.CUSTOM);
                    byte[] finalData = MetaCryptor.embedMetadata(encrypted, path.getFileName().toString());
                    Path outputPath = outputDir.toPath().resolve(path.getFileName().toString() + ".crypt");
                    Files.createDirectories(outputPath.getParent());
                    Files.write(outputPath, finalData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}