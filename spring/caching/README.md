# Spring Boot Cache

## Cache Annotations

1. enable caching
2. store data
3. update data
4. evict data

### @EnableCaching

```java
@Configuration
@EnableCaching
public class AppConfig {
    ...
}
```

### @Cacheable

- Used with methods that are cacheable.

- parameter로 받은 isbn을 key로 사용

```java
@Cacheable(cacheNames="books", key="#isbn")
public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed) {...}
```

- parameter로 받은 isbn 객체의 프로퍼티를 key로 사용

```java
@Cacheable(cacheNames="books", key="#isbn.rawNumber")
public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed) {...}
```

- 조건도 추가 가능: 책 이름의 길이가 32보다 작은 책 정보만 캐싱

```java
@Cacheable(cacheNames="books", key="#name.length() < 32")
public Book findBook(String name) {...}
```

- Synchronized Caching: 여러 메서드에서 같은 캐시를 사용할 때

```java
@Cacheable(cacheNames="foos", sync=true)
public Foo executeExpensiveOperation(String id) {...}
```

### @CachePut

- updates the cache

```java
@CachePut(cacheNames="books", key="#isbn")
public Book updateBook(ISBN isbn, BookDescriptor descriptor) {...}
```

### @CacheEvict

- Clears cache values from the cache storage

```java
@CacheEvict(cacheNames="books", allEntries=true)
public void loadBooks(InputStream batch) {...}
```

### @Caching

- 하나의 메서드에 같은 타입의 캐시 애노테이션 여러개 사용할 때 (CacheEvict, CachePut)

```java
@Caching(evict = { @CacheEvict("primary"), @CacheEvict(cacheNames="secondary", key="#p0") })
public Book importBooks(String deposit, Date date) {...}
```

### @CacheConfig

- class level: 해당 클래스의 모든 메서드에 적용

```java
@CacheConfig("books")
public class BookRepositoryImpl implements BookRepository {

    @Cacheable
    public Book findBook(ISBN isbn) {...}
}
```

---

## Different Cache Providers

- JDK ConcurrentMap-based Cache
- Ehcache-based Cache
- Caffeine Cache

---

## TTL/Eviction policy

- cache provider를 통해 세팅할 수 있다.
- Spring Boot는 abstraction(interface의 형태로)을 제공하고 실제 implementation은 프로바이더를 통한다.

---

@Cacheable 안에 @Cacheable

DB CRUD 다 만들고 나중에 캐시

cache 서비스 따로 만들어서 cache 통제만 모아놓는 곳

ttl 지나도 레디스에 남아있는데 hit 때리면 사라지게 되는데 flush 용도로 주기적으로 호출해줌 (로그 활용)

1분에 한번씩 디스크에 씀 (그래서 1분전 데이터가 들어가 있는 형태)

key serialize 전략
value serialize 전략 -> 객체 잘 JSON으로 들어가는지 확인

redis에다가 딱 한번만 lua script 넣도록

---
