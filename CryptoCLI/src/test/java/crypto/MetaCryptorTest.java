package crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MetaCryptorTest {
    @Test
    public void testMetadataEmbedding() {
        String filename = "original.txt";
        byte[] data = "Hello World".getBytes();
        byte[] embedded = MetaCryptor.embedMetadata(data, filename);
        String extractedName = MetaCryptor.extractFilename(embedded);
        byte[] extractedData = MetaCryptor.stripMetadata(embedded);
        assertEquals(filename, extractedName);
        assertArrayEquals(data, extractedData);
    }
}