package com.cst8277.subscriptionservice;

import java.util.UUID;

public class Subscriber {
    private UUID id;
    private String comment;

    public Subscriber(byte[] idBytes, String name) {
        this.id = convertBytesToUUID(idBytes);
        this.comment = comment;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private UUID convertBytesToUUID(byte[] bytes) {
        if (bytes == null || bytes.length != 16) {
            throw new IllegalArgumentException("Invalid UUID byte array");
        }
        long mostSigBits = 0;
        long leastSigBits = 0;
        for (int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (bytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (bytes[i] & 0xff);
        }
        return new UUID(mostSigBits, leastSigBits);
    }
}
