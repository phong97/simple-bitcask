package com.bitcask;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class BitcaskTest {
    private Bitcask bitcask;
    private final String testFile = "data/test.log";

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Path.of("data"));
        Files.deleteIfExists(Path.of(testFile));
        bitcask = new Bitcask(testFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        bitcask.close();
    }

    @Test
    void testPutAndGet() throws IOException {
        bitcask.put("key1", "value1");
        assertEquals("value1", bitcask.get("key1"), "Value should match initial put");

        bitcask.put("key1", "value2");
        assertEquals("value2", bitcask.get("key1"), "Value should update after new put");

        assertNull(bitcask.get("key2"), "Non-existent key should return null");
    }

    @Test
    void testPersistence() throws IOException {
        bitcask.put("key1", "persistent");
        bitcask.close();

        Bitcask newBitcask = new Bitcask(testFile);
        assertEquals("persistent", newBitcask.get("key1"), "Value should persist after reload");
        newBitcask.close();
    }
}
