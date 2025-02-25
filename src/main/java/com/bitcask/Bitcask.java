package com.bitcask;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class Bitcask {
    
    private Map<String, Long> keyDir; /// key -> offset
    private RandomAccessFile logFile; /// log file
    private String filePath;

    public Bitcask(String filePath) throws IOException {
        this.filePath = filePath;
        this.keyDir = new HashMap<>();
        this.logFile = new RandomAccessFile(filePath, "rw");
        loadKeyDir(); // load keyDir from log file
    }

    // load keyDir from log file when Bitcask is created 
    private void loadKeyDir() throws IOException {
        logFile.seek(0);
        long offset = 0;
        String line;
        while ((line = logFile.readLine()) != null) {
            Entry entry = Entry.fromString(line, offset);
            keyDir.put(entry.key, offset); // only save the latest offset
            offset = logFile.getFilePointer();
        }
    }

    public void put(String key, String value) throws IOException {
        long offset = logFile.getFilePointer();
        Entry entry = new Entry(key, value, System.currentTimeMillis(), offset);
        byte[] entryBytes = entry.toBytes();

        // write entry to log file
        logFile.seek(offset);
        logFile.write(entryBytes);

        // update keyDir
        keyDir.put(key, offset);
    }

    public String get(String key) throws IOException {
        Long offset = keyDir.get(key);
        if (offset == null) {
            return null; // key not found
        }

        // read entry from log file
        logFile.seek(offset);
        String line = logFile.readLine();
        Entry entry = Entry.fromString(line, offset);
        return entry.value;
    }

    public void close() throws IOException {
        logFile.close();
    }
}
