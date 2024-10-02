package com.pudito.banking;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.pudito.banking.models.Transaction;
//import com.pudito.banking.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam Long accountId, @RequestParam Double amount) {
        return new ResponseEntity<>(transactionService.deposit(accountId, amount), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestParam Long accountId, @RequestParam Double amount) {
        return new ResponseEntity<>(transactionService.withdraw(accountId, amount), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam Double amount) {
        return new ResponseEntity<>(transactionService.transfer(fromAccountId, toAccountId, amount), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long accountId) {
        return new ResponseEntity<>(transactionService.getTransactionHistory(accountId), HttpStatus.OK);
    }
}