# Stack

## Implementation (Java)

using ArrayList

```java
import java.util.ArrayList;

public class MyStack<T> {
    private ArrayList<T> stack = new ArrayList<T>();

    public void push(T item) {
        stack.add(item);
    }

    public T pop() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.remove(stack.size() - 1);
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        MyStack<Integer> ms = new MyStack<Integer>();
        ms.push(1);
        ms.push(2);
        System.out.println(ms.pop());
        ms.push(3);
        System.out.println(ms.pop());
        System.out.println(ms.pop());
    }
}
```

using Stack class

```java
//  java.util.Stack 클래스 임포트
import java.util.Stack;

// 자료형 매개변수를 넣어서, 스택에 들어갈 데이터의 타입을 지정해야 함
Stack<Integer> stack_int = new Stack<Integer>(); // Integer 형 스택 선언

stack_int.push(1); // Stack 에 1 추가
stack_int.push(2); // Stack 에 2 추가
stack_int.push(3); // Stack 에 3 추가 (출력에 나온 부분은 push() 성공시, 해당 아이템을 리턴해주기 때문임)

stack_int.pop();
stack_int.pop();
stack_int.pop();
```
