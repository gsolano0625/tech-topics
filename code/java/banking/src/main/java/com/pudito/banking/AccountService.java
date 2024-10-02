package com.pudito.banking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pudito.banking.models.Account;
import com.pudito.banking.models.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account updateAccount(Long accountId, Account updatedAccount) {
        Account existingAccount = getAccount(accountId);
        existingAccount.setAccountHolder(updatedAccount.getAccountHolder());
        return accountRepository.save(existingAccount);
    }

    public Double getBalance(Long accountId) {
        return getAccount(accountId).getBalance();
    }
}