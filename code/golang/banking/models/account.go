package models

import "fmt"

type Account struct {
	AccountNumber string
	Balance       float64
}

func (a *Account) Deposit(amount float64) {
	if amount <= 0 {
		fmt.Println("Deposit amount must be positive.")
		return
	}
	a.Balance += amount
	fmt.Printf("Deposited: %.2f\n", amount)
}

func (a *Account) Withdraw(amount float64) {
	if amount <= 0 {
		fmt.Println("Withdrawal amount must be positive.")
		return
	}
	if amount > a.Balance {
		fmt.Println("Insufficient funds.")
		return
	}
	a.Balance -= amount
	fmt.Printf("Withdrew: %.2f\n", amount)
}

func (a *Account) CheckBalance() float64 {
	return a.Balance
}
