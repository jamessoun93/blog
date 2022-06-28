# JPA: Proxy

`em.find()` 메서드 말고 `em.getReference()` 라는 메서드가 있음.

- `em.find()`: 데이터베이스를 통해서 실제 엔티티 객체 조회
- `em.getReference()`: 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회

```java
try {
    Member member = new Member();
    member.setUsername("Hello");

    em.persist(member);

    em.flush();
    em.clear();

    Member foundMember = em.getReference(Member.class, member.getId());
    System.out.println("foundMember = " + foundMember.getClass());

    System.out.println("foundMember.id = " + foundMember.getId());
    System.out.println("foundMember.username = " + foundMember.getUsername());

    tx.commit();
}
```

위와 같이 find 대신 getReference로 멤버를 조회하면 HibernateProxy 객체가 반한됨.

HibernateProxy는 실제 엔티티를 상속받는 가짜 엔티티 객체임.

하지만 실제 엔티티가 만들어진 적이 없으니 타겟이 비어있음.

이럴 경우 getUsername() 이 호출되었을 때는 DB 호출이 일어나야함.

## HibernateProxy 동작 메커니즘 (위 예시 기준)

1. `getName()` 호출
2. 프록시 객체에 target이 없기 때문에 영속성 컨텍스트를 통해 DB에 있는 데이터를 요청함. (초기화 요청)
3. 영속성 컨텍스트는 DB에서 리턴받은 값으로 실제 엔티티 객체를 생성함.
4. 프록시 객체가 새로 만들어진 실제 엔티티를 타겟으로 지정.
5. 이후부터는 프록시를 통해 해당 엔티티에 있는 값을 가지고 오게 됨.

## 프록시의 특징

- 프록시 객체는 처음 사용할 떄 한번만 초기화 됨.
- 프록시 객체는 원본 엔티티를 상속받은 객체이기 때문에 타입 비교 시 `==` 말고 `instanceof`를 사용해야 함.
- 영속성 컨텍스트 내 찾는 엔티티가 이미 존재한다면 `em.getReference()`를 호출해도 실제 엔티티를 반환함.

  - 한 영속성 컨텍스트 내에서는 같은 객체에 대한 `==` 비교가 `true`를 보장함.

    ```java
    try {
        Member member = new Member();
        member.setUsername("Hello");

        em.persist(member);

        em.flush();
        em.clear();

        Member m1 = em.find(Member.class, member.getId());
        System.out.println("m1 = " + m1.getClass());

        Member ref = em.getReference(Member.class, member.getId());
        System.out.println("ref = " + ref.getClass());

        System.out.println("m1 == ref: " + (m1 == ref)); // true

        tx.commit();
    }
    ```

  - `em.find()`가 이미 영속성 컨텍스트에 Member 객체를 등록해두어서 이후 `em.getReference()`를 호출해도 프록시로 가져오지 않게 됨.
  - 만약 반대로 한다면?

    ```java
    try {
        Member member = new Member();
        member.setUsername("Hello");

        em.persist(member);

        em.flush();
        em.clear();

        Member refMember = em.getReference(Member.class, member.getId());
        System.out.println("refMember = " + refMember.getClass());

        Member findMember = em.find(Member.class, member.getId());
        System.out.println("findMember = " + findMember.getClass());

        System.out.println("refMember == findMember: " + (refMember == findMember)); // true

        tx.commit();
    }
    ```

  - `em.getReference()`는 프록시로 가져오고, `em.find()`는 DB 호출까지 하고 나서 `==`을 보장해주기 위해 프록시 객체를 리턴하게 됨.
  - 여기서 중요한 점은 개발할 때 프록시던, 실제 엔티티 객체던 개발에 문제가 없게 개발하는 것이 중요함.

- 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, `org.hibernate.LazyInitializationException` 발생함. (실무에서 자주 발생함)

## 프록시 확인 방법

위처럼 영속성 컨텍스트의 도움을 받을 수 없는 경우를 대비해서 exception이 발생하기 전에 미리 확인할 수 있는 방법들도 있음.

1. 프록시 인스턴스의 초기화 여부 확인

```java
try {
    Member member = new Member();
    member.setUsername("Hello");

    em.persist(member);

    em.flush();
    em.clear();

    Member refMember = em.getReference(Member.class, member.getId());
    System.out.println("refMember = " + refMember.getClass());

    // 프록시 인스턴스의 초기화 여부 확인
    System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));

    tx.commit();
}
```

2. 프록시 클래스 확인 방법

```java
try {
    Member member = new Member();
    member.setUsername("Hello");

    em.persist(member);

    em.flush();
    em.clear();

    Member refMember = em.getReference(Member.class, member.getId());

    // 프록시 클래스 확인
    System.out.println("refMember = " + refMember.getClass());

    tx.commit();
}
```

3. 프록시 강제 초기화

```java
try {
    Member member = new Member();
    member.setUsername("Hello");

    em.persist(member);

    em.flush();
    em.clear();

    Member refMember = em.getReference(Member.class, member.getId());

    // 강제 초기화
    refMember.getUsername();

    tx.commit();
}
```

프록시 객체에는 username 값이 없기 떄문에 영속성 컨텍스트를 통해 DB 호출을 해서 가져와야함.

그 과정에서 초기화가 일어남.

Hibernate를 사용한 초기화 방법은 아래.

```java
try {
    Member member = new Member();
    member.setUsername("Hello");

    em.persist(member);

    em.flush();
    em.clear();

    Member refMember = em.getReference(Member.class, member.getId());

    // Hibernate를 사용한 강제 초기화
    Hibernate.initialize(refMember);

    tx.commit();
}
```
