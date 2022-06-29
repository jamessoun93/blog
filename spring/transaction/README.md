# (작성중) Spring Transaction (Isolation Level & Propagation Level)

## Isolation Levels

일반적인 ACID의 Isolation과 같은 개념.

`@Transactional` 애노테이션을 통해 특정 메서드가 지정한 격리 수준으로 트랜잭션을 처리할 수 있음.

```java
@Autowired
private BankAccountDAO bankAccountDAO;

@Transactional(isolation = Isolation.READ_COMMITTED) // <--- 이렇게 사용
public void transferFund(Account fromAccount, Account toAccount, BigDecimal amount) {
    bankAccountDAO.withdraw(fromAccount, amount);
    bankAccountDAO.deposit(toAccount, amount);
}
```

위 예시에서는 transferFund 메서드가 READ COMMITTED의 격리 수준으로 트랜잭션을 처리하게 됌.

## Spring Isolation Levels

1. ISOLATION_READ_UNCOMMITTED

- 동시에 실행중인 다른 트랜잭션이 아직 commit 하지 않은 상태로 변경한 데이터까지 다 읽을 수 있음.
- Dirty Read, Non-repeatable Read, Phantom Read에 취약함.

2. ISOLATION_READ_COMMITTED

- 동시에 실행중인 다른 트랜잭션이 commit한 변경사항은 다 읽을 수 있음.
- Non-repeatable Read, Phantom Read에 취약함.

3. ISOLATION_REPEATABLE_READ

- 트랜잭션 중 특정 row를 읽어 들이는 쿼리를 실행한다면 해당 트랜잭션이 끝날 때까지 읽어 들인 row의 데이터는 변하지 않음.

4. ISOLATION_SERIALIZABLE

- 가장 엄격한 Isolation Level.
- 트랜잭션이 진행되는 동안 특정 테이블을 읽으면 동시에 진행되는 다른 트랜잭션은 해당 테이블에 데이터를 추가, 변경, 삭제할 수 없음.
- 모든 Read Phenomena를 해결할 수 있는 방법임.
- 가장 성능이 안좋음.

더 자세한 내용은 링크 참고: [ACID: Isolation](../../database/acid/isolation)

---

## Propagation Levels

1. REQUIRED

- 메서드 실행 중 이미 트랜잭션이 실행중이면 같은 트랜잭션을 사용하고, 실행중인 트랜잭션이 없다면 생성해서 사용.
- 현재 메서드 내 다른 메서드가 네스팅되어 호출될 경우 각각의 메서드가 별도의 트랜잭션을 가지고 있는 것 처럼 작동하지만, 실제로는 하나의 물리적은 트랜잭션을 사용함.
- 내부 메서드에 의해 ROLLBACK이 발생한다면, 위의 트랜잭션도 ROLLBACK 함.

2. REQUIRES_NEW

- 메서드 실행 중 이미 트랜잭션이 실행중이더라도 새로운 트랜잭션을 생성해서 사용.
- 각각 다른 물리적인 트랜잭션을 사용.
- 네스팅 된 메서드 호출 구조에서도 independent 하게 작동함.
- 자식 메서드에서 ROLLBACK 해도 부모 트랜잭션이 영향을 받지 않는다는 뜻.
- 자식 메서드의 트랜잭션이 실행을 시작하면 부모 트랜잭션은 잠시 실행을 멈췄다가 자식 트랜잭션이 실행을 종료하면 이어서 실행함.

3. NESTED

- 물리적으로 같은 트랜잭션을 사용하지만, 각각 savepoint를 지정하여 롤백 시 각자의 정해진 savepoint로 롤백할 수 있음.
- JDBC savepoint 같은 것.
- Spring JDBC가 관리하는 트랜잭션에서만 사용해야함. (?)

4. MANDATORY

- 실행중인 트랜잭션이 꼭 있어야 함.
- 없다면 스프링 컨테이너가 exception을 던짐.

5. NEVER

- 실행중인 트랜잭션이 있으면 안됨.

6. NOT_SUPPORTED

-

7. SUPPORTS

-
