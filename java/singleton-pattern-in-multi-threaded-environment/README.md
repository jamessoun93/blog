# 멀티스레드 환경에서 Thread Safe 하게 Singleton Pattern 사용하기

## Singleton Pattern 이란?

GoF Design Patterns 중 하나로 같은 생성자가 여러번 호출되더라도 최초 생성자 호출시 생성되는 하나의 동일한 인스턴스가 반복적으로 반환되는 패턴입니다.

사용할 클래스의 인스턴스가 최초 한번 메모리에 할당된 뒤 매번 똑같은 역할을 하는 인스턴스를 여러개 만드는것이 아닌 동일한 인스턴스를 사용하게 해서 메모리 효율적으로 사용하는 기법입니다.

가장 일반적인 싱글톤 패턴 구현 방법은 아래와 같습니다.

```java
class Singleton {
    private static Singleton myInstance = null;

    private Singleton() {}

    public static Singleton getInstance() {
        if (myInstance == null) {
            myInstance = new Singleton();
        }

        return myInstance;
    }
}
```

Singleton 클래스의 생성자를 private으로 선언하여 접근이 불가능하게 하고 Singleton 클래스에 해당 instance를 반환하는 static 메소드를 두고 사용합니다.

보통 인스턴스를 생성할 때는 아래와 같이 new 키워드를 사용해서 새로운 instance를 생성합니다.

```java
Singleton mySingleton = new Singleton();
```

싱글톤 패턴을 적용하면 생성자를 사용해서 매번 새로운 instance를 만들지 않고, 이미 만들어진 인스턴스가 존재하는지 확인하고 생성 혹은 반환하는 static 메소드를 활용하여 호출하게 됩니다.

```java
Singleton mySingleton = Singleton.getInstance();
```

## 멀티쓰레드 환경에서 Singleton Pattern의 문제점

Singleton 패턴을 싱글쓰레드 환경에서 사용할 때는 전혀 문제가 되지 않겠지만, 멀티쓰레드 환경에서는 동시성 문제가 발생할 수 있는 가능성이 있습니다.

여러개의 쓰레드가 동시에 getInstance() 메소드에 접근한다고 할 때 여러개의 인스턴스가 만들어질수도 있는 상황이 발생할 수 있기 때문입니다.

데이터베이스 커넥션 풀 객체를 공용으로 사용하기 위해 싱글톤 패턴을 적용했는데, 여러개가 생겨버린다면 굉장히 비효율적이겠죠?

## 해결방법

멀티쓰레드 환경에서 Singleton Pattern 사용시 발생할 수 있는 동시성 문제를 해결하는 방법은 여러가지가 있습니다.

물론 모든 해결방법에는 trade-off가 존재합니다.

그렇기 때문에 각각의 장단점을 잘 파악하고 효율적인 어플리케이션을 만들기 위해 알맞는 해결방법을 적용할 수 있어야 합니다.

### 해결방법 1 - synchronized 메소드 선언

떠올리기 가장 쉬운 방법으로 `synchronized` 키워드를 사용하는 방법이 있습니다.

```java
class Singleton {
    private static Singleton myInstance = null;

    private Singleton() {}

    public static synchronized Singleton getInstance() {
        if (myInstance == null) {
            myInstance = new Singleton();
        }

        return myInstance;
    }
}
```

`synchronized` 키워드를 사용하여 `getInstance()` 메소드를 동기화해주면, 최초로 접근한 쓰레드가 해당 메소드 호출을 종료할 때까지 다른 쓰레드가 접근하지 못하도록 lock을 걸어줍니다.

여러개의 쓰레드가 동시 접근하면서 생길 수 있는 문제는 방지할 수 있다는 장점이 있지만, `getInstance()` 메소드를 호출할 때마다 lock이 걸려 성능저하가 발생한다는 문제가 있습니다.

최초 instance 생성 시 여러개의 instance가 생기는 것을 방지하고자 동기화를 해줬는데, instance가 정상적으로 생성된 이후에도 `getInstance()` 메소드를 호출할 때마다 lock을 걸어주는건 불필요하겠죠?

제 역할은 잘 수행하지만 오버헤드가 굉장히 커서 권장하지 않는 방법입니다.

### 해결방법 2 - DCL(Double Checked Locking) 방식

DCL 방식에서 Double Checked는 재확인한다는 의미에서 사용되었는데요,

위의 `synchronized` 메소드를 선언해서 동기화하는 방식처럼 호출할 때마다 불필요하게 lock을 거는 방식이 아니라, 생성된 인스턴스가 존재하지 않을때만 lock을 걸어 인스턴스 생성 과정에서 발생할 수 있는 동시성 문제를 해결하는 방법입니다.

이렇게 되면 최초 인스턴스 생성이 필요할때는 lock을 이용해 thread safe하게 인스턴스를 생성한 뒤 할당하고, 그 이후 해당 인스턴스 접근시 불필요하게 lock을 걸지 않고 사용할 수 있어 깔끔해보이는 방법입니다.

하지만 이 방법에도 문제가 있습니다.

thread A와 B가 있다고 가정해보겠습니다.

thread A가 최초 `getInstance()` 메소드에 접근하여 `myInstance`가 `null`인 것을 확인하고 `myInstance`에 새로운 `Singleton` 객체를 생성해주러 `synchronized` 블록에 진입합니다.

`myInstance`는 비어있기 때문에 `myInstance = new Singleton();` 을 실행합니다.

바로 여기서 문제가 발생할 수 있는 가능성이 있습니다.

자바 메모리 모델은 기본적으로 부분적으로 초기화된 객체에 대한 접근을 허용합니다.

`myInstance = new Singleton();` 를 처리할 때 `myInstance` 라는 공간을 먼저 할당한 뒤 아직 새로운 `Singleton` 객체의 레퍼런스를 미리 할당해둔 `myInstance` 에 할당하지 않은 상태일 때 `myInstance`에 대한 접근이 가능하다는 뜻입니다.

그렇기 때문에 낮은 확률로 thread B가 `getInstance()` 메소드에 접근했을 때 `myInstance`가 `null`이 아니게 되어 `Singleton` 객체를 생성하지 않고 넘어가 문제가 발생할 수도 있습니다.

```java
class Singleton {
    private static Singleton myInstance = null;

    private Singleton() {}

    public static Singleton getInstance() {
        if (myInstance == null) {
            synchronized (Singleton.class) {
                if (myInstance == null) {
                    myInstance = new Singleton();
                }
            }
        }

        return myInstance;
    }
}
```

### 해결방법 3 - DCL 방식에 volatile 키워드 사용

(cache incoherence 문제에 대해 조금 더 공부해야할듯)

참고: https://jenkov.com/tutorials/java-concurrency/cache-coherence-in-java-concurrency.html

### 해결방법 4 - static 초기화

위에서 소개한 세 가지 방법은 전부 런타임 시 필요한 인스턴스를 생성해서 할당하는 방식입니다.

이번에 소개드릴 static 초기화 방식은 런타임 시가 아닌 최초 JVM이 Class Loader를 이용해서 class path 내에 있는 모든 클래스들을 로드할 때 미리 인스턴스를 생성해주는 방법입니다.

```java
class Singleton {
    private static Singleton myInstance = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() {
        return myInstance;
    }
}
```

이 방법의 장점으로는 클래스를 로드하면서 인스턴스가 생성되기 때문에 런타임 시 동시성을 제어할 목적으로 사용하는 `synchronized` 키워드와 `volatile` 키워드를 전혀 사용할 필요가 없어집니다.

위의 해결 방법들의 문제점들을 전부 해결해주는 아주 좋은 방법처럼 보여지지만 여기에도 물론 단점은 존재합니다.

해당 클래스를 약 300초 뒤에 사용한다고 했을때 이 static 초기화 방식을 사용하면 300초 뒤에 사용하나 3초뒤에 사용하나 무조건 최초 클래스가 로딩될 때 만들어지기 때문에 아직 필요하지 않은 시점에도 불필요하게 메모리 자원을 선점하고 있게 됩니다.

심지어 사용하지 않게 되는 경우가 있을 수도 있는데 그런 경우 불필요한 자원 낭비가 될 수 있습니다.

실제로 이렇게 객체 하나 만들어두는게 그렇게 큰 자원낭비인가 싶을 수 있겠지만, 이런 것들을 파악해서 optimize 할 수 있는 능력은 꼭 필요하다고 생각합니다.

### 해결방법 5 - LazyHolder 방식

LazyHolder 방식은 Class Loader에 약간의 트릭을 줘서 위의 static 초기화 방식의 문제점을 보완하는 방법입니다.

```java
class Singleton {
    private Singleton() {}

    public static Singleton getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder() {
        private static final Singleton INSTANCE = new Singleton();
    }
}
```

위와 같이 LazyHolder라는 Inner Class를 선언해서 Singleton 클래스가 최초 클래스 로딩 단계에서 로드가 되더라도 싱글톤 인스턴스는 생성되지 않습니다.

그렇기 때문에 `getInstance()`가 호출될 때 접근을 해서 생성하게 되는 것이죠.

### 해결방법 6 - ENUM 방식
