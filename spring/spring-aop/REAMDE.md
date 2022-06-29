# (작성중) Spring: AOP (Aspect Oriented Programming)

## Intro

```java
class A {
    method a () {
        AAAA
        스프링 AOP를 배워보자
        BBBB
    }

    method b () {
        AAAA
        JPA를 배워보자
        BBBB
    }
}

class B {
    method a () {
        AAAA
        MSA를 구축해보자
        BBBB
    }
}
```

위 예시처럼 AAAA와 BBBB처럼 메인 로직 앞뒤로 반복적으로 수행되는 작업들이 있을 때, 만약 AAAA에 기능을 일부 수정한다면 AAAA를 사용하는 모든 코드를 변경해야하는 불편함이 있음.

그래서 이런것들은 따로 모으는게 좋지 않을까?

(python decorator 같은 개념)

@Transactional 같은 애노테이션이 Spring AOP를 사용하여 만들어진 예.

---

- 모든 메소드의 호출 시간을 측정하고 싶다면?
- 공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern)
- 회원 가입 시간, 회원 조회 시간을 측정하고 싶다면?

```java
method 회원가입 () {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    // 회원가입 로직

    stopWatch.stop();
    stopWatch.prettyPrint();
}
```

해당 기능을 사용하고 싶은 모든 메서드에 위 StopWatch 코드를 로직 위 아래로 붙여넣기 해야함.

문제

- 회원가입, 회원 조회에 시간을 측정하는 기능은 핵심 관심 사항이 아니다.
- 시간을 측정하는 로직은 공통 관심 사항이다.
- 시간을 측정하는 로직과 핵심 비즈니스의 로직이 섞여서 유지보수가 어렵다.
- 시간을 측정하는 로직을 별도의 공통 로직으로 만들기 매우 어렵다.
- 시간을 측정하는 로직을 변경할 때 모든 로직을 찾아가면서 변경해야 한다.

결과

- 회원가입, 회원 조회등 핵심 관심사항과 시간을 측정하는 공통 관심 사항을 분리한다.
- 시간을 측정하는 로직을 별도의 공통 로직으로 만들었다.
- 핵심 관심 사항을 깔끔하게 유지할 수 있다.
- 변경이 필요하면 이 로직만 변경하면 된다.
- 원하는 적용 대상을 선택할 수 있다.

---

## 다양한 AOP 구현 방법

1. 컴파일

`.java` 파일이 `.class` 파일로 컴파일되는 과정 사이에 AOP를 적용하는 방식. (AspectJ)

2. 바이트코드 조작

위의 컴파일 과정이 완료된 후 클래스 로더가 클래스 파일을 읽어와서 메모리에 올릴 때 조작하는 방식. (AspectJ)

3. 프록시 패턴 (Spring AOP가 활용하는 방법)

@Transactional 애노테이션의 예.

애노테이션만 붙이면 트랜잭션 수행 로직이 적용된 Repository 클래스의 프록시가 생성되어 활용되어 짐.

그래서 우리는 굳이 메인 로직 위아래로 트랜잭션 코드를 작성할 필요가 없는 것.

---
