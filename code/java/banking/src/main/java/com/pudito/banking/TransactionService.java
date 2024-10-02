package com.pudito.banking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import com.pudito.banking.models.Account;
import com.pudito.banking.models.Transaction;
import com.pudito.banking.models.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction deposit(Long accountId, Double amount) {
        Account account = accountService.getAccount(accountId);
        account.setBalance(account.getBalance() + amount);
        accountService.updateAccount(accountId, account);

        return transactionRepository.save(new Transaction(accountId, "DEPOSIT", amount, LocalDateTime.now()));
    }

    public Transaction withdraw(Long accountId, Double amount) {
        Account account = accountService.getAccount(accountId);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        accountService.updateAccount(accountId, account);

        return transactionRepository.save(new Transaction(accountId, "WITHDRAWAL", amount, LocalDateTime.now()));
    }

    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount) {
        withdraw(fromAccountId, amount);
        deposit(toAccountId, amount);
        return transactionRepository.save(new Transaction(fromAccountId, "TRANSFER", amount, LocalDateTime.now()));
    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}