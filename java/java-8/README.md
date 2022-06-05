# Java 8에 추가된 것들 333

Optional

- Optional 클래스는 null 처리를 보다 간편하게 하기 위해서 만들어졌다.
- 자칫 잘못하면 NullPointerException이 발생할 수도 있는데, 이 문제를 보다 간편하고 명확하게 처리하려면 Optional을 사용하면 된다.
- 단, Optional 클래스에 값을 잘못 넣으면 NoSuchElementException 이 발생할 수도 있으니 유의해서 사용해야만 한다.

Default Method

- 여러분들이 만약 오픈 소스 코드를 만들었다고 가정하자. 그 오픈 소스가 엄청 유명해져서 전 세계 사람들이 다 사용하고 있는데, 인터페이스에 새로운 메소드를 만들어야 하 는 상황이 발생했다. 자칫 잘못하면 내가 만든 오픈 소스를 사용한 사람들은 전부 오류가 발생 하고 수정을 해야 하는 일이 발생할 수도 있다. 이럴 때 사용하는 것이 바로 default 메소드다.

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
String[] stringArray=new String[]{"StudyHard","GodOfZJava","Book"};
```

- 위를 `(StudyHard,GodOfJava,Book)` 이런식으로 바꾸고 싶을때?
- String에 계속 더하거나, StringBuilder나 StringBuffer를 사용할 수 있지만, 콤마(,)에 대한 처리를 해주지 않으면
  `(StudyHard ,GodOfJava,Book,)` 이런식의 결과가 나옴
- Book 뒤에 있는 콤마를 처리하기 위해서 if문을 넣거나 substring으로 콤마를 잘라 주어야 한 다. 이러한 단점을 보완하기 위해서 StringDoiner가 만들어졌다
- 배열의 구성요소 사이에 콤마만 넣고자 하려면 다음과 같이 사용하면 된다

```java
Stringjoiner joiner=new Stringjoiner(",");
for(String string:stringArray) {
    joiner.add(string);
}
System.out.println(joiner);
```

- 결과 -> `StudyHard,GodOfJava,Book`

- 앞 뒤에 소괄호를 넣는 예

```java
Stringjoiner joiner=new Stringjoiner(",", "(", ")");
for(String string:stringArray) {
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

Runnable 예시 (생략)

정리

- 메소드가 하나만 존재하는 인터페이스는 @FunctionalInterace로 선언할 수 있으며, 이 인터페이스를 람다 표현식으로 처리할 수 있다.
- (매개 변수 목록) -〉 처리식으로 람다를 표현하며, 처리식이 한 줄 이상일 때에는 처리식을 중괄호로 묶을 수 있다.

# java.util.function 패키지

Java 8에서 제공하는 주요 Functional 인터페이스는 java.util.function 패키지에 다음과 같이 있다.

- Predicate
- Supplier
- Consumer
- Function
- UnaryOperator
- Binaryoperator
