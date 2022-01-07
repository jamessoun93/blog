# Transaction

트랜잭션이란 무엇일까요? 트랜잭션을 왜 사용할까요?

트랜잭션이란, 하나의 작업 단위로 처리되는 SQL 쿼리 모음입니다.

보통 원하는 작업을 전부 하나의 SQL 쿼리문으로 처리하기는 어렵습니다. 어떤 작업이냐에따라 다르겠지만 사실 불가능에 가깝죠.

이해를 돕기 위해 트랜잭션을 적용할만한 간단한 예시로 계좌이체 프로세스를 예를 들어보겠습니다.

계좌이체 프로세스를 간소화해서 실행해야하는 액션만 생각을 해본다면,

1. `SELECT` - 보내는 이의 계좌 잔고를 확인한다.
2. `UPDATE` - 보내는 이의 계좌 잔고에서 보낼 금액만큼 차감한다.
3. `UPDATE` - 받는 이의 계좌 잔고에 받은 만큼 금액을 증감한다.

트랜잭션은 이 세가지 작업을 하나의 작업으로 처리한다는 뜻입니다.

## Trasaction Lifespan

- `BEGIN` → Transaction 시작
- `COMMIT` → disk에 변경사항을 저장한다 (persist)
- `ROLLBACK` → 모든 변경사항을 되돌린다.
  - 만약 트랜잭션에서 20,000개의 쿼리를 실행하다가 중간에 문제가 생겼다고 가정해봅시다.
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
