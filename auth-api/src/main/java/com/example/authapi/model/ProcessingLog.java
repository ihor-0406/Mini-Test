package com.example.authapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processing_log")
public class ProcessingLog {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private UUID userId;

    @Column(name = "input_text", nullable = false, length = 2000)
    private String inputText;

    @Column(name = "output_text", nullable = false, length = 2000)
    private String outputText;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public ProcessingLog() {}

    public ProcessingLog(UUID userId, String inputText, String outputText) {
        this.userId = userId;
        this.inputText = inputText;
        this.outputText = outputText;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
