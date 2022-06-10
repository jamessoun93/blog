# Java 8에 추가된 것들

Java 기본 제공 함수형 인터페이스

1. Function

```java
public class Main {
    public static void main(String[] args) {
        Function<Integer, Integer> plus10 = (i) -> i + 10;

        System.out.println(plus10.apply(1));
    }
}
```

compose()

```java
public class Main {
    public static void main(String[] args) {
        Function<Integer, Integer> plus10 = (i) -> i + 10;
        Function<Integer, Integer> multiply2 = (i) -> i * 2;

        Function<Integer, Integer> multiply2AndPlus10 = plus10.compose(multiply2);

        System.out.println(multiply2AndPlus10.apply(2));
    }
}
```

multiply2 먼저 부른다음 plus 10

andThen()

```java
public class Main {
    public static void main(String[] args) {
        Function<Integer, Integer> plus10 = (i) -> i + 10;
        Function<Integer, Integer> multiply2 = (i) -> i * 2;

        Function<Integer, Integer> plus10AndMultiply2 = plus10.andThen(multiply2);

        System.out.println(plus10AndMultiply2.apply(2));
    }
}
```

plus 10 먼저 한 뒤 그 결과값에 multiply 2

2. Consumer

리턴이 없고 T 타입을 받기만 함

```java
public class Main {
    public static void main(String[] args) {
        Consumer<T> printT = (i) -> System.out.println(i);
        printT.accept(10);
    }
}
```

3. Supplier

아무것도 받지 않고 T 타입의 값을 리턴하는 인터페이스

```java
public class Main {
    public static void main(String[] args) {
        Supplier<T> get10 = () -> 10;
        System.out.println(get10.get());
    }
}
```

4. Predicate

T 타입을 받아서 boolean 을 리턴하는 함수 인터페이스

And, Or, Negate를 사용하여 조합 가능

```java
public class Main {
    public static void main(String[] args) {
        Predicate<String> startsWithJ = (s) -> s.startsWith("j");
        Predicate<Integer> isEven = (i) -> i % 2 == 0;
    }
}
```

5. UnaryOperator

Function 인터페이스랑 같은데 입력값과 리턴타입이 동일할 때 (입력값이 하나일 경우)

```java
public class Main {
    public static void main(String[] args) {
        // Function<Integer, Integer> plus10 = (i) -> i + 10;
        UnaryOperator<Integer> plus10 = (i) -> i + 10;

        System.out.println(plus10.apply(1));
    }
}
```

외 BiFunction과 BinaryOperator도 있지만 사용 방법 비슷하고 매개변수 개수만 다름

---

# Lambda Expressions

- 익명 클래스를 사용하면 가독성도 떨어지고 불편한데, 이러한 단점을 보완하기 위해서 람다 표현식이 만들어졌다
- 대신, 이 표현식은 인터 페이스에 메소드가 “하나”인 것들만 적용 가능하다.
- Java에 있는 인터페이스 중, 메소드가 하나인 인터페이스는 이런 애들이 있음
  - java.lang.Runnable
  - java.util.Comparator
  - java.io.FileFilter
  - java.util.concurrent.Callable
  - java.security.PrivilegedAction
  - java.nio.file.PathMatcher
  - java.lang.reflect.InvocationHandler

예시

```java
interface Calculate {
    int operation(int a,int b);
}
```

이렇게 Calculate라는 인터페이스가 있고, operation() 메소드가 선언되어 있다. 대신 a와 b 로 무슨 작업을 하는지는 선언되어 있지 않다

```java
Calculate calculateAdd = new Calculate() {
    @Override
    public int operation(int a, int b) {
        return a+b;
    };
}
System.out.printin(calculateAdd.operation(1,2));
```

이를 람다 표현식으로 처리하면

```java
Calculate calculateAdd = (a,b) -> a + b;
System.out.printin(calculateAdd.operation(1,2));
```

- 일반적인 인터페이스이지만, 이 인터페이스는 Functional(기능적) 인터페이스라고 부를 수 있다.
- 기능적 인터페이스는 이와 같이 하나의 메소드만 선언되어 있는 것을 의미한다.
- 그런데, 이렇게만 선언해 두면 매우 혼동될 수도 있다. 왜냐하면, 같이 개발하는 다른 친구가 이 인터페이스 선언이 모호하다며 operationAdd()와 operationSubstract() 메소드로 구분하여 두 개의 메소드를 선언할 수도 있기 때문이다.

```java
interface Calculate {
    int operationAdd(int a,int b);
    int operationSubstract(int a,int b);
}
```

메소드가 두개라 람다 표현식 컴파일 오류가 발생함

이러한 혼동을 피하기 위하여, 인터페이스 선언시 어노테이션을 사용할 수 있다.

```java
@FunctionalInterface
interface Calculate {
    int operation(int a, int b);
}
```

명시적으로 이렇게 @FunctionalInterface를 사용하면 이 인터페이스에는 내용이 없는 “하나” 의 메소드만 선언할 수 있다.

만약 두 개의 인터페이스를 선언한다면 다음과 같은 컴파일 오류가 발생한다.

정리

- 메소드가 하나만 존재하는 인터페이스는 @FunctionalInterace로 선언할 수 있으며, 이 인터페이스를 람다 표현식으로 처리할 수 있다.
- (매개 변수 목록) -〉 처리식으로 람다를 표현하며, 처리식이 한 줄 이상일 때에는 처리식을 중괄호로 묶을 수 있다.

# 추가

- refer하는 변수는 effectively final 해야함
- final 하다고 가정하고 써야함

# Inner Class, Anonymous Class, Lambda Expression 공통점 및 차이점

shadowing 여부

- 이너 클래스와 anonymous class는 변수명이 같아도 자기 스코프 안에 있는 변수를 씀.
- lambda는 lambda를 가지고 있는 함수의 스코프를 함께 사용함.

---

# Method Reference

기존에 이미 있는 메소드를 참조

이미 만들어둔 메소드를 functional 인터페이스의 구현체로 사용하는 것

```java
public class Greeting {
    private String name;

    public Greeting() {

    }

    public Greeting(String name) {
        this.name = name;
    }

    public String hello(String name) {
        return "hello " + name;
    }

    public static String hi(String name) {
        return "hi " + name;
    }
}
```

위와 같은 클래스가 있다고 했을때,

String을 받아서 String을 리턴해주는 functional interface를 사용하고 싶다면 아래와 같이 할 수 있다.

```java
public static void main(String[] args) {
    // static 메소드 참조
    UnaryOperator<String> hi = Greeting::hi;

    // 특정 인스턴스 메소드 참조
    Greeting greeting = new Greeting();
    UnaryOperator<String> hello = greeting::hello;
    System.out.println(hello.apply("seunghyun"));

    // 생성자 참조 (입력값 없는 것)
    Supplier<Greeting> newGreeting = Greeting::new;
    newGreeting.get();

    // 생성자 참조 (입력값 있는 것)
    Function<String, Greeting> sGreeting = Greeting::new;
    Greeting seungGreeting = sGreeting.apply("seunghyun");
    System.out.println(seungGreeting.getName());
}
```

추가로, 임의 객체의 인스턴스 메소드 참조 하는 방법도 있다.

```java
public static void main(String[] args) {
    String[] names = {"James", "Tom", "Woong"};
    Arrays.sort(names, new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return 0;
        }
    })
}
```

위의 예시로 보았을 때 Comparator는 함수형 인터페이스이기 때문에 람다식으로 변환이 가능합니다.

```java
public static void main(String[] args) {
    String[] names = {"James", "Tom", "Woong"};
    Arrays.sort(names, (o1, o2) -> 0);
}
```

그리고 이렇게 람다로 변환해줄 수 있다면 같은 일을 하는 다른 메소드를 참조할 수 있다는 뜻.

```java
public static void main(String[] args) {
    String[] names = {"James", "Tom", "Woong"};
    Arrays.sort(names, String::compareToIgnoreCase);
    System.out.println(Arrays.toString(names));
}
```

여기서 주의할 것은 String 클래스에 compareToIgnoreCase 라는 메소드가 static 하게 정의되어 있는 것이 아닌, 위의 names에 담겨있는 임의의 객체들의 인스턴스 메소드를 참조하는 것.

- static method 참조 - 타입::스태틱 메소드
- 특정 객체의 인스턴스 메소드 참조 - 객체 레퍼런스::인스턴스 메소드
- 생성자 참조 - 타입::new
- 임의 객체의 인스턴스 메소드 참조 - 타입::인스턴스 메소드

---

# 인터페이스 기본 메소드와 스태틱 메소드

```java
public interface Person {
    void printName();
    String getName();
}

public class DefaultPerson implements Person {
    String name;

    public DefaultPerson(String name) {
        this.name = name;
    }

    @Override
    public void printName() {
        System.out.println(this.name);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
```

이렇게 있을 때 인터페이스를 구현한 인스턴스들에 공통적으로 구현해줬으면 하는 기능이 나중에 생겼다고 가정해보자.

```java
public interface Person {
    void printName();
    String getName();
    void printNameUpperCase();
}
```

이렇게 하면 해당 인터페이스를 구현하고 있는 인스턴스들은 전부 컴파일 에러가 납니다.

이럴 때 default 키워드를 사용해서 default 메소드를 선언해서 사용할 수 있습니다.

```java
public interface Person {
    void printName();
    String getName();
    default void printNameUpperCase() {
        System.out.println(getName().toUpperCase());
    }
}
```

여기서 주의사항은, default 메소드로 구현한 기능이 모든 인스턴스들에 제공되는 기능인데 항상 정상적으로 작동할 것이란 보장이 없음.

예를 들면, 위 예시에서는 `getName()` 이 뭐를 리턴해줄지 모르는데 만약 null 을 리턴한다면?

그래서 이런걸 방지하려면 최소한의 문서화를 잘 해놓자.

@implSpec 활용

```java
public interface Person {
    void printName();
    String getName();

    /**
      * @implSpec
      * 이 구현체는 getName()이 반환하는 문자열을 대문자로 바꿔 출력한다.
      */
    default void printNameUpperCase() {
        System.out.println(getName().toUpperCase());
    }
}
```

그리고 만약 새로 추가된 이 default 메소드에 문제가 있다면 override해서 재정의할 수 있음.

만약 같은 이름의 default 메소드를 가진 두개 이상의 인터페이스를 구현할 경우 어떤걸 써야하는지 애매하기 때문에 컴파일 에러가 발생하고 직접 오버라이드해서 재정의 해줘야 함.

해당 타입 관련 헬퍼 메소드나 유틸리티 메소드를 제공하고 싶은 경우 static 메소드를 추가할 수 있음.

```java
public interface Person {
    void printName();
    String getName();

    /**
      * @implSpec
      * 이 구현체는 getName()이 반환하는 문자열을 대문자로 바꿔 출력한다.
      */
    default void printNameUpperCase() {
        System.out.println(getName().toUpperCase());
    }

    static void printAnything() {
        System.out.println("heeeelllooo");
    }
}
```

실제로 이런 변화들로 인해 새로운 변화들이 많이 생겼다.

Java 8에서 추가한 기본 메소드로 인해 API에는 어떤 변화들이 있었는지 알아보자.

## Iterable

- forEach

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    names.forEach((str) -> System.out.println(str));
}
```

위의 forEach 부분은 Consumer 이기 때문에 아래와 같이 써줄 수 있다.

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    // names.forEach((str) -> System.out.println(str));
    names.forEach(System.out::println);
}
```

- spliterator(): 쪼갤 수 있는 기능을 가진 Iterator
- tryAdvance는 Iterator의 hasNext와 같은 기능을 하며 Consumer를 받는다.

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    Spliterator<String> spliterator = names.spliterator();
    while (spliterator.tryAdvance(System.out::println));
}
```

여기서 쪼개는 기능을 추가하면 아래와 같다.

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    Spliterator<String> spliterator = names.spliterator();
    Spliterator<String> spliterator1 = spliterator.trySplit();

    while (spliterator.tryAdvance(System.out::println));
    System.out.println("---------");
    while (spliterator1.tryAdvance(System.out::println));
}
```

spliterator.trySplit() 을 호출하면 기존 spliterator에 절반을 담고 spliterator1에 나머지 절반을 담음

## Collection

- stream() 은 다음에 자세히
- removeIf(Predicate)

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    names.removeIf(s -> s.startsWith("j"));
    names.forEach(System.out::println);
}
```

## Comparator

- Functional Interface
- 정렬할 때 사용

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    names.sort(String::compareToIgnoreCase);
    names.forEach(System.out::println);
}
```

만약 순서를 반대로 재정렬하고 싶다면

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;
    names.sort(compareToIgnoreCase.reversed());

    names.forEach(System.out::println);
}
```

여기서 추가로 정렬 기준을 추고 싶다면 thenComparing() 같은것을 활용할 수 있다.

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;
    names.sort(compareToIgnoreCase.reversed().thenComparing(...));

    names.forEach(System.out::println);
}
```

이외에도 아래와 같은 static 메소드들이 제공된다.

- static reverseOrder() / naturalOrder()
- static nullsFirst() / nullsLast()
- static comparing()

## 변화

이런 default 메소드라는 변화가 생기면서 라이브러리 API에도 많은 변화들이 생겼다.

Java 8 이전에는,

인터페이스가 만약 추상 메소드를 3개 가지고 있다고 쳤을 때, 해당 인터페이스를 구현하는 추상 클래스를 생성해서 인터페이스에 있는 메소드를 전부 비어있는 구현체로 넣어두는 패턴을 많이 사용했다.

이렇게 하면 실제로 구현할 클래스들에서 인터페이스의 메소드를 전부 구현할 필요없이 추상 클래스를 상속해서 필요한 메소드만 구현하면 되기 때문에.

Java 8 부터는 인터페이스에서 default 메소드가 이런 불편함을 없애줌.

인터페이스를 구현하기 때문에 상속으로부터 자유로워짐. (비침투성(?))

상속하면 뭐가 문제길래? 상속은 한번밖에 안되기 때문에 다른 클래스는 상속을 못함.

실제로 Spring의 WebMvcConfigurer 라는 인터페이스가 있고 이걸 구현한 WebMvcConfigurerAdapter라는 추상 클래스가 있고 얘는 Spring 5부터 Java 8의 변화로 인해 deprecated 되었음.

만약 저 추상클래스가 없었다면 WebMvcConfigurer 인터페이스의 메소드를 전부 구현하느라 굉장히 피곤했을텐데 저렇게 해결했었음.

하지만 interface의 default method로 이럴 필요성을 없애버림.

---

# Stream

- 자바의 스트림은 "뭔가 연속된 정보"를 처리하는 데 사용한다.
- stream은 데이터를 담고 있는 저장소가 아니다
- stream은 순차적으로 데이터를 처리함.
- Functional in nature: 제공된 데이터를 변경하지 않는다.
- 스트림으로 처리하는 데이터는 오직 한번만 처리한다.
- 스트림으로 처리해야할 데이터가 무제한일 수도 있다.
  - 이럴 경우 Short Circuit 메소드를 사용해서 제한할 수 있다)
- Stream의 구조
  - list.<스트림 생성>.<중개 연산>.<종단 연산>
  - list.stream().filter(x -> x > 10).count()
  - 중개 operation 들은 lazy 하다.
    - 종단 연산을 만나기 전까지 아무것도 하지 않는다.
    - 종단 연산이 있어야만 실행된다는 뜻
  - 중개 연산은 Stream 을 리턴하고, 종단 연산은 Stream 을 리턴하지 않는다.
- 컬렉션에는 스트림을 사용할 수 있지만, 아쉽게도 배열에는 스트림을 사용할 수 없다
- 배열을 컬렉션의 List로 변환하는 방법은 여러 가지가 존재함.
  - `Arrays.asList()`

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    List<String> collect = names.stream().map((s) -> {
        return s.toUpperCase();
    }).collect(Collectors.toList());

    collect.forEach(System.out::println);

    System.out.println("--------------");

    names.forEach(System.out::println);
}
```

- parallelStream() 을 이용해서 병렬처리도 쉽게 가능함.

```java
public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.add("seunghyun");
    names.add("james");
    names.add("tom");

    List<String> collect = names.parallelStream().map(s -> {
        System.out.println(s + " " + Thread.currentThread().getName());
        return s.toUpperCase();
    }).collect(Collectors.toList());

    collect.forEach(System.out::println);
}
```

하지만 CPU를 많이 사용하고 몇개의 쓰레드로 처리되는지 보장이 되지 않아 더 늘어질 가능성도 있음.

데이터가 정말 많은 경우 유용할 수 있고, 실제로 테스트 해보면서 적용하는게 좋음.

# Stream API 사용 예시

아래와 같은 OnlineClass 클래스가 있다고 가정합니다.

```java
public class OnlineClass {

    private Integer id;

    private String title;

    private boolean closed;

    public OnlineClass(Integer id, String title, boolean closed) {
        this.id = id;
        this.title = title;
        this.closed = closed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
```

그릭고 main 메소드에서는 아래와 같이 OnlineClass ArrayList를 생성합니다.

```java
List<OnlineClass> springClasses = new ArrayList<>();
springClasses.add(new OnlineClass(1, "spring boot", true));
springClasses.add(new OnlineClass(2, "spring data jpa", true));
springClasses.add(new OnlineClass(3, "spring mvc", false));
springClasses.add(new OnlineClass(4, "spring core", false));
springClasses.add(new OnlineClass(5, "rest api development", false));
```

Stream API를 사용하여 아래 작업들을 수행하는 코드를 작성해보겠습니다.

1. title이 "spring"으로 시작하는 수업만 출력
2. close 되지 않은 수업들만 출력
3. 수업 이름만 모아서 스트림 생성하고 출력

### 1. title이 "spring"으로 시작하는 수업만 출력

```java
springClasses.stream()
        .filter(oc -> oc.getTitle().startsWith("spring"))
        .forEach(oc -> System.out.println(oc.getId()));
```

각 중개연산과 종단연산에 어떤 타입이 파라미터로 제공되는지 잘 파악 해야합니다.

### 2. close 되지 않은 수업들만 출력

```java
springClasses.stream()
        .filter(oc -> !oc.isClosed)
        .forEach(oc -> System.out.println(oc.getId()));
```

중간에 `filter(oc -> !oc.isClosed)` 부분은 메소드 레퍼런스를 사용하여 조금 더 힙하게(개인적인 생각) 만들어줄 수 있습니다.

```java
filter(oc -> Predicate.not(OnlineClasses::isClosed));
```

### 3. 수업 이름만 모아서 스트림 생성하고 출력

```java
springClasses.stream()
        .map(OnlineClass::getTitle)
        .forEach(System.out::println);
```

<-- 정리 필요 -->

```java
List<OnlineClass> javaClasses = new ArrayList<>();
javaClasses.add(new OnlineClass(6, "The Java, Test", true));
javaClasses.add(new OnlineClass(7, "The Java, Code manipulation", true));
javaClasses.add(new OnlineClass(8, "The Java, 8 to 11", false));

List<List<OnlineClass>> myEvents = new ArrayList<>();
myEvents.add(springClasses);
myEvents.add(javaClasses);


System.out.println("두 수업 목록에 들어있는 모든 수업 아이디 출력");
// TODO
myEvents.stream()
//                .flatMap(list -> list.stream())
        .flatMap(Collection::stream)
        .forEach(oc -> System.out.println(oc.getId()));

System.out.println("10부터 1씩 증가하는 무제한 스트림 중에서 앞에 10개 빼고 최대 10개 까지만");
// TODO
Stream.iterate(10, i -> i + 1)
        .skip(10)
        .limit(10)
        .forEach(System.out::println);

System.out.println("자바 수업 중에 Test가 들어있는 수업이 있는지 확인");
// TODO
boolean test = javaClasses.stream().anyMatch(oc -> oc.getTitle().contains("Test"));
System.out.println(test);

System.out.println("스프링 수업 중에 제목에 spring이 들어간 제목만 모아서 List로 만들기");
// TODO
List<String> spring = springClasses.stream()
        .map(OnlineClass::getTitle)
        .filter(s -> s.contains("spring"))
        .collect(Collectors.toList());

spring.forEach(System.out::println);
```

<-- 정리 필요 -->

---

<!-- Optional

- Optional 클래스는 null 처리를 보다 간편하게 하기 위해서 만들어졌다.
- 자칫 잘못하면 NullPointerException이 발생할 수도 있는데, 이 문제를 보다 간편하고 명확하게 처리하려면 Optional을 사용하면 된다.
- 단, Optional 클래스에 값을 잘못 넣으면 NoSuchElementException 이 발생할 수도 있으니 유의해서 사용해야만 한다.

날짜 관련 클래스들

- 이전에는 Date나 SimpleDateFormatter 를 사용하여 날짜를 처리했지만 쓰레드 세이프하지 않고, 불변 객체가 아니라서 지속적으로 값이 변경 가능했다.
- 1900년 1월 1 일은 1900, 1, 0을 매개 변수로 넘겨줘야 했음.
- 그래서 java.time 이라는 패키지가 새로 추가됌

Parallel Array Sorting

```java
Arrays.parallelSort(arr);
```

- sort() vs parallelSort()
- sort()는 단일 쓰레드로 수행됌
- parallelSort()는 필요에 따라 여러 개의 쓰레드로 나뉘어서 작업이 수행됌
- parallelSort()가 CPU를 더 많이 사용하겠지만 속도는 더 빠름
- 실제 비교해보면 5,000개쯤 부터 parallelSort()의 성능이 더 빨라지는 것을 알 수 있음
- 개수가 많지 않은 배열에서는 굳이 parallelSort()를 사용할 필요는 없다고 봐도 무방하다.

StringJoiner

- 순차적으로 나열되는 문자열을 처리할 때 사용

```java
String[] stringArray = new String[]{"StudyHard","GodOfZJava","Book"};
```

- 위를 `(StudyHard,GodOfJava,Book)` 이런식으로 바꾸고 싶을때?
- String에 계속 더하거나, StringBuilder나 StringBuffer를 사용할 수 있지만, 콤마(,)에 대한 처리를 해주지 않으면
  `(StudyHard ,GodOfJava,Book,)` 이런식의 결과가 나옴
- Book 뒤에 있는 콤마를 처리하기 위해서 if문을 넣거나 substring으로 콤마를 잘라 주어야 한 다. 이러한 단점을 보완하기 위해서 StringDoiner가 만들어졌다
- 배열의 구성요소 사이에 콤마만 넣고자 하려면 다음과 같이 사용하면 된다

```java
Stringjoiner joiner = new Stringjoiner(",");
for (String string : stringArray) {
    joiner.add(string);
}
System.out.println(joiner);
```

- 결과 -> `StudyHard,GodOfJava,Book`

- 앞 뒤에 소괄호를 넣는 예

```java
Stringjoiner joiner=new Stringjoiner(",", "(", ")");
for (String string:stringArray) {
    joiner.add(string);
}
System.out.println(joiner);
```

- 결과 -> `(StudyHard,GodOfJava,Book)`

- 이렇게 StringJoiner를 사용할 수 있지만 Stream과 Lambda식을 활용하면 아래와 같이도 사용할 수 있다.

```java
List<String> stringList = Arrays.asList(stringArray);
String result = stringList.stream()
    .collect(Collectors.joining(","));
System.out.printin(result);
```

--- -->
