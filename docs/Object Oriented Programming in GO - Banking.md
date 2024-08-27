# Implementing Object-Oriented Programming in Go: A Banking Example

Object-Oriented Programming (OOP) is a popular paradigm that organizes software design around data, or objects, rather than functions and logic. In Go, although not a purely object-oriented language, we can still apply OOP principles effectively. 

In this article, we’ll explore how to implement OOP concepts in Go by building a simple banking system.

### Understanding the Banking Example

Let’s imagine we are building a basic banking application. The application should allow us to create accounts, deposit money, withdraw money, and check balances. We will use Go’s structs, methods, and interfaces to model these functionalities.

### Defining the Account Struct

In Go, structs are the primary way to define complex data types. For our banking example, we’ll start by defining an `Account` struct to represent a bank account.

```go
type Account struct {
    AccountNumber string
    Balance       float64
}
```

Here, `AccountNumber` is a string representing the account’s unique identifier, and `Balance` is a float representing the current balance of the account.

### Adding Methods to the Account Struct

Next, we’ll add methods to our `Account` struct to perform operations such as depositing and withdrawing money.

```go
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
```

- **Deposit**: Adds a specified amount to the account balance.
- **Withdraw**: Subtracts a specified amount from the account balance, with checks to ensure the amount is positive and does not exceed the current balance.
- **CheckBalance**: Returns the current balance of the account.

### Implementing Interfaces for Different Account Types

Now, let’s introduce different types of accounts—say, a savings account and a checking account. We’ll use Go’s interfaces to achieve polymorphism, allowing us to treat different account types uniformly.

```go
type BankAccount interface {
    Deposit(amount float64)
    Withdraw(amount float64)
    CheckBalance() float64
}
```

The `BankAccount` interface defines the methods that any type of account must implement. Since our `Account` struct already has these methods, it automatically satisfies the `BankAccount` interface.

### Creating Specialized Account Types

Now, let’s create specific types of accounts. For example, a savings account might have an interest rate, while a checking account might have a different feature set.

```go
type SavingsAccount struct {
    Account
    InterestRate float64
}

type CheckingAccount struct {
    Account
    OverdraftLimit float64
}
```

The `SavingsAccount` embeds the `Account` struct and adds an `InterestRate` field. Similarly, the `CheckingAccount` embeds `Account` and adds an `OverdraftLimit` field.

### Adding Methods to Specialized Accounts

We can now add methods specific to our specialized account types. For example, we might want to apply interest to a savings account.

```go
func (sa *SavingsAccount) ApplyInterest() {
    interest := sa.Balance * sa.InterestRate / 100
    sa.Deposit(interest)
    fmt.Printf("Applied interest: %.2f\n", interest)
}
```

This method calculates interest based on the current balance and interest rate, then deposits the interest back into the account.

For the `CheckingAccount`, we might want to modify the `Withdraw` method to take the overdraft limit into account:

```go
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
```

This version of `Withdraw` checks whether the withdrawal amount exceeds the combined balance and overdraft limit before proceeding.

### Using the Banking System

With everything in place, let’s see how our banking system works in practice.

```go
func main() {
    savings := &SavingsAccount{
        Account:      Account{AccountNumber: "12345", Balance: 1000},
        InterestRate: 5.0,
    }

    checking := &CheckingAccount{
        Account:       Account{AccountNumber: "67890", Balance: 500},
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
```

### Conclusion

In this article, we demonstrated how to apply object-oriented programming concepts in Go using a banking system example. We saw how Go’s structs, methods, interfaces, and composition enable us to model complex systems in an object-oriented way, even without traditional OOP features like classes and inheritance.

Go's emphasis on composition over inheritance allows for simpler and more maintainable code, while interfaces provide flexibility and polymorphism. This approach is well-suited to Go’s philosophy of simplicity and efficiency, making it an excellent choice for building robust and scalable applications.