package com.pudito.banking.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountHolder;
    private Double balance = 0.0;

    public Account() {
    }

    public Account(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void deposit(Double amount) {
        this.balance += amount;
    }

    public void withdraw(Double amount) {
        if (this.balance < amount) {
            throw new RuntimeException("Not enough balance");
        }
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountHolder='" + accountHolder + '\'' +
                ", balance=" + balance +
                '}';
    }

    // write override equals and hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Account account = (Account) obj;
        return id.equals(account.id) && accountHolder.equals(account.accountHolder) && balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountHolder, balance);
    }

}