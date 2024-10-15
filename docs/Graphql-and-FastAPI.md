# Building a Banking API in Python Using GraphQL, FastAPI, and SQLite
[Code](https://github.com/gsolano0625/tech-topics/tree/main/code/python/banking-graphql-fastapi)

In this guide, we will build a simple banking API using Python, FastAPI, GraphQL (via `Strawberry`), and `SQLite` for persistent data storage. We'll extend the previous example where we had in-memory data and replace it with a persistent database using SQLite. This will allow us to store and retrieve data across API sessions.

### Step 1: Install Required Libraries

You will need a few additional libraries to handle SQLite and ORM in Python:

```bash
pip install fastapi uvicorn strawberry-graphql sqlalchemy sqlite-utils
```

In this tutorial:
- **FastAPI**: A modern, high-performance web framework for Python.
- **Strawberry-GraphQL**: Integrates GraphQL into FastAPI.
- **SQLAlchemy**: A powerful ORM (Object Relational Mapper) to interact with the SQLite database.
- **SQLite-utils**: A utility for creating and managing SQLite databases.

### Step 2: Set Up the Database Using SQLAlchemy

We’ll start by creating the SQLite database and defining models for customers, accounts, and transactions using SQLAlchemy.

#### 1. Define SQLAlchemy Models

Create a file called `models.py` for database models:

```python
from sqlalchemy import create_engine, Column, Integer, Float, String, ForeignKey
from sqlalchemy.orm import declarative_base, relationship, sessionmaker

# Create a new SQLite database
DATABASE_URL = "sqlite:///./banking.db"

# Set up the database engine and session
engine = create_engine(DATABASE_URL, connect_args={"check_same_thread": False})
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Base model for other database models
Base = declarative_base()

class Customer(Base):
    __tablename__ = "customers"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)

    accounts = relationship("Account", back_populates="customer")


class Account(Base):
    __tablename__ = "accounts"

    id = Column(Integer, primary_key=True, index=True)
    customer_id = Column(Integer, ForeignKey("customers.id"))
    balance = Column(Float)

    customer = relationship("Customer", back_populates="accounts")
    transactions_from = relationship("Transaction", foreign_keys="Transaction.from_account_id")
    transactions_to = relationship("Transaction", foreign_keys="Transaction.to_account_id")


class Transaction(Base):
    __tablename__ = "transactions"

    id = Column(Integer, primary_key=True, index=True)
    from_account_id = Column(Integer, ForeignKey("accounts.id"))
    to_account_id = Column(Integer, ForeignKey("accounts.id"))
    amount = Column(Float)

    from_account = relationship("Account", foreign_keys=[from_account_id])
    to_account = relationship("Account", foreign_keys=[to_account_id])
```

- **Customer**: Represents a customer in the system.
- **Account**: Represents a bank account, associated with a customer.
- **Transaction**: Records money transfers between two accounts.

#### 2. Create the Database Tables

To initialize the database and create the necessary tables, use the following code:

```python
from models import Base, engine

# Create all tables
Base.metadata.create_all(bind=engine)
```

### Step 3: Define GraphQL Schema Using Strawberry

We will now define our GraphQL schema using `Strawberry`. We’ll update the queries and mutations to interact with the SQLite database through SQLAlchemy.

#### 1. Define GraphQL Types

Create a file `graphql_schema.py` to define the types, queries, and mutations:

```python
import strawberry
from typing import List, Optional
from models import Customer, Account, Transaction
from database import get_db  # Utility to get the session

@strawberry.type
class CustomerType:
    id: int
    name: str

@strawberry.type
class AccountType:
    id: int
    balance: float
    customer: CustomerType

@strawberry.type
class TransactionType:
    id: int
    from_account: AccountType
    to_account: AccountType
    amount: float
```

#### 2. Define Queries

Next, we define the queries to retrieve data from the SQLite database.

```python
@strawberry.type
class Query:
    @strawberry.field
    def customers(self) -> List[CustomerType]:
        db = next(get_db())
        db_customers = db.query(Customer).all()
        return [CustomerType(id=c.id, name=c.name) for c in db_customers]

    @strawberry.field
    def accounts(self) -> List[AccountType]:
        db = next(get_db())
        db_accounts = db.query(Account).all()
        return [
            AccountType(id=a.id, balance=a.balance, customer=CustomerType(id=a.customer.id, name=a.customer.name))
            for a in db_accounts
        ]

    @strawberry.field
    def transactions(self) -> List[TransactionType]:
        db = next(get_db())
        db_transactions = db.query(Transaction).all()
        return [
            TransactionType(
                id=t.id,
                from_account=AccountType(id=t.from_account.id, balance=t.from_account.balance, customer=CustomerType(id=t.from_account.customer.id, name=t.from_account.customer.name)),
                to_account=AccountType(id=t.to_account.id, balance=t.to_account.balance, customer=CustomerType(id=t.to_account.customer.id, name=t.to_account.customer.name)),
                amount=t.amount,
            )
            for t in db_transactions
        ]
```

#### 3. Define Mutations

We define mutations to create accounts and transfer funds between accounts.

```python
@strawberry.type
class Mutation:
    @strawberry.mutation
    def create_customer(self, name: str) -> CustomerType:
        db = next(get_db())

        new_customer = Customer(name=name)
        db.add(new_customer)
        db.commit()
        db.refresh(new_customer)
        return CustomerType(id=new_customer.id, name=new_customer.name)


    @strawberry.mutation
    def create_account(self, customer_id: int, initial_balance: float) -> AccountType:
        db = next(get_db())
        customer = db.query(Customer).filter(Customer.id == customer_id).first()
        if not customer:
            raise ValueError("Customer not found")

        new_account = Account(customer_id=customer_id, balance=initial_balance)
        db.add(new_account)
        db.commit()
        db.refresh(new_account)
        return AccountType(id=new_account.id, balance=new_account.balance, customer=CustomerType(id=customer.id, name=customer.name))

    @strawberry.mutation
    def transfer_funds(self, from_account_id: int, to_account_id: int, amount: float) -> bool:
        db = next(get_db())
        from_account = db.query(Account).filter(Account.id == from_account_id).first()
        to_account = db.query(Account).filter(Account.id == to_account_id).first()

        if not from_account or not to_account:
            raise ValueError("Account not found")

        if from_account.balance < amount:
            raise ValueError("Insufficient balance")

        from_account.balance -= amount
        to_account.balance += amount

        new_transaction = Transaction(from_account_id=from_account_id, to_account_id=to_account_id, amount=amount)
        db.add(new_transaction)
        db.commit()

        return True
```

- **create_account**: Creates a new account for an existing customer.
- **transfer_funds**: Transfers funds between two accounts and records the transaction.

### Step 4: FastAPI Integration

Now, we need to integrate the GraphQL schema with FastAPI and expose the API.

#### 1. Create Database Session Dependency

Create a `database.py` file with a utility to get the database session:

```python
from models import SessionLocal

# Dependency to get the database session
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
```

#### 2. Integrate FastAPI with Strawberry

We need to connect FastAPI and Strawberry, using the `graphql_schema.py` file we created.

```python
from fastapi import FastAPI
from strawberry.fastapi import GraphQLRouter
from graphql_schema import Query, Mutation
import strawberry

# Create FastAPI instance
app = FastAPI()

# Create GraphQL schema
schema = strawberry.Schema(query=Query, mutation=Mutation)

# Add GraphQL route
graphql_app = GraphQLRouter(schema)

app.include_router(graphql_app, prefix="/graphql")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
```

### Step 5: Running the API

To run the API, execute the following command:

```bash
uvicorn app:app --reload
```

The API will be accessible at `http://localhost:8000/graphql`. You can query and mutate data through the GraphQL endpoint.

### Step 6: Example Queries and Mutations

Here are a few sample GraphQL queries and mutations to test the API:

#### Create Customers

```graphql
mutation createCustomers {
  c1: createCustomer(name: "John Pachuco") {
    id
    name
  }
  
  c2: createCustomer(name: "Alex Johnson") {
    id
    name
  }
}
```

#### 2. Creating New Accounts

```graphql
mutation createfirstAccounts {
  c1: createAccount(customerId: 3, initialBalance: 5000) {
    id
    balance
    customer {
      name
    }
  }
  
  c2: createAccount(customerId: 2, initialBalance: 7500) {
    id
    balance
    customer {
      name
    }
  }  
}
```

#### Fetching All Accounts

```graphql
query Banking {
  customers {
    id
    name
  }
  
  accounts {
    id
    balance
    customer {
      name
    }
  }
}
```

#### Transferring Funds Between Accounts

```graphql
mutation {
  transferFunds(fromAccountId: 1, toAccountId: 2, amount: 300) 
}
```

### Conclusion

In this tutorial, we successfully built a banking API using FastAPI, GraphQL, and SQLite. We used SQLAlchemy as our ORM to interact with the database and `Strawberry` to implement the Graph

- **TransactionType**: Represents a transaction between two accounts.
