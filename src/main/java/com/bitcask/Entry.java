package main.java.com.bitcask;

import java.nio.charset.StandardCharsets;

public class Entry {
    String key;
    String value;
    long timestamp;
    long offset;

    public Entry(String key, String value, long timestamp, long offset) {
        this.key = key;
        this.value = value;
        this.timestamp = timestamp;
        this.offset = offset;
    }

    public byte[] toBytes() {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        String entryStr = keyBytes.length + "," + valueBytes.length + "," + timestamp + "," + key + "," + value + "\n";
        return entryStr.getBytes(StandardCharsets.UTF_8);
    }

    public static Entry fromString(String line, long offset) {
        String[] parts = line.split(",", 4);
        String key = parts[3].substring(0, Integer.parseInt(parts[0]));
        String value = parts[3].substring(Integer.parseInt(parts[0]) + 1);
        long timestamp = Long.parseLong(parts[2]);
        return new Entry(key, value, timestamp, offset);
    }
}
