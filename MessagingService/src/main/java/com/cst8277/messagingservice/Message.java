package com.cst8277.messagingservice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Message {
    private UUID id;
    private String content;
    private ZonedDateTime created;
    private UUID producer_id;

    public Message(UUID id, String content, ZonedDateTime created, UUID producer_id) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.producer_id = producer_id;
    }

    // Getters and setters

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public UUID getProducerId() {
        return producer_id;
    }

    public void setProducerId(UUID producer_id) {
        this.producer_id = producer_id;
    }
}
