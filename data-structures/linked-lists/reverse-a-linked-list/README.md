# Reverse a Linked List

기존 구현했던 Singly Linked List 코드에 `reverse` 메소드 추가

## Implementation (JavaScript)
```js
class Node {
  constructor(value) {
    this.value = value
    this.next = null
  }
}

class LinkedList {
  constructor(value) {
    this.head = new Node(value)
    this.tail = this.head
    this.length = 1
  }
  
  append(value) {
    const newNode = new Node(value)
    
    this.tail.next = newNode
    this.tail = newNode
    this.length++
    
    return this
  }
  
  prepend(value) {
    const newNode = new Node(value)
    
    newNode.next = this.head
    this.head = newNode
    this.length++
    
    return this
  }
  
  insert(index, value) {
    // prepend if index is less or equal to 0
    if (index <= 0) {
      return this.prepend(value)
    }
    
    // append if index is bigger than this.length
    if (index >= this.length) {
      return this.append(value)
    }
    
    const newNode = new Node(value)
    
    const leader = this.traverseToIndex(index - 1)
    const holdingPointer = leader.next
    
    leader.next = newNode
    newNode.next = holdingPointer
    
    this.length++
    
    return this
  }
  
  remove(index) {
    const leader = this.traverseToIndex(index - 1)
    const nodeToRemove = leader.next
    
    leader.next = nodeToRemove.next
    
    this.length--
    
    return this
  }
  
  traverseToIndex(index) {
    let counter = 0
    let currentNode = this.head
    
    while (counter !== index) {
      currentNode = currentNode.next
      counter++
    }
    
    return currentNode
  }
  
  printList() {
    const array = []
    let currentNode = this.head
    while (currentNode !== null) {
      array.push(currentNode.value)
      currentNode = currentNode.next
    }
    
    return array
  }

  reverse() {
    if (!this.head.next) {
      return this
    }
    
    let first = this.head
    this.tail = this.head
    
    let second = first.next
    
    while (second) {
      const tmp = second.next
      second.next = first
      first = second
      second = tmp
    }
    
    this.head.next = null
    this.head = first
    
    return this
  }
}

const myLinkedList = new LinkedList(10)
```