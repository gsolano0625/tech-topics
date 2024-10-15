import strawberry
from typing import List
from models import Customer, Account, Transaction
from database import get_db  # Utility to get the session

# Types for the GraphQL schema
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


# Queries
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

# Mutations
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
