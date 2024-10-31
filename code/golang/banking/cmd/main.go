package main

import (
	"fmt"
	"gsolano/banking/models"
)

type Account = models.Account
type SavingsAccount = models.SavingsAccount
type CheckingAccount = models.CheckingAccount

func main() {
	savings := &SavingsAccount{
		Account:      Account{AccountNumber: "12345", Balance: 1000},
		InterestRate: 5.0,
	}

	checking := &CheckingAccount{
		Account:        Account{AccountNumber: "67890", Balance: 500},
		OverdraftLimit: 200,
	}

	// Deposit money into savings
	savings.Deposit(200)
	fmt.Println("Savings Balance:", savings.CheckBalance())

	// Apply interest to savings
	savings.ApplyInterest()
	fmt.Println("Savings Balance after interest:", savings.CheckBalance())

	// Withdraw money from checking
	checking.Withdraw(600)
	fmt.Println("Checking Balance:", checking.CheckBalance())

	// Try to withdraw more than overdraft limit allows
	checking.Withdraw(200)
	fmt.Println("Checking Balance:", checking.CheckBalance())
}
