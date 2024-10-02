# API Design in Spring Boot: A Banking Example

In modern software development, designing a well-structured API is crucial for scalability, security, and ease of use. **Spring Boot** offers an efficient framework to build REST APIs, making it easier for developers to focus on business logic while Spring handles infrastructure concerns like dependency injection, security, and database connectivity.

In this article, we'll explore **API design** using **Spring Boot**, and demonstrate its application through a simple **banking system**.

### 1. Requirements of a Banking API

Before diving into code, let's outline the basic requirements for our banking API:

1. **Account Management:**
    - Create, update, and retrieve bank accounts.
    - View account balance.

2. **Transaction Management:**
    - Deposit and withdraw money from accounts.
    - Transfer funds between accounts.
    - View transaction history.

3. **Security:**
    - Only authenticated users should perform banking operations.

4. **Error Handling:**
    - Proper validation and error handling to ensure smooth client interaction.

### 2. Defining the API Endpoints

Here are the RESTful endpoints that we will design:

- **Account Management:**
    - `POST /accounts`: Create a new bank account.
    - `GET /accounts/{id}`: Get account details by account ID.
    - `PUT /accounts/{id}`: Update account details.
    - `GET /accounts/{id}/balance`: Get the balance of a specific account.

- **Transaction Management:**
    - `POST /transactions/deposit`: Deposit money into an account.
    - `POST /transactions/withdraw`: Withdraw money from an account.
    - `POST /transactions/transfer`: Transfer money between two accounts.
    - `GET /transactions/{accountId}`: Get the transaction history of an account.

### 3. Setting Up the Spring Boot Project

First, initialize a Spring Boot project with the required dependencies:

1. **Spring Web**: For creating the REST endpoints.
2. **Spring Data JPA**: For data persistence.
3. **Spring Security**: To add authentication and authorization.
4. **H2 Database**: For easy in-memory database management during development.

#### Maven

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

#### Gradle - Groovy


```groovy
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	runtimeOnly 'com.h2database:h2'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.springframework.security:spring-security-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

### 4. Model Classes

Let's start by defining our domain objects: **Account** and **Transaction**.

#### Account Model

```java
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String accountHolder;
    private Double balance = 0.0;

    // Constructors, getters, setters
}
```

#### Transaction Model

```java
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long accountId;
    private String type; // "DEPOSIT", "WITHDRAWAL", "TRANSFER"
    private Double amount;
    private LocalDateTime timestamp;

    // Constructors, getters, setters
}
```

### 5. Service Layer

The service layer contains the business logic of the application. Here's how the service for account management and transaction processing might look.

#### AccountService

```java
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
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
```

#### TransactionService

```java
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
```

### 6. Controller Layer

The controller layer handles incoming HTTP requests and routes them to the appropriate service.

#### AccountController

```java
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.getAccount(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return new ResponseEntity<>(accountService.updateAccount(id, account), HttpStatus.OK);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.getBalance(id), HttpStatus.OK);
    }
}
```

#### TransactionController

```java
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
```

### 7. Error Handling and Security

#### Custom Exception Handling

A custom exception handler can be used to ensure all errors are properly handled:

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFunds(InsufficientFundsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
```

#### Securing the API

By default, Spring Boot includes basic security configurations. For simplicity, we can allow access to the APIs with basic authentication or by using JWT tokens for more advanced security setups.

### 8. Testing the API

Once your API is set up, you can use tools like **Postman** or **cURL** to test the different endpoints.

#### Example Request: Create an Account

```bash
curl -X POST http://localhost:8080/accounts -H "Content-Type: application/json" -d '{"accountHolder": "John Doe"}'
```

#### Example Request: Deposit Money

```bash
curl -X POST http://localhost:8080/transactions/deposit?accountId=1&amount=100
```

### Conclusion

This example illustrates how to design a basic banking API using Spring Boot. We covered:

- Setting up RESTful endpoints.
- Creating a service layer to handle the business logic.
- Managing accounts and transactions.
- Securing and handling errors in the

API.

This is just a starting point, and you can expand it by adding advanced features such as currency management, auditing, multi-factor authentication, etc. Proper API design ensures scalability and maintainability, especially in critical systems like banking.