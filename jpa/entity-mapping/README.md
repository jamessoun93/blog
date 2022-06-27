# JPA: 엔티티 매핑 (Entity Mapping)

## 객체와 테이블 매핑 (@Entity, @Table)

### @Entity

- @Entity가 붙은 클래스는 JPA가 관리하는 엔티티임.
- JPA를 사용해서 테이블과 매핑할 클래는 @Entity 필수
- 주의할 점
  - 기본 생성자 필수 (파라미터가 없는 public 또는 protected 생성자)
  - final 클래스, enum, interface, inner 클래스는 사용 불가
  - DB에 저장할 필드에 final 사용 불가
- name 속성
  - JPA에서 사용할 엔티티의 이름을 지정
  - 기본값을 클래명 그대로이고 이름 충돌이 없으면 그냥 기본값 그대로 사용

### @Table

- 엔티티와 매핑할 테이블 지정

## 필드와 컬럼 매핑

- @Column: 컬럼 매핑
  - 속성
    - name: 해당 필드와 매핑할 컬럼 이름
      ```java
      @Column(name = "name")
      private String username;
      ```
    - insertable, updatable: 등록, 변경 가능 여부 (기본값 true)
      ```java
      @Column(name = "name", updatable = false)
      private String username;
      ```
      위와 같이 해놓으면 해당 컬럼 값은 변경되지 않음 (DB에서 강제로 하는게 아니라면)
    - nullable: null 값의 허용 여부
      ```java
      @Column(name = "name", nullable = false)
      private String username;
      ```
    - unique: 특정 걸럼에 unique 제약조건을 걸 때 사
      ```java
      @Column(name = "name", unique = true)
      private String username;
      ```
      하지만 unique constraint의 이름을 랜덤으로 만들어줘서 사용하지 않고, `@Table(uniqueConstraints = ...)`를 사용함.
    - columnDefinition: DB 컬럼 정보를 직접 줄 수 있음.
    - length: 문자 길이 제약조건, String 타입에만 사용.
    - precision, scale: 아주 큰 숫자에서 소숫점 자리수 지정할 때 사용, 정밀한 소수를 다룰 때 사용
      ```java
      @Column(precision = 19, scale = ...)
      private BigDecimal price;
      ```
- @Enumerated: enum 타입 매핑할 때 사용
  - EnumType.ORDINAL: enum 순서를 그대로 DB에 저장 (기본값)
  - EnumType.STRING: enum 이름을 DB에 저장
  - EnumType.ORDINAL 사용하면 안됨. 꼭 STRING을 사용할 것.
  - enum 클래스에 가장 앞부분에 새로운 타입을 추가할 경우 순서 그대로 0번이 되어 저장되어 진다. (해결할 수 없는 버그가 된다는 뜻)
- @Temporal: 날짜/시간(java.util.Date, java.util.Calendar) 관련 필드 매핑할 때 사용
  - 사실 이건 이제 안사용해도 됨.
  - 자바8 부터 제공되는 `LocalDate`와 `LocalDateTime` 사용하면 생략가능
- @Lob: VARCHAR를 넘어가는 길이의 데이터를 저장할때 (clob, blob)
  - Lob 해놓고 String 타입으로 선언하면 기본적으로 clob으로 생성됨.
  - 나머지는 blob으로 매핑됨.
- @Transient: 매핑을 안하고 싶은 컬럼 정의할 때 사용
  - DB 저장 X, 조회 X
  - 주로 메모리상에서만 임시로 값을 보관하고 싶은 경우 사용

```java
@Entity
public class Member {

    @Id
    private Long id;

    @Column(name = "name")
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;
}
```

위처럼 엔티티를 정의하면 아래와 같이 만들어짐.

```sql
create table Member (
       id bigint not null,
        age integer,
        createdDate timestamp,
        description clob,
        lastModifiedDate timestamp,
        roleType varchar(255),
        name varchar(255),
        primary key (id)
    )
```

## 기본 키 매핑 (@Id)

기본 키 매핑 어노테이션

- @Id
- @GeneratedValue

- 직접 할당: @Id만 사용
- 자동 생성: @GeneratedValue

  - AUTO (default): 사용하는 DB 방언에 맞춰서 자동으로 선택
  - IDENTITY: 기본 키 생성을 DB에 위임

    - MySQL 같은 경우 AUTO_INCREMENT
    - 문제: DB에 들어가봐야 id값을 알 수 있음.
      - 영속성 컨텍스트에서 관리하려면 무조건 pk가 존재 해야하는데 없음.
      - 그래서 IDENTITY 전략에서만 특이하게 persist 시점에 바로 insert 쿼리를 날려버림.
      - 그렇게 가져온 id값을 영속성 컨텍스트에서 pk값으로 사용하게 됨.
      - 모아서 insert하는게 불가능하다는 단점으로 작용할 수 있음.

  - SEQUENCE: 유일한 값을 순서대로 생성하는 특별한 DB 오브젝트 (에: 오라클 시퀀스)
    - 문제: IDENTITY와 마찬가지로 DB에 들어가봐야 id값을 알 수 있음.
    - 위와 동일한 문제가 있음.
    - 그래서 allocationSize라는 속성을 통해 미리 50개를 올려놓는식으로 성능 개선 효과를 볼 수 있음.
    - 데이터베이스에 미리 50개 올려놓고 메모리에서 50개 다 쓰면 next value 부르는 방식.
  - TABLE: 키 생성 전용 테이블을 만들어서 키를 뽑아 사용하는 방식 (잘 사용하지 않음)
    - 장점: 모든 DB에서 사용가능
    - 단점: 성능 (테이블 lock도 걸릴수도 있고 숫자 뽑는데 최적화도 안되어 있기 때문에)
    - @TableGenerator 사용

권장하는 식별자 전략

- 기본 키 제약 조건: null 안됨, 유일해야함, 변하면 안됨.
- 먼 미래까지 이런 조건들에 만족하는 자연키를 찾기가 어려움. (예를 들면 주민등록번호)
- 그래서 비즈니스와 전혀 상관없는 대리키를 사용하는걸 권장함.
- 권장: Long 타입 + 대체키(시퀀스 혹은 UUID) + 키 생성전략 사용
- 절대 비즈니스를 키로 끌고 오지 않는게 좋음.
