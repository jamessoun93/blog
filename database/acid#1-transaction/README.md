# Transaction

**Definition:** collection of SQL queries that are treated as one unit of work.

It’s very hard to do everything you want in one query. (it’s also impossible)

E.g. Account Deposit (SELECT, UPDATE, UPDATE) → transaction consisting of 3 different queries.

## Trasaction Lifespan

- `BEGIN`
- `COMMIT` → persist this change to disk
- `ROLLBACK` → all the changes, **forget about it**
    - let’s say you’re making 20000 queries and there’s a crash in the middle of it. As you’re making them, do you persist them to disk? if you did, you’re gonna have to go undo all those work.
    - when it comes to crash in the middle of 10000 queries, the database better know to rollback these changes.
    - you can optimize your database based on your use case, mysql, postgres, sql server, oracle each one optimizes on certain things.
        - I think Im going to optimize for crashes
        - I think im going to optimize for commits
        - In Postgres, commits are fastest because they do that in ways such that any query that is executed during the transaction, they try to persist this change. Postgres does a lot of I/O but their commits are really fast.
    - What if during the commit, you get a crash → if your commits are fast, the risk is low but if your commits are slow like SQL server, if you have a large transaction thats scary (is it committed or not?)

## Nature of Transactions

- usually transactions are used to change and modify data
- however, its perfectly normal to have a read-only transaction
    - when you actually tell the database that, it can optimize itself to it.
- Use case?
    - you want a transaction to maintain consistency
    - Example, you want to generate a report and you want to get consistent snapshot based at the time of transaction
        - I want anything that I read is based on the initial time
        - anything you read, if something changed by a concurrent transaction, you don’t care. you want to be **isolated.**

## Example

Send $100 From Account 1 to Account 2

```sql
BEGIN TX1

SELECT BALANCE FROM ACCOUNT WHERE ID = 1
BALANCE > 100 (constraint: balance can't go below 0, if it does it means its inconsistent data)

UPDATE ACCOUNT SET BALANCE = BALANCE - 100 WHERE ID = 1
UPDATE ACCOUNT SET BALANCE = BALANCE + 100 WHERE ID = 2

COMMIT TX1
```

## Things to note

- When you don’t start a transaction, database will start one for you.
- so if you execute a normal update statement or insert, the database in the backend starts a transaction and almost immediately commits it.
- so we’re always in a transaction.
- some transactions are user defined and some are actually built and implicitly defined by the system.