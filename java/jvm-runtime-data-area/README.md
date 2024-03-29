# JVM: Runtime Data Area

[Tistory 블로그 포스팅 바로가기](https://seunghyunson.tistory.com/23)

Java Virtual Machine(JVM) 구조는 크게 Class Loader, Runtime Data Area, Execution 이렇게 세 가지로 나눌 수 있는데, 여기서는 메모리 영역인 Runtime Data Area에 대한 내용을 다룹니다.

# Runtime Data Area

Runtime Data Area란, JVM이 프로그램을 수행하기 위해 OS로부터 할당받은 메모리 영역입니다.

이 메모리 공간은 5가지 영역으로 쪼개어져 있으며 크게 두 가지로 분류됩니다.

JVM 시작시 생성되며 JVM 종료 시 소멸되는 영역들로는 Method Area와 Heap이 있으며, 모든 쓰레드 간 자원이 공유됩니다.

각 쓰레드가 생성될 때 생성되고 해당 쓰레드가 종료될 때 소멸되는 영역들에는 Stack, PC Register, Native Method Stack이 있습니다.

![이미지 출처: https://dzone.com/articles/jvm-run-time-data-areas](images/1.jpeg)

## 1. Method Area

Method Area는 JVM 벤더마다 다르게 구현되어 있습니다.

Oracle Hotspot JVM의 Method Area는 JDK 7까지는 Permanent Generation(PermGen)이라고 불렀고, JDK 8 버전부터는 Metaspace로 완전히 대체되었습니다.

PermGen과 Metaspace의 차이는 아래에서 살펴보고 우선 PermGen이 무엇을 하는 공간인지에 대해 먼저 알아보겠습니다.

PermGen은 Runtime Constant Pool(런타임 상수 풀), 필드 및 메서드 데이터와 같은 모든 클래스 레벨 데이터와 메서드 및 생성자의 코드가 저장되는 곳입니다.

전역 변수나 static 변수들 또한 이 영역에 저장되며 프로그램이 종료될 때까지 메모리 남아있게 되고, 프로그램이 종료될 때까지 어디서든 사용 가능하게 됩니다.

그렇다고 모든 static 변수가 PermGen에 저장되는 것은 아닙니다. 만약 해당 static 변수가 어떤 object에 대한 레퍼런스라면 참조 대상이 되는 object는 다른 object들과 마찬가지로 heap 영역에 저장됩니다.

(Heap 영역에 대한 내용은 아래 있습니다.)

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

위와 같은 클래스가 있다고 한다면, `name` 과 `age` 같은 필드 데이터와 생성자 정보가 바로 이 Method Area(PermGen)에 저장됩니다.

위에서 설명했듯이 Method Area는 JVM 시작 시 생성되며 JVM 종료 시 소멸되고 모든 쓰레드 간 자원이 공유되며, JVM 당 Method Area는 하나만 존재합니다.

만약 Method Area에 공간이 부족하여 새로운 공간 할당이 불가능하다면 `OutOfMemoryError`가 throw 됩니다.

### 1-1. Runtime Constant Pool

런타임 상수 풀에는 클래스/인터페이스의 메소드, 필드, 문자열 상수 등의 레퍼런스가 저장되며, 이들의 물리적인 메모리 위치를 참조할 경우에 사용됩니다.

JVM은 이 상수풀에 존재하는 데이터를 사용하여 같은 데이터를 참조하는 다른 클래스들과 연결시켜줍니다.

### 1-2. PermGen(JDK 7) -> Metaspace (JDK 8)

JDK 7 메모리 구조 (PermGen)

![이미지 출처: https://docs.deistercloud.com/content/Axional%20development%20libraries.20/Studio%20J2EE%20Server.4/Tunning.xml?embedded=true](images/2.png)

JDK 8 메모리 구조 (Metaspace)

![이미지 출처: https://docs.deistercloud.com/content/Axional%20development%20libraries.20/Studio%20J2EE%20Server.4/Tunning.xml?embedded=true](images/3.png)

이 PermGen 영역이 JDK 8부터는 Metaspace의 완전히 대체되었다고 했는데, 왜 이런 변화가 있었을까요?

기존 PermGen 영역은 크기가 제한적이라 `OutOfMemoryError`를 유발한다는 단점이 있었습니다.

크기가 작기 때문에 JVM은 비용이 많이 드는 가비지 컬렉션(GC)을 수시로 수행해 필요한 공간을 확보해야 했고, class loader들이 제대로 GC되지 않는 문제로 인해 memory leak이 발생하곤 했습니다. 그렇다고 이 PermGen 영역의 공간을 필요에 따라 자동으로 증가시키는 것은 불가능했기 때문에 성능에 대한 튜닝이 어려운 문제가 있었던 것이죠.

이런 문제로 인해, PermGen은 JDK 8 버전부터 완전히 Metaspace라는 영역으로 대체되었습니다.

기존 PermGen과 Metaspace의 가장 큰 차이점은 메모리 할당 방식입니다.

Metaspace는 기존 Java Heap 메모리 옆에 붙어있지 않고, OS에서 제공하는 native 메모리를 사용하며, 필요시 자동으로 크기를 증가시켜 공간을 확보하게 됩니다.

이러한 변경사항으로 인해 클래스 메타데이터 사용량이 메타스페이스의 최대 크기에 도달할 때 자동으로 GC가 돌게 되면서 JVM은 `OutOfMemoryError`의 발생확률을 줄일 수 있습니다.

## 2. Heap

Heap 영역은 Object 클래스를 상속받는 모든 객체들(클래스 인스턴스들)과 해당 인스턴스의 변수들을 담는 공간입니다.

```java
Dog bubbles = new Dog("Bubbles", 7);
```

위 코드에서 new 키워드를 통해 새로운 Dog 인스턴스가 생성되고, 생성된 Dog 인스턴스는 heap 영역에 할당됩니다.

Heap 영역은 Garbage Collection이라는 메커니즘을 통해 자동으로 저장공간이 관리됩니다.

Heap 영역은 크게 Young Generation과 Old Generation으로 구분됩니다.

![이미지 출처: https://medium.com/platform-engineer/understanding-java-memory-model-1d0863f6d973](images/4.png)

### 2-1. Heap: Young Generation

Young Gen은 Eden 영역과 Survivor Memory Spaces라 불리는 S0, S1 영역으로 나뉩니다.

새로 생성된 객체는 항상 Eden 영역에 생성됩니다.

GC 싸이클을 거치면서 (Young Gen에서는 Minor GC) 더 이상 레퍼런스 되지 않는 객체를 정리함과 동시에 지속적으로 참조되고 있는 객체를 S0, S1 영역을 활용하여 모아둡니다.

### 2-2. Heap: Old Generation

여러 번의 Minor GC 싸이클을 (threshold를 설정할 수 있음) 거친 뒤에도 오랫동안 살아남는 객체들은 Old Generation 영역으로 옮겨지게 됩니다.

Old Gen 영역도 가득 차게 되는 경우 Major GC에 의해 공간이 정리됩니다.

여기서는 간단하게 Heap 영역의 구조를 설명하지만, 실제로 Heap 영역에 대한 이해가 있어야 GC 메커니즘을 이해할 수 있습니다.

정말 중요하고 내용이 방대하기 때문에 GC를 주제로 따로 포스트를 작성할 예정입니다.

Heap 영역 또한 JVM 시작 시 생성되며 JVM 종료 시 소멸되어 JVM 당 Heap 영역은 하나만 존재하고, 모든 쓰레드 간 자원이 공유되기 때문에 동기화 문제가 발생할 수 있습니다.

## 3. Stack

Stack은 위에서 언급했던 새로운 쓰레드가 생성될 때마다 생성되는 영역입니다.

스택 영역은 쓰레드가 실행되면서 실행하는 메소드 별로 해당 스코프 내 할당된 로컬 변수 및 기본형 타입(Primitive type) 변수의 값을 담고, heap 영역에 존재하는 객체에 대한 레퍼런스를 담습니다.

![이미지 출처: baeldung.com](images/5.jpeg)

해당 값들을 Stack Frame이라는 블록에 담게 되고, 새로운 메소드가 호출될 때마다 이전에 존재하던 Stack Frame 위에 새로운 Stack Frame이 쌓이게 됩니다.

JVM Stack은 우리가 익히 알고 있는 Last-In-First-Out(LIFO)를 구현하는 자료구조인 Stack과 동일한 방식으로 작동합니다.

JavaScript의 Call Stack(호출 스택)에 대해 알고 있다면 같다고 생각하면 됩니다.

메소드가 실행을 마치게되면 해당하는 stack frame은 사라지고 가장 최근 추가되었던 stack frame에 해당하는 메소드가 이어서 실행됩니다.

Stack 영역 내에 존재하는 데이터는 해당 stack frame 이 존재하는 한 살아있게 됩니다.

만약 stack 영역이 가득 차게 된다면 Java는 java.lang.StackOverFlowError를 throw 합니다.

혹시 Stack Overflow에 대해 잘 모를까 봐 설명을 덧붙이자면,  
엘레베이터에서 양쪽에 거울이 있어서 내 모습이 여러 겹으로 끝없이 반사되는 것을 본 적이 있을 텐데 이런 현상과 비슷합니다.  
프로그래밍에서의 예시로는, 재귀 함수를 구현하고 종료되는 시점을 명확하게 만들어두지 않아 특정 메소드의 body에서 본인을 호출하고, 호출된 본인의 body에서 또 본인을 호출해서 Stack Frame이 끝없이 쌓이게 되는 현상을 말합니다.

Stack 영역은 쓰레드 별로 따로 존재하기 때문에 동시에 실행되고 있는 다른 쓰레드의 영향을 받지 않습니다.  
이걸 threadsafe 하다고 합니다.

## 4. PC Register

Java SE8 Specification에 의하면, JVM은 한 번에 많은 쓰레드를 실행할 수 있습니다.

이렇게 실행되는 각 쓰레드 별로 Program Counter 레지스터를 가지고 있습니다.

Program Counter Register란, 메모리에서 실행될 다음 명령어의 주소를 가진 프로세서의 레지스터를 뜻합니다. 실행되는 각 명령마다 다음 순서로 실행할 명령어의 주소를 트랙하는데 필요한 자료구조입니다.

JVM의 PC Register는 JVM 쓰레드가 현재 실행중인 메소드의 명령을 담고 있는 주소를 포함하게 되고, 실행중인 JVM 쓰레드가 무엇을 하는지를 관리하는 일종의 관리자라고 표현할 수 있습니다.

## 5. Native Method Stack

Native Method는 Java가 아닌 다른 언어로 작성된 메서드입니다. C, C++ 또는 다른 프로그래밍 언어일 수 있습니다.

Native Method Stack은 JVM Stack 영역과 비슷하게 LIFO를 구현하는 자료구조이며, Java 메서드가 아닌 네이티브 메서드들의 호출 순서를 담아 관리하는 stack입니다.

---

## References

- https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html
- https://openjdk.java.net/jeps/122
- https://docs.deistercloud.com/content/Axional%20development%20libraries.20/Studio%20J2EE%20Server.4/Tunning.xml?embedded=true
- https://www.baeldung.com/java-stack-heap
- https://medium.com/platform-engineer/understanding-java-memory-model-1d0863f6d973
- https://stackoverflow.com/questions/41358895/permgen-is-part-of-heap-or-not
