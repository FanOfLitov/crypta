package crypto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MetaCryptor {
    public static byte[] embedMetadata(byte[] data, String filename) {
        byte[] nameBytes = filename.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(4 + nameBytes.length + data.length);
        buffer.putInt(nameBytes.length);
        buffer.put(nameBytes);
        buffer.put(data);
        return buffer.array();
    }

    public static byte[] stripMetadata(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int len = buffer.getInt();
        buffer.position(4 + len);
        byte[] result = new byte[data.length - 4 - len];
        buffer.get(result);
        return result;
    }

    public static String extractFilename(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int len = buffer.getInt();
        byte[] name = new byte[len];
        buffer.get(name);
        return new String(name, StandardCharsets.UTF_8);
    }
}