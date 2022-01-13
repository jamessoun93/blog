# Stacks

- [Stacks](#stacks)
  - [Implementation using Linked List (JavaScript)](#implementation-using-linked-list-javascript)
  - [Implementation using Arrays (JavaScript)](#implementation-using-arrays-javascript)

## Implementation using Linked List (JavaScript)
```js
class Node {
  constructor(value) {
    this.value = value
    this.next = null
  }
}

class Stack {
  constructor() {
    this.top = null
    this.bottom = null
    this.length = 0
  }
  
  peek() {
    return this.top
  }
  
  push(value) {
    const newNode = new Node(value)
    
    if (this.length === 0) {
      this.top = newNode
      this.bottom = newNode
    } else {
      newNode.next = this.top
      this.top = newNode
    }
    
    this.length++
    
    return this
  }
  
  pop() {
    if (!this.top) {
      return null
    }
    
    if (this.top === this.bottom) {
      this.bottom = null
    }
    
    const holdingPointer = this.top
    this.top = this.top.next
    this.length--
    
    return holdingPointer
  }
}

const myStack = new Stack()
```

---

## Implementation using Arrays (JavaScript)
```js
class Stack {
  constructor() {
    this.array = []
  }
  
  peek() {
    return this.array[this.array.length - 1]
  }
  
  push(value) {
    this.array.push(value)
    
    return this
  }
  
  pop() {
    const poppedValue = this.array.pop()
    
    return poppedValue
  }
}

const myStack = new Stack()
```