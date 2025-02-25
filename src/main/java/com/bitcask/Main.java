package com.bitcask;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Bitcask bitcask = new Bitcask("data/bitcask.log");
            bitcask.put("user1", "Alice");
            bitcask.put("user2", "Bob");
            bitcask.put("user1", "Alice Updated"); // update data

            System.out.println(bitcask.get("user1")); // In: Alice Updated
            System.out.println(bitcask.get("user2")); // In: Bob
            System.out.println(bitcask.get("user3")); // In: null

            bitcask.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
