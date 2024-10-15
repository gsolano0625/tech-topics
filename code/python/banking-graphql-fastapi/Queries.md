Queries

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

query banking {
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

mutation createfirstAccount {
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

mutation transfer {
  transferFunds(fromAccountId: 1, toAccountId: 2, amount: 300) 
}
```
