package models

import "fmt"

type BankAccount interface {
	Deposit(amount float64)
	Withdraw(amount float64)
	CheckBalance() float64
}

type SavingsAccount struct {
	Account
	InterestRate float64
}

type CheckingAccount struct {
	Account
	OverdraftLimit float64
}

func (sa *SavingsAccount) ApplyInterest() {
	interest := sa.Balance * sa.InterestRate / 100
	sa.Deposit(interest)
	fmt.Printf("Applied interest: %.2f\n", interest)
}

func (ca *CheckingAccount) Withdraw(amount float64) {
	if amount <= 0 {
		fmt.Println("Withdrawal amount must be positive.")
		return
	}
	if amount > ca.Balance+ca.OverdraftLimit {
		fmt.Println("Insufficient funds, even with overdraft.")
		return
	}
	ca.Balance -= amount
	fmt.Printf("Withdrew: %.2f\n", amount)
}
