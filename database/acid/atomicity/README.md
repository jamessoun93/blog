# Atomicity

One of the four ACID properties that defines a relational database management system.
but really it defines any database system, whether it’s a sequel, graph, time based.

## Atomicity

- all the queries in a transaction must succeed.
    - a transaction is considered to be one unit of work and that kind of defines what atomicity really is. It’s like an atom and atom cannot be split (maybe wrong - nuclear fission). you treat this as an atom that cannot be split.
- If one query fails for whatever reasons (for ex, BALANCE goes to -1 or negative, duplicate primary key entry, invalid sql syntax), all prior successful queries in the transaction should rollback.
- If the database goes down (db crashes NOT a failed query) prior to a commit of a transaction, all the successful queries in the transactions should rollback.
    - when the database is back up, it should detect the failure mid-transaction and should rollback.
    - Think about this, what is the database really doing in a transaction. Is it actually writing to disk? What if I don’t commit, do we go back to disk and **remove** everything? This is what really makes or breaks database. So it goes back to the very famous question: ***What database should I choose?***
    - You need to understand how these databases work and based on your use case, make a choice.
    - There are databases that are writing to disk before commits cuz they assume you’re gonna commit. So when you actually in fact commit, it doesn’t have to do anything so its really fast. or just mark it as committed using one bit or so.
    - Other databases don’t write anything. so the queries are executed very fast but the ones you want to commit, they have to go take everything that is in memory and flush to disk so the commit will be slow. But the rollback will be very fast.
    - It all depends. There is no right or wrong. There’s always a tradeoff.

## Example

| account_id | balance |
|------------|---------|
| 1          | $1000   |
| 2          | $500    |

Account 1에서 Account 2로 $100를 송금하는 상황

```sql
BEGIN TX1

SELECT BALANCE FROM ACCOUNT WHERE ID = 1
BALANCE > 100 (constraint: balance can't go below 0, if it does it means its inconsistent data)

UPDATE ACCOUNT SET BALANCE = BALANCE - 100 WHERE ID = 1
```
이 순간 문제가 발생하여 CRASH가 나서 데이터베이스가 종료되었다고 가정해봅시다.

| account_id | balance |
|------------|---------|
| 1          | $900    |
| 2          | $500    |

그렇다면 Account 1의 balance는 기존 $1000에서 $100를 차감한 $900로 남아있게 되고, Account 2의 balance는 $500로 변화가 없는 상태가 됩니다.

이런식으로 ROLLBACK이 되지 않은 상태로 남는다면 Atomicity(원자성)을 보장하지 않게되어 저장된 데이터가 Inconsistent(불일치)하게 되는 현상이 생기게 됩니다.

이런 경우 데이터베이스가 다시 시작됐을때 꼭 ROLLBACK을 할 수 있어야 합니다.


## Summary

- 트랜잭션은 아토믹 해야하며 하나 이상의 쿼리가 실패할 경우 모든 쿼리를 롤백해야 합니다.
- 100개의 쿼리 중 하나라도 실패한다면 100개의 쿼리 모두 롤백되어야 합니다.