package com.pudito.banking.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;
    private String type; // "DEPOSIT", "WITHDRAWAL", "TRANSFER"
    private Double amount;
    private LocalDateTime timestamp;

    // Constructors, getters, setters, and toString
    public Transaction() {
    }

    public Transaction(Long accountId, String type, Double amount) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(Long accountId, String type, Double amount, LocalDateTime timestamp) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    //public void setId(Long id) {
    //    this.id = id;
    //}

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    // override equals and hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Transaction that = (Transaction) obj;
        return id.equals(that.id) && accountId.equals(that.accountId) && type.equals(that.type) && amount.equals(that.amount) && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, type, amount, timestamp);
    }
}