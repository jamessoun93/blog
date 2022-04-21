# Queues

## Implementation (Java)

using ArrayList

```java
import java.util.ArrayList;

public class MyQueue<T> {
    private ArrayList<T> queue = new ArrayList<T>();

    public void enqueue(T item) {
        queue.add(item);
    }

    public T dequeue() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public static void main(String[] args) {
        MyQueue<Integer> mq = new MyQueue<Integer>();
        mq.enqueue(1);
        mq.enqueue(2);
        mq.enqueue(3);
        System.out.println(mq.dequeue());
        System.out.println(mq.dequeue());
        System.out.println(mq.dequeue());
    }
}
```

using Queue

```java
import java.util.LinkedList;
import java.util.Queue;

Queue<Integer> queue_int = new LinkedList<Integer>();
Queue<String> queue_str = new LinkedList<String>();

// 데이터 추가는 add(value) 또는 offer(value) 를 사용함
queue_int.add(1);
queue_int.offer(2);
// 출력에 true 라고 출력되는 부분은 offer() 메서드가 리턴한 값으로,
// 셀의 맨 마지막에 함수를 넣을 경우, 변수가 변수값이 출력되는 것처럼 함수는 함수 리턴값이 출력되는 것임
System.out.println(queue_int); // Queue 인스턴스를 출력하면, 해당 큐에 들어 있는 아이템 리스트가 출력됨
queue_int.poll(); // poll() 은 큐의 첫 번째 값 반환, 해당 값은 큐에서 삭제
queue_int.remove(); // poll() 과 마찬가지로, 첫 번째 값 반환하고, 해당 값은 큐에서 삭제
queue_int.clear(); // queue 초기화
```
