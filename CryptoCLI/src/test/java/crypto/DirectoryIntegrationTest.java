package crypto;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DirectoryIntegrationTest {
    File tempInputDir, tempOutputDir;

    @BeforeAll
    public void setup() throws IOException {
        tempInputDir = Files.createTempDirectory("encinput").toFile();
        tempOutputDir = Files.createTempDirectory("encoutput").toFile();
        Files.write(new File(tempInputDir, "test1.txt").toPath(), "Data 1".getBytes());
        Files.write(new File(tempInputDir, "test2.txt").toPath(), "Data 2".getBytes());
    }

    @Test
    public void testEncryptDecryptDir() throws Exception {
        EncryptDirCommand enc = new EncryptDirCommand();
        enc.inputDir = tempInputDir;
        enc.outputDir = tempOutputDir;
        enc.passphrase = "dirpass";
        enc.run();

        DecryptDirCommand dec = new DecryptDirCommand();
        dec.inputDir = tempOutputDir;
        File finalOutputDir = Files.createTempDirectory("decrypted").toFile();
        dec.outputDir = finalOutputDir;
        dec.passphrase = "dirpass";
        dec.run();

        for (String filename : new String[]{"test1.txt", "test2.txt"}) {
            byte[] original = Files.readAllBytes(new File(tempInputDir, filename).toPath());
            byte[] decrypted = Files.readAllBytes(new File(finalOutputDir, filename).toPath());
            assertArrayEquals(original, decrypted);
        }
    }
}
