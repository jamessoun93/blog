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
  - 만약 트랜잭션에서 20,000개의 쿼리를 실행하다가 중간에 문제가 생겼다고 가정해봅시다. 문제가 생기기 전까지 실행됐던 쿼리들의 변경사항은 디스크에 써져 있었을까요? 만약 그랬다면 ROLLBACK 과정에서 디스크에 썼던 모든 변경사항을 전부 지워줘야합니다.
  - 그렇다면 이런 과정속에서 최적화는 어떻게 진행해야할까요?
  - 우리의 USE CASE에 따라 데이터베이스를 최적화할 수 있습니다. 우리가 잘 알고있는 MySQL, PostgreSQL, SQL Server, OracleDB 같은 DBMS들은 각각 특정 포인트에 포커스를 두고 최적화하게끔 만들어져 있습니다.
    - ROLLBACK 단계에 포커스를 둔 최적화
    - COMMIT 단계에 포커스를 둔 최적화
    - 예를 들어 PostgreSQL 같은경우, 트랜잭션 내 쿼리들이 실행될 때 디스크에 변경사항을 쓰기때문에 최종적으로 COMMIT할 때의 속도가 굉장히 빠릅니다. (그래서 I/O 작업이 정말 많이 일어난다는 단점이 있기도 합니다.)
  - COMMIT 단계에서 CRASH가 나는 경우 사용하는 DBMS가 COMMIT이 빠른 경우라면 SQL Server처럼 COMMIT이 느린 경우보다 덜 위험할 수 있습니다.
  - 트랜잭션 후 실제로 COMMIT이 되고 안되고는 굉장히 중요한 문제입니다.

## Nature of Transactions

- 트랜잭션은 보통 데이터를 생성하고 변경할 때 사용합니다.
- 하지만 트랙잭션을 read-only 용도로 사용할 수도 있습니다.
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
