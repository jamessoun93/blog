# [객체 지향 프로그래밍 #1] 캡슐화

- [1. 캡슐화](#1-캡슐화)
- [2. 예시](#2-예시)
- [3. 캡슐화 하지 않았다면?](#3-캡슐화-하지-않았다면)
- [4. 캡슐화 한다면?](#4-캡슐화-한다면)
- [5. 캡슐화의 또 다른 장점](#5-캡슐화의-또-다른-장점)
- [6. 캡슐화를 위한 규칙 #1: Tell, Don't Ask](#6-캡슐화를-위한-규칙-1-tell-dont-ask)
- [7. 캡슐화를 위한 규칙 #2: Law of Demeter](#7-캡슐화를-위한-규칙-2-law-of-demeter)
- [8. 연습 #1](#8-연습-1)
- [9. 연습 #2](#9-연습-2)
- [10. 연습 #3](#10-연습-3)
- [11. 연습 #4](#11-연습-4)

## 1. 캡슐화

**캡슐화**란 **데이터와 관련 기능을 묶는 것**을 뜻한다.

**캡슐화**를 통해 **기능에 대한 내부 구현을 외부에 감출 수** 있다. (숨기기 위한 감춤보다는 굳이 신경을 쓰지 않아도 되게끔 한다는 표현이 맞을 것 같다.)

**캡슐화**를 하는 가장 큰 이유는 **외부에 영향 없이 객체 내부 구현 변경을 가능하게 하기 위함**이다.

캡슐화를 하지 않았을 때 어떤 문제가 생길 수 있을지 예시를 통해 살펴보자.

## 2. 예시

```java
if (acc.getMembership() == REGULAR && acc.getExpDate().isAfter(now())) {
    ...정회원 기능
}
```

위와 같이 해당 계정의 멤버쉽이 REGULAR 면서 현재 날짜 기준 만료가 되지 않았다면 정회원 혜택을 제공하는 코드다.

하지만 시간이 지나면서 5년 이상 회원에 대한 정회원 혜택을 1개월 무상 제공하는 요구사항이 추가되어 아래와 같이 코드가 변경되었다.

```java
if (acc.getMembership() == REGULAR &&
    (
        (acc.getServiceDate().isAfter(fiveYearsAgo) && acc.getExpDate().isAfter(now())) ||
        (acc.getServiceDate().isBefore(fiveYearsAgo) && addMonth(acc.getExpDate()).isAfter(now()))
    )
) {
    ...정회원 기능
}
```

기존에는 멤버쉽 만료일자만 가지고 판단을 해도 됐었는데 요구사항이 변경돼 서비스 이용 시작 날짜까지 함께 확인해야 하게끔 변경되었다.

## 3. 캡슐화 하지 않았다면?

만약 해당 기능을 캡슐화하지 않았다고 가정해보자.

여기서 생각해야할 부분이 있다. 정회원이 이용할 수 있는 기능이 과연 서비스 전체에 하나만 있을까?

당연히 여러군데 퍼져 있을텐데 해당 기능을 캡슐화하지 않았다면 여러군데 퍼져 있는 정회원 확인 기능마다 찾아다니며 위의 변경사항을 적용해야 한다.

요구사항의 변화가 해당 데이터를 사용하는 모든 코드의 변화로 이어지게 되는 것이다.

물론 테스트 코드가 견고하게 잘 짜여 있다면 수정하다가 놓치는 부분을 바로바로 찾을 수는 있을테지만 그렇지 못할 경우 바로 버그로 이어지게 된다.

## 4. 캡슐화 한다면?

위에서 설명했듯이 **캡슐화**란 **데이터와 관련 기능을 묶는 것**을 뜻하고 해당 기능의 상세 구현을 감춘다.

이 개념을 이용해 캡슐화를 해보자.

기능을 제공하기 위해 **필요한 데이터**와 **해당 데이터를 사용하여 제공해야하는 기능**을 **클래스**로 분리하는 방식으로 캡슐화할 수 있다.

```java
public class Account {
    private Membership membership;
    private Date expDate;

    public boolean hasRegularPermission() {
        return membership == REGULAR && expDate.isAfter(now());
    }
}
```

이렇게 캡슐화 하면 위 1-1 의 예시의 if 문을 아래와 같이 변경할 수 있다.

```java
if (acc.hasRegularPermission()) {
    ...정회원 기능
}
```

단순하게 생각하면 코드가 줄었네? 읽기 편해졌네? 라고 생각할 수 있다. 그렇다면 새로 추가된 요구사항을 적용해보자.

```java
public class Account {
    private Membership membership;
    private Date expDate;

    public boolean hasRegularPermission() {
        return membership == REGULAR && (
            expDate.isAfter(now()) || (
                serviceDate.isBefore(fiveYearsAgo()) && addMonth(expDate).isAfter(now())
            )
        )
    }
}
```

이렇게 Account 클래스 내부 구현을 변경했을 때 해당 클래스에서 제공하는 기능을 활용하는 if 문은 어떻게 될까?

```java
if (acc.hasRegularPermission()) {
    ...정회원 기능
}
```

아무것도 변경되지 않는다.

특히 이런 기능은 여러군데서 쓰일 가능성이 높기 때문에 더더욱 캡슐화의 가치를 느낄 수 있다.

즉, 캡슐화를 잘 하면 요구사항이 변경됐을 때 연쇄적으로 변경이 전파되는 것을 최소화할 수 있다.

## 5. 캡슐화의 또 다른 장점

캡슐화의 또 다른 장점으로는 캡슐화를 시도하는 것만으로도 기능에 대한 이해를 높일 수 있다는 점이다.

```java
if (acc.getMembership() == REGULAR) {
    ...정회원 기능
}
```

이렇게 작성되어 있던 코드를 캡슐화하려면 `acc.getMembership() == REGULAR` 를 왜 확인하려고 하는지를 이해해야 한다.

위 예시에서는 정회원 권한을 가졌는지 확인하기 위함이니 `hasRegularPermission()` 같은 메서드명으로 기능을 제공할 수 있게 된다.

## 6. 캡슐화를 위한 규칙 #1: Tell, Don't Ask

캡슐화를 시도할 때 먼저 생각하면 좋을 규칙을 소개한다.

> Tell, Don't Ask

**데이터를 달라 하지 말고 해달라고 하기** 라는 뜻이다.

```java
if (acc.getMembership() == REGULAR) {
    ...정회원 기능
}
```

위 예시에서는 `acc` 객체에 `membership` 데이터를 달라고 한 뒤 REGULAR 인지 확인한다.

이런식으로 데이터를 달라고 하지 않고, REGULAR 권한을 가졌는지 확인해달라고 변경하면 아래와 같이 변한다.

```java
if (acc.hasRegularPermission()) {
    ...정회원 기능
}
```

결국 REGULAR 인지를 확인하는 이유는 REGULAR 권한을 가졌는지 궁금하기 때문이니 **데이터를 달라고 한 뒤 직접 확인을 하기보다 직접 권한을 가졌는지 물어보라는 뜻**이다.

## 7. 캡슐화를 위한 규칙 #2: Law of Demeter

디미터 법칙에 대한 조금 더 자세한 내용은 아래 테코블 블로그 글을 참고하자.

👉 [디미터 법칙(Law of Demeter)](https://tecoble.techcourse.co.kr/post/2020-06-02-law-of-demeter)

요약하자면,

- 메서드에서 생성한 객체의 메서드만 호출
- 파라미터로 받은 객체의 메서드만 호출
- 필드로 참조하는 객체의 메서드만 호출

결국 원하는 것을 얻기 위해 연결된 객체들을 따라가지 말고, 메서드만 호출하는 방식으로 결합도를 낮출 수 있다는 내용이다.

```java
acc.getExpDate().isAfter(now);
```

`acc` 로부터 `expDate` 를 받아와 현재 날짜와 비교를 하기보다,

```java
acc.isExpired();
```

단순하게 만료되었는지 확인을 하는 메서드를 호출하라는 뜻이다.

이런식으로 변경하기 위해서는 애초에 왜 위에서 처럼 `expDate` 를 구해서 `isAfter` 를 하려하는지 이유를 파악하게 되고, 자연스럽게 목적에 맞는 메서드로 구성하기 위해 캡슐화를 하게 된다.

## 8. 연습 #1

```java
public AuthResult authenticate(String id, String pw) {
    Member member = findOne(id);
    if (member == null) return AuthResult.NO_MATCH;

    if (member.getVerificationEmailStatus() != 2) {
        return AuthResult.NO_EMAIL_VERIFIED;
    }

    if (passwordEncoder.isPasswordValid(member.getPassword(), pw, member.getId())) {
        return AuthResult.SUCCESS;
    }

    return AuthResult.NO_MATCH;
}
```

제공된 코드에서 어떤 부분을 캡슐화 해볼지 위에서 정리한 규칙들을 떠올리며 살펴보자.

> Tell, Don't Ask

데이터를 가져와서 판단하는 부분이 있다면?

```java
if (member.getVerificationEmailStatus() != 2) {
    return AuthResult.NO_EMAIL_VERIFIED;
}
```

위 if 문이 좋은 캡슐화 후보가 될 수 있을 것 같다.

```java
if (!member.isEmailVerified()) {
    return AuthResult.NO_EMAIL_VERIFIED;
}
```

if 문을 위와 같이 변경하고, `Member` 클래스에 `isEmailVerified` 메서드를 아래와 같이 구현할 수 있다.

```java
public class Member {
    private int verificationEmailStatus;

    public boolean isEmailVerified() {
        return verificationEmailStatus == 2;
    }
}
```

이렇게 하면 `isEmailVerified` 메서드의 내부 코드가 변경돼도 해당 메서드를 호출하는 쪽에서는 변경할 코드가 없다.

## 9. 연습 #2

```java
public class Rental {
    private Movie movie;
    private int daysRented;

    public int getFrequentRenterPoints() {
        if (movie.getPriceCode() == Movie.NEW_RELEASE && daysRented > 1) return 2;
        else return 1;
    }
}
```

```java
public class Movie {
    public static int REGULAR = 0;
    public static int NEW_RELEASE = 1;
    private int priceCode;

    public int getPriceCode() {
        return priceCode;
    }
}
```

마찬가지로 데이터를 달라고 하는 부분을 찾아보자.

`movie.getPriceCode() == Movie.NEW_RELEASE` 이 부분이 있다.

이 부분을 `movie.isNewRelease()` 로 변경하고 `Movie` 클래스에서 `isNewRelease()` 메서드를 구현할 수 있다.

```java
public class Rental {
    private Movie movie;
    private int daysRented;

    public int getFrequentRenterPoints() {
        if (movie.isNewRelease() && daysRented > 1) return 2;
        return 1;
    }
}
```

하지만 조금 더 나아가 `daysRented` 를 받아 계산까지 해주는 방식으로 통으로 메서드화를 해볼 수 있을 것 같다.

```java
public class Rental {
    private Movie movie;
    private int daysRented;

    public int getFrequentRenterPoints() {
        return movie.getFrequentRenterPoints(daysRented);
    }
}
```

```java
public class Movie {
    public static int REGULAR = 0;
    public static int NEW_RELEASE = 1;
    private int priceCode;

    public int getFrequentRenterPoints(int daysRented) {
        if (priceCode == NEW_RELEASE && daysRented > 1) return 2;
        return 1;
    }
}
```

이렇게 포인트 계산 로직 자체를 `Movie` 클래스에 위임할 수 있다.

데이터를 가지고 있는 쪽에 기능 구현을 하면서 처리를 위해 필요한 값을 인자로 넘겨받는 방식으로 캡슐화를 한 것이다.

## 10. 연습 #3

```java
public void randomMethod() {
    Timer t = new Timer();
    t.startTime = System.currentTimeMillis();

    ...

    t.stopTime = System.currentTimeMillis();
    long elapsedTime = t.stopTime - t.startTime;
}
```

```java
public class Timer {
    public long startTime;
    public long stopTime;
}
```

`randomMethod` 에서는 전부 `Timer` 객체의 값을 직접 사용하고 있다.

만약 시간을 측정하는 unit 이 millisecond 에서 second 로 바뀌면 어떻게 될지 안봐도 눈에 훤하다.

위에서 언급한 규칙대로라면 캡슐화하기 좋은 예다.

```java
public void randomMethod() {
    Timer t = new Timer();
    t.start();

    ...

    t.stop();
    long elapsedTime = t.elapsedTime(MILLISECOND);
}
```

```java
public class Timer {
    public long startTime;
    public long stopTime;

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
    }

    public long elapsedTime(TimeUnit unit) {
        switch(unit) {
            case MILLISECOND:
                return stopTime - startTime;
            ...
        }
    }
}
```

이렇게 `randomMethod` 에서 필요한 기능만 갖다쓰도록 변경되었다.

만약 unit 이 바뀌어도 `randomMethod` 에서는 파라미터로 넘기는 unit 값 외에는 바뀌는 코드가 없다.

## 11. 연습 #4

```java
public void verifyEmail(String token) {
    Member member = findByToken(token);
    if (member == null) throw new BadTokenException();

    if (member.getVerificationEmailStatus() == 2) {
        throw new AlreadyVerifiedException();
    } else {
        member.setVerificationEmailStatus(2);
    }
    ...
}
```

먼저 후보를 찾아보자.

`member.getVerificationEmailStatus() == 2` 를 `member.isEmailVerified()` 의 형태로 바꿀 수 있을 것 같다.

하지만 바꾸고 나도 사실상 코드 구조는 그대로라 딱히 큰 도움이 되지는 않는 것 같다.

바로 else 에 있는 setter 를 활용해서 데이터를 변경하는 부분 때문인데,

이런식으로 데이터를 가져와 판단한 뒤 값을 변경하는 방식을 담은 코드는 통째로 캡슐화를 하면 큰 도움이 된다.

```java
public void verifyEmail(String token) {
    Member member = findByToken(token);
    if (member == null) throw new BadTokenException();

    member.verifyEmail();
    ...
}
```

그리고 `Member` 클래스에 `verifyEmail` 기능을 구현한다.
