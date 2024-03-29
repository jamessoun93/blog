# Spring: 다양한 의존성 주입 방법

1. 생성자 주입

- 생성자의 호출 시점에 1회 호출 보장.
- 불변, 필수 의존관계에 사용.
- 생성자가 1개만 있을 경우 @Autowired 생략 가능.

2. 수정자 주입(setter 주입)

- 주입받는 객체가 변경될 가능성이 있는 경우에 사용(거의 없음)
- @Autowired로 주입할 대상이 없는 경우 오류 발생.

3. 필드 주입

- 필드에 바로 의존 관계를 주입하는 방법.
- 외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적인 단점이 존재함.
- 예를 들어, 테스트할 때 DB를 호출하는 repository를 사용하지 않고, 메모리를 활용해 dummy 데이터를 넘기는 repository를 사용하고 싶은데 그렇게 할 수 있는 방법이 없음.
- 만약 하려면 setter를 다 만들어줘야 함.
- 문제 파악을 runtime 시에만 확인 가능.
- 사용하면 안되는 방법!

---

## 생성자 주입을 선택해야 하는 이유

불변성

- 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없음. (종료 전까지 변하면 안됨.)
- 수정자 주입을 사용하면, setXxx 메서드를 public으로 열어두어야 함.
- 변경하면 안되는 메서드를 열어두는 것은 좋은 설계 방법이 아님(실수로 변경할 수도 있기 때문).
- 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없음. (불변하게 설계 가능)

누락

- 테스트 코드를 짤 때 (예를 들어 서비스 클래스), 생성자 주입을 이용하면 해당 클래스가 어떤 의존성이 필요한지 컴파일 단계에서 확인할 수 있음.

final 키워드

- final 키워드를 사용할 수 있어서 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에서 확인할 수 있음.
- 오직 생성자 주입 방식만 final 키워드를 활용할 수 있음.

순환 참조를 찾을 수 있음

-
