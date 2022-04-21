# JVM: Runtime Data Area

(참고: 해당 포스팅은 Java SE8 Specification을 기준으로 작성되었습니다.)

Java Virtual Machine(JVM) 구조는 크게 Class Loader, Runtime Data Area, Execution 이렇게 세 가지로 나눌 수 있는데, 여기서는 메모리 영역인 Runtime Data Area에 대한 내용을 다룬다.

# Runtime Data Area

Runtime Data Area란, JVM이 프로그램을 수행하기 위해 OS로부터 할당받은 메모리 영역이다.

이 메모리 공간은 5가지 영역으로 쪼개어져 있으며 크게 두가지로 분류된다.

JVM 시작시 생성되며 JVM 종료시 소멸되는 영역들로는 Method Area와 Heap이 있으며, 모든 쓰레드 간 자원이 공유된다.

각 쓰레드가 생성될 때 생성되고 해당 쓰레드가 종료될 때 소멸되는 영역들에는 PC Register, JVM Stack, Native Method Stack이 있다.

## 1. Method Area

Method Area에는 Runtime Constant Pool(런타임 상수 풀), 필드 및 메서드 데이터와 같은 모든 클래스 레벨 데이터와 메서드 및 생성자 대한 코드가 저장된다.

```java
public class Dog {

    private String name;
    private int age;

    public Dog(String name, int age) {

        this.name = name;
        this.age = age;
    }
}
```

위와 같은 클래스가 있다고 한다면, `name` 과 `age` 같은 필드 데이터와 생성자 정보가 바로 이 Method Area에 저장된다.

위에서 설명했듯이 Method Area는 JVM 시작시 생성되며 JVM 종료시 소멸되고 모든 쓰레드 간 자원이 공유되며, JVM 당 Method Area는 하나만 존재한다.

만약 Method Area에 공간이 부족하여 새로운 공간 할당이 불가능하다면 OutOfMemoryError가 throw 된다.

## 2. Heap

Heap 영역은 Object 클래스를 상속받는 모든 객체들(클래스 인스턴스들)과 해당 인스턴스의 변수들을 담는 공간이다.

```java
Dog bubbles = new Dog("Bubbles", 7);
```

위 코드에서 new 키워드를 통해 새로운 Dog 인스턴스가 생성되고, 생성된 Dog 인스턴스는 heap 영역에 할당된다.

Heap 영역은 Garbage Collection 이라는 메커니즘을 통해 자동으로 저장공간이 관리된다.

Heap 영역은 크게 Young Generation과 Old Generation으로 구분된다.

### 2-1. Heap: Young Generation

Young Gen은 Eden 영역과 Survivor Memory Spaces라 불리우는 S0, S1 영역으로 나뉘어진다.

새로 생성된 객체는 항상 Eden 영역에 생성된다.

GC (Young Gen에서는 Minor GC) 싸이클을 넘기면서 더 이상 레퍼런스되지 않는 (작성중)

### 2-2. Heap: Old Generation

Heap 영역의 구조를 이해해야 GC를 이해할 수 있으며, 정말 중요하고 내용이 방대하기 때문에 GC를 주제로 따로 포스트를 작성할 예정이다.
