# Thread Safety란? Thread Unsafe한 경우는?

Java는 하나의 어플리케이션을 실행하기 위해 다수의 worker thread들이 동시에 class bytecode를 실행하는 멀티쓰레디드 언어입니다.

동시에 실행되는 쓰레드들은 주로 필드와 참조하는 객체에 대한 접근을 공유하면서 전체적으로 굉장히 효율적인 구조를 띄고 있지만, 쓰레드간 간섭으로 인한 메모리 일관성 문제가 발생할 수 있습니다.

JVM의 Runtime Data Area를 살펴보면, 프로세스의 각 쓰레드들은 독립적인 PC Register, Stack, Native Method Stack 영역을 가지지만, Heap 영역과 데이터 영역을 서로 공유하기 때문에 쓰레드들이 동시에 공유되고 있는 자원에 접근해서 예상치 못한 결과가 발생할 수 있습니다.

이런 경우를 Thread Safe 하지 않다고 합니다.

## Thread Interference & Memory Consistency Errors

아래 `Counter` 클래스를 살펴보겠습니다.

```java
class Counter {
    private int count = 0;

    public void increment() {
        count++;
    }

    public void decrement() {
        count--;
    }

    public int value() {
        return count;
    }
}
```

만약 여러 쓰레드에서 `Counter` 객체를 참조하는 경우, 서로의 간섭으로 인해 `count`의 값이 무엇이 될지 예상할 수 없는 문제가 발생할 수 있습니다.

`increment()` 함수가 하는 일을 아래와 같이 리스트업할 수 있습니다.

1. `count`의 현재값을 가져온다.
2. 해당 값에 `1`만큼 더한다.
3. 결과값을 `count` 변수에 할당한다.

쓰레드 하나가 위 세 작업을 처리한다면 아무 문제가 없습니다.

하지만 쓰레드 A, B가 동시에 실행된다면 이야기가 다릅니다.

만약 쓰레드 A는 `increment()` 메소드를 실행하고, 동시에 쓰레드 B는 `decrement()` 메소드를 실행한다고 가정해보겠습니다.

1. Thread A: `count`의 현재값을 가져온다. (count: 0)
2. Thread B: `count`의 현재값을 가져온다. (count: 0)
3. Thread A: 해당 값에 `1`만큼 더한다. (count: 0)
4. Thread B: 해당 값에서 `1`만큼 뺀다. (count: 0)
5. Thread A: 결과값을 `count` 변수에 재할당한다. (기대하는 count 값: 1)
6. Thread B: 결과값을 `count` 변수에 재할당한다. (기대하는 count 값: -1)

쓰레드 두개가 동시에 실행되는데도 위처럼 순서가 있게 실행되는 이유는 바로 CPU가 작동하는 방식 때문입니다.

CPU Core가 1개라고 가정했을 때 쓰레드 A, B가 정말 동시에 따로 실행되는 것이 아닌, context switching을 통해 쓰레드 간 왔다갔다 하면서 명령이 실행되기 때문입니다.

그래서 위 예시에서는 매 step마다 context switching이 일어났다고 가정합니다.

이런 상황을 **Race Condition** 이라고도 하며, 여러 프로세스 혹은 여러 쓰레드 간 상대적인 타이밍 혹은 interleaving에 의해 프로그램의 수행 결과가 달라질 수 있는 condition을 말합니다.

쓰레드 A와 B가 서로 본인의 명령을 먼저 수행하려고 경주하는 것과 같습니다.

(여기서부터 내용 조금 보완 필요)

만약 모든 명령이 같은 쓰레드에서 실행되었다면 바로 앞 명령에서 업데이트된 결과를 확인하고 현재 명령을 실행할 수 있겠지만, 별도의 쓰레드에서 실행되기 때문에 쓰레드 A의 변경사항이 쓰레드 B에게 보인다는 보장이 없습니다.

그래서 3~6번 명령을 거치는 동안, 쓰레드 A, B가 서로 0을 기준으로 명령을 처리하게 된 것이죠.

(여기까지 내용 조금 보완 필요)

만약 위 순서대로 정말 쓰레드 B의 마지막 명령인 6번 명령이 가장 늦게 실행되었다면, 쓰레드 A의 결과값은 쓰레드 B의 결과값으로 인해 덮어씌워졌을 것입니다.

결국 쓰레드 간 간섭으로 인해 제대로 된 결과가 나올지 안나올지 모른다는 문제가 있고, 이런 버그가 발생한다면 찾기도 힘들고 고치기도 어려워질 수 있게 됩니다.

---

이런 이유들 때문에 데이터 일관성이 중요한 어플리케이션의 경우, Thread Safe한 코드를 작성하는게 매우 중요합니다.

Thread Safe 하다는 것은, 여러개의 쓰레드로부터 동시에 같은 공유 자원에 대한 접근이 이루어져도 데이터 일관성이 보장되고 프로그램 실행에 문제가 없다는 뜻입니다.

Thread Safety를 보장하는 방법이 여러가지 있꼬

## References

- https://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html
- https://www.baeldung.com/java-thread-safety
- https://www.programmersought.net/en/article/324098292.html
