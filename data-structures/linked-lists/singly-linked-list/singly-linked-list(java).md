# Singly Linked List

Singly Linked List 구현 코드

## Implementation (Java)

```java
public class Node<T> {
    T data;
    Node<T> next = null;

    public Node(T data) {
        this.data = data;
    }
}

Node<Integer> node1 = new Node<Integer>(1);
Node<Integer> node2 = new Node<Integer>(2);

node1.next = node2;
Node head = node1;
```

```java
public class SinglyLinkedList<T> {
    public Node<T> head = null;

    public class Node(T data) {
        this.data = data;
    }

    public void addNode(T data) {
        if (head == null) {
            head = new Node<T>(data);
        } else {
            Node<T> node = this.head;
            while(node.next != null) {
                node = node.next;
            }
            node.next = new Node<T>(data);
        }
    }

    public void printAll() {
        if (head != null) {
            Node<T> node = this.head;
            System.out.println(node.data);
            while(node.next != null) {
                node = node.next;
                System.out.println(node.data);
            }
        }
    }

    public void addNodeInBtw(T data, T prevData) {
        Node<T> prevNode = this.search(prevData);

    }


}

SinglyLinkedList<Integer> sll = new SinglyLinkedList<Integer>();
sll.addNode(1);
sll.addNode(2);
sll.addNode(3);

sll.printAll();
```
