package main

import (
	"fmt"
	"gsolano/banking/models"
)

type Account = models.Account
type SavingsAccount = models.SavingsAccount
type CheckingAccount = models.CheckingAccount
type BankAccount = models.BankAccount

func transfer(source BankAccount, target BankAccount, amount float64) bool {
	amountBeforeWithdraw := source.CheckBalance()
	source.Withdraw(amount)

	if amountBeforeWithdraw > source.CheckBalance() {
		target.Deposit(amount)
		return true
	}

	return false
}

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

	// Transfer money from savings to checking
	transfer(savings, checking, 500)
	fmt.Println("Savings Balance after transfer:", savings.CheckBalance())
	fmt.Println("Checking Balance after transfer:", checking.CheckBalance())
}
