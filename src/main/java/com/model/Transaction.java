package com.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("amount")
    @Column(nullable = false)
    private Double amount;

    @JsonProperty("currency")
    @Column(nullable = false, length = 3)
    private String currency;

    @JsonProperty("user_id")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Transaction(Double amount, String currency, Long userId) {
        this.amount = amount;
        this.currency = currency;
        this.userId = userId;
        this.status = Status.NEW;
        this.timestamp = LocalDateTime.now();
    }

    public enum Status {
        @JsonProperty("NEW") NEW,
        @JsonProperty("SUSPICIOUS") SUSPICIOUS,
        @JsonProperty("CONFIRMED") CONFIRMED
    }
}