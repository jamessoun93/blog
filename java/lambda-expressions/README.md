# Java 8: Lambda Expressions

람다식이란 Java 8에 새로 추가된 기능입니다.

람다식은 한개의 메소드만 있는 인터페이스를 더 편하게 사용할 수 있게 해주고, 보통 익명 클래스를 사용할 때 많이 사용합니다.

람다식을 사용하는 방법에 대해서 알아보기 위해 쓰레드를 생성하는 코드를 작성해보겠습니다.

```java
// Runnable 구현 클래스 사용

public class Main {
    public static void main(String[] args) {
       new Thread(new MyRunnable()).start();
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("From Runnable");
    }
}
```

위와 같이 `Runnable` 인터페이스를 구현하는 클래스를 생성해서 `run()` 메소드를 오버라이드 해서 사용하는 방법이 있고, 아래와 같이 익명 클래스를 활용하는 방법도 있습니다.

```java
// 익명 클래스 사용

public class Main {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("From Runnable");
            }
        }).start();
    }
}
```

위의 두 가지 예시 모두 `System.out.println(...)` 문을 실행하기 위해 여러 줄의 부가적인 코드를 추가해야 합니다.

프린트문을 제외한 코드의 나머지 부분은 객체를 인스턴스화하고 단 하나의 메소드를 구현하기 위해 존재합니다.

반복적으로 이런 작업을 할 때 개발자라면 이런 부분을 어떻게 줄일 수 없을까 하는 생각이 들 수 있습니다.

새로운 쓰레드를 생성하는 것은 필요하니 해당 `Thread` 생성자에 단순히 내가 `run()` 메소드의 body에 넣고 싶은 코드를 바로 넘겨줄 수 있다면 편하지 않을까요?

람다식을 활용하면 가능합니다.

```java
// 람다 작용

public class Main {
    public static void main(String[] args) {
        new Thread(() -> System.out.println("From Runnable")).start();
    }
}
```

굉장히 간결하죠?

람다식은 메소드의 이름과 리턴 타입이 없어서 익명함수라고도 불립니다.

TBD
