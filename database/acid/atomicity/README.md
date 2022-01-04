# Atomicity

One of the four ACID properties that defines a relational database management system.
but really it defines any database system, whether it’s a sequel, graph, time based.

## Atomicity

- all the queries in a transaction must succeed.
    - a transaction is considered to be one unit of work and that kind of defines what atomicity really is. It’s like an atom and atom cannot be split (maybe wrong - nuclear fission). you treat this as an atom that cannot be split.
- If one query fails for whatever reasons (for ex, BALANCE goes to -1 or negative, duplicate primary key entry, invalid sql syntax), all prior successful queries in the transaction should rollback.
- If the database goes down (db crashes NOT a failed query) prior to a commit of a transaction, all the successful queries in the transactions should rollback.
    - when the database is back up, it should detect the failure mid-transaction and should rollback.
    - Think about this, what is the database really doing in a transaction. Is it actually writing to disk? What if I don’t commit, do we go back to disk and remove everything? This is what really makes or breaks database. So it goes back to the very famous question: ***What database should I choose?***
    - You need to understand how these databases work and based on your use case, make a choice.
    - There are databases that are writing to disk before commits cuz they assume you’re gonna commit. So when you actually in fact commit, it doesn’t have to do anything so its really fast. or just mark it as committed using one bit or so.
    - Other databases don’t write anything. so the queries are executed very fast but the ones you want to commit, they have to go take everything that is in memory and flush to disk so the commit will be slow. But the rollback will be very fast.
    - It all depends. There is no right or wrong. There’s always a tradeoff.