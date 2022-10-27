# [JPA #4] 엔티티 매핑 (Entity Mapping)

[Tistory 블로그 포스팅 바로가기](https://seunghyunson.tistory.com/32)

이번 포스팅에서는 JPA가 엔티티 객체와 테이블을 어떻게 매핑하는지 알아보겠습니다.

---

## 객체와 테이블 매핑 (@Entity, @Table)

### @Entity

`@Entity` 라는 애노테이션이 붙은 클래스는 JPA가 관리하는 엔티티를 뜻합니다.

JPA를 사용해서 테이블과 매핑할 클래스는 필수로 붙여줘야 합니다.

엔티티를 선언할 때는 몇 가지 주의사항이 있는데요.

- 기본 생성자가 필수입니다. (기본 생성자란, 파라미터가 없는 public 또는 protected 생성자를 뜻합니다.)
- final 클래스, enum, interface, inner 클래스는 사용이 불가합니다.
- DB에 저장할 필드에 final 키워드를 사용할 수 없습니다.

### @Table

엔티티와 매핑할 테이블을 지정한다는 애노테이션이며, `name` 이라는 속성을 통해 원하는 테이블명을 정할 수 있습니다.

---

## 필드와 컬럼 매핑

### @Column

`@Column` 은 컬럼 매핑을 위해 사용되며 함께 사용 가능한 속성들은 아래와 같습니다.

- `name`: 해당 필드와 매핑할 컬럼 이름
  ```java
  @Column(name = "name")
  private String username;
  ```
- `insertable`, `updatable`: 등록, 변경 가능 여부 (기본값 true)
  ```java
  @Column(name = "name", updatable = false)
  private String username;
  ```
  위와 같이 설정하면 해당 컬럼 값은 DB에서 강제로 하는게 아니라면 변경되지 않습니다.
- `nullable`: `NULL` 값의 허용 여부
  ```java
  @Column(name = "name", nullable = false)
  private String username;
  ```
- `unique`: 특정 걸럼에 UNIQUE 제약조건을 걸 때 사용
  ```java
  @Column(name = "name", unique = true)
  private String username;
  ```
  하지만 이렇게만 두면 unique constraint의 이름을 랜덤으로 생성해주기 때문에 운영환경에서 사용하기에는 적합하지 않습니다. 그래서 `@Table(uniqueConstraints = ...)`의 형태로 사용하는 것을 권장합니다.
- `columnDefinition`: DB 컬럼 정의를 직접할 때 사용
- `length`: 문자 길이 제약조건, String 타입에만 사용
- `precision`, `scale`: 아주 큰 숫자에서 소숫점 자리수 지정할 때 사용, 정밀한 소수를 다룰 때 사용
  ```java
  @Column(precision = 19, scale = ...)
  private BigDecimal price;
  ```

### @Enumerated

Enum 타입 매핑을 위해 사용합니다.

- `EnumType.ORDINAL`: enum 순서를 그대로 DB에 저장 (기본값)
- `EnumType.STRING`: enum 이름을 DB에 저장

이렇게 두 가지 옵션이 제공되긴 하지만, `EnumType.ORDINAL` 은 enum 클래스에 가장 앞부분에 새로운 타입을 추가할 경우 순서 그대로 `0`번이 되어 저장되기 때문에 정말 해결하기 어려운 버그를 발생시킬 수 있기 때문에 사용하면 안됩니다.

그래서 꼭 `EnumType.STRING` 을 사용해야 합니다.

### @Temporal

날짜/시간(`java.util.Date`, `java.util.Calendar`) 관련 필드 매핑할 때 사용되지만, 자바8 이상의 버전부터는 `LocalDate`와 `LocalDateTime` 를 사용하면 생략이 가능합니다.

### @Lob

`VARCHAR`를 넘어가는 길이의 데이터를 저장할 때 사용합니다. (clob, blob)

지정할 수 있는 속성은 따로 없으며, 매핑하는 필드 타입이 String 이라면 clob으로, 나머지는 blob 으로 매핑됩니다.

### @Transient

매핑을 안하고 싶은 컬럼 정의할 때 사용합니다.

DB에도 저장되지 않으며 조회도 불가능하게 됩니다.

주로 메모리상에서만 임시로 값을 보관하고 싶은 경우 사용합니다.

---

## 기본 키 매핑 (@Id)

기본 키 매핑을 위한 애노테이션으로 아래와 같이 두 가지를 사용할 수 있습니다.

- `@Id`
- `@GeneratedValue`

`id` 를 직접 할당하고 싶은 경우 `@Id` 만 사용하면 됩니다.

```java
@Id
private Long id;
```

`id` 를 자동 생성되게 하고 싶다면 `@GeneratedValue` 를 사용합니다.

```java
@Id
@GeneratedValue
private Long id;
```

### @GeneratedValue

`@GeneratedValue` 는 사용하는 데이터베이스 방언에 따라 조금씩 차이가 있기 때문에 전략(strategy)가 존재합니다.

#### AUTO

사용하는 DB 방언에 맞춰서 `IDENTITY`, `SEQUENCE`, `TABLE` 중 한 가지가 자동으로 선택되는 전략입니다.

```java
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
```

#### IDENTITY

IDENTITY 전략은 기본 키 생성을 DB에 위임합니다.

가장 대표적인 예로 MySQL 의 `AUTO_INCREMENT` 를 들 수 있습니다.

단점이 있다면, DB에 들어가봐야 id값을 알 수 있다는 점입니다.

애플리케이션에서 엔티티 객체에 id를 할당하지 않고 DB에서 하기 때문에, 영속성 컨텍스트에 등록 전 persist 시점에 insert 쿼리가 날라간다는 특이점이 있습니다. (영속성 컨텍스트에서 관리하려면 무조건 pk가 존재 해야하기 때문에)

따라서 영속성 컨텍스트의 쓰기 지연 저장소의 장점을 이용하지 못한다는 단점이 있습니다.

#### SEQUENCE

오라클의 시퀀스처럼 DB에 있는 시퀀스 오브젝트를 통해서 값을 생성하는 방식입니다.

시퀀스 오브젝트란, 유일한 값을 순서대로 생성하는 특별한 DB 오브젝트를 뜻합니다.

단점이 있다면, IDENTITY와 마찬가지로 DB에 들어가봐야 id값을 알 수 있다는 점입니다.

그래서 `allocationSize` 라는 속성을 통해 미리 50개를 올려놓는 방식으로 성능 개선 효과를 볼 수 있습니다. (데이터베이스에 미리 50개 올려놓고 메모리에서 50개 다 쓰면 next value 부르는 방식)

#### TABLE

키 생성 전용 테이블을 만들어서 키를 뽑아 사용하는 방식입니다. (잘 사용하지 않는 방법입니다.)

모든 DB에서 사용 가능하다는 장점이 있지만, 테이블 lock도 걸릴 수 있고 숫자 뽑는데 최적화도 안되어 있기 때문에 성능 이슈가 존재합니다.

---

## 권장하는 식별자 전략

PK는 기본적으로 null 허용이 안되고, 유일하면서 변하면 안됩니다.

그리고 비즈니스와 전혀 상관없는 대리키를 사용해야 먼 미래까지 변할 일이 없게끔 만들 수 있습니다.

결국 Long 타입의 AUTO_INCREMENT나 SEQUENCE 전략을 사용하거나, UUID 혹은 사내에서 정한 규칙에 맞는 랜덤값을 활용하는 방법을 권장합니다.

---

지금까지 JPA에서 엔티티 매핑을 어떻게 하는지와 각각 제공되는 속성들을 알아본 뒤, 마지막에는 권장 PK 설정 전략까지 알아봤습니다.
