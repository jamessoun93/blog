# Final

Java에서 final 키워드를 사용하는 방법

- final class 선언
- final method 선언
- final variable 선언

## 1. Final Class 선언

클래스를 final로 선언하면 다른 클래스가 final로 선언한 클래스의 하위 클래스가 되는 것을 방지합니다.  
final로 선언한 클래스를 상속받아 확장할 수 없다는 뜻입니다.

특정 클래스를 상속받는 하위 클래스가 생기면 안되거나, 절대 그럴일이 없다고 판단될 때 final class로 선언해줄 수 있습니다.

```java
// final 키워드 추가
public final class Animal {}

// BAD: 상속 불가능
public class Dog extends Animal {}
```

## 2. Final Method 선언

메소드를 final로 선언하면 해당 final 메소드가 선언되어있는 클래스를 상속받은 하위 클래스에서 해당 메소드를 override하는 것을 방지합니다.

부모 클래스에서 특정 작업을 정해진 방식으로 수행해야 하는 경우, 자식 클래스에서 해당 메소드를 override하여 다른 방식으로 구현하는 것을 원치 않기때문에 final 키워드를 사용하여 메소드를 선언하여 방지할 수 있습니다.

```java
public class Animal {
  ...
  public final void eat() {
    System.out.println("냠냠");
  }
}

public class Dog extends Animal {

  // BAD: override 불가능
  public void eat() {
    System.out.println("nom nom");
  }
}
```

## 3. Final Variable 선언

변수를 final로 선언하면

```java

```
