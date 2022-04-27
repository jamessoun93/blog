# 객체의 불변성(Immutability)은 왜 중요할까? Java에서 객체를 Immutable하게 쓰는 방법은?

[Tistory 블로그 포스팅 바로가기](https://seunghyunson.tistory.com/24)

## 객체의 불변성(Immutability)이란?

객체가 immutable 하다는 것은 객체가 최초 생성된 시점 이후 상태 값이 변하지 않는다는 뜻입니다.

객체를 처음 만들었을 때의 속성을 해당 객체가 사라질 때까지 그대로 유지한다는 뜻이죠.

## 객체의 불변성이 왜 중요할까? 어떤 장단점이 있을까?

그렇다면 객체의 불변성이 왜 중요할까요?

이펙티브 자바(Effective Java)에서는 특별한 이유가 있지 않은 이상 클래스는 immutable 해야 한다고 합니다.

> Classes should be immutable unless there's a very good reason to make them mutable....If a class cannot be made immutable, limit its mutability as much as possible.

여러 가지 장점들이 있기 때문인데요.

객체를 immutable 하게 생성한다면 생성 시점 이후 해당 객체의 상태를 변경할 수 없으므로, 실행 중인 쓰레드 간 서로의 간섭에 의해 생길 수 있는 동기화(synchronization) 문제에 대한 걱정을 안 해도 되게끔 해줍니다. (이것을 바로 Thread safe 하다고 합니다.)

이런 이유 때문에 동시성이 중요한 프로그램에서 특히 유용합니다.

불변 객체들을 활용하면 side-effect에 대한 걱정이 없기 때문에 설계, 구현, 및 사용하는데 편리하다는 장점이 있습니다.

프로그램 실행 간 exception이 발생하더라도 사용했던 객체들의 상태값은 변함이 없고, 이런 장점 때문에 캐시 해두고 사용한다면 효율을 극대화할 수 있게 됩니다.

하지만 Immutability를 보장하는 방법에는 장점들만 있는 것은 아닙니다.

모든 객체의 불변성을 보장하게 된다면, 상태 변화가 필요한 경우 새로운 객체를 생성해야 한다는 단점이 있고, 새로운 객체를 많이 생성하는 경우 성능 문제가 발생할 수 있다는 걱정을 하는 의견들도 꽤나 많습니다.

하지만 Oracle에 의하면, 객체 생성 비용에 대한 영향은 종종 과대평가되며, 불변 객체를 활용할 때의 이점들이 이런 단점을 상쇄시킨다고 합니다.

> The impact of object creation is often overestimated, and can be offset by some of the efficiencies associated with immutable objects. These include decreased overhead due to garbage collection, and the elimination of code needed to protect mutable objects from corruption.

## 객체를 Immutable하게 쓰는 방법은?

객체를 생성할 때 불변성을 지켜 생성될 수 있게 클래스를 만드는 방법에는 무엇이 있을까요?

### 1. `setter` 메소드를 제공하지 않는 방법

객체의 상태값을 변경하는 `setter` 메소드를 아예 만들지 않는 방법입니다.

IDE로 개발을 하다 보면 `getter`와 `setter` 메소드를 모든 클래스에 제공하는 경우가 있는데,  
상태 값을 변경할 필요가 없는 클래스에 `setter` 메소드를 추가하거나, 상태 값을 가져올 필요가 없는데 `getter` 메소드를 추가하는 건 불필요한 코드를 추가로 작성하는 꼴이 됩니다.

여기서는 상태값을 변경하지 않기 위해 `setter` 메소드를 제공하지 않는 방법을 사용할 수 있습니다.

### 2. final class로 선언

클래스 자체를 `final`로 선언하면 해당 클래스를 다른 클래스에서 상속받는 게 불가능하기 때문에, 부모 클래스에 선언되어 있는 메소드 오버라이딩이 불가합니다.

하지만 상속이 불가능하다는 것 뿐이지 해당 클래스의 객체들이 immutability를 보장하지는 않습니다.

선언한 final class에 1번 `setter` 메소드를 제공하지 않는 방법을 함께 사용해야 `setter`를 사용한 상태값 수정을 막을 수 있습니다.

### 3. 모든 mutable 필드를 final로 선언

Java에서 변수를 final로 선언하면 해당 변수를 초기화할 때 할당된 값을 변경할 수 없게 됩니다.

인스턴스 변수가 primitive type이라면 final로 선언하여 불변성을 유지할 수 있습니다.

하지만 인스턴스 변수가 특정 객체를 참조하는 참조 변수라면, 참조 대상이 바뀔 수 없다는 뜻일 뿐 참조하고 있는 객체 자체의 불변성을 보장할 수는 없습니다.

이런 경우, 참조하고 있는 객체 또한 immutable 하게 사용할 수 있게끔 처리를 해주어야 합니다.

### 4. 모든 필드의 접근제어자를 private으로 선언

클래스의 모든 필드를 private으로 선언하면 해당 클래스만 해당 필드에 대한 접근권한을 가지게 됩니다.

클래스에 setter 메소드가 없다면 private으로 선언해 외부 클래스로부터의 접근을 차단할 수 있습니다.

### 5. 생성자를 통해 초기화되는 필드들은 깊은 복사를 통한 참조 대상 재할당

생성자를 통해 초기화되는 인스턴스 변수들이 reference 변수라면 깊은 복사를 통해 참조하는 객체 내부의 값이 변하는 것을 방지할 수 있습니다.

### 6. getter 메소드는 객체의 깊은 복사본을 반환

getter 메소드는 실제 객체에 대한 reference를 반환하는 대신 깊은 복사를 통해 생성한 객체에 대한 reference를 반환하여, 반환받은 객체를 사용할 때 실수로라도 기존 객체를 건드릴 일이 없게끔 만들어줄 수 있습니다.

---

## References

- https://docs.oracle.com/javase/tutorial/essential/concurrency/immutable.html
- https://www.geeksforgeeks.org/create-immutable-class-java/
