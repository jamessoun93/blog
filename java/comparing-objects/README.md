# 자바 객체 비교

같은 내용의 String을 각각 다른 변수에 담아 같은지 비교를 해보자.

```java
public void compare() {
    String text = "Check value";
    String text2 = "Check value";

    if (text == text2) {
        System.out.println("같음");
    } else {
        System.out.println("다름");
    }

    if (text.equals("Check value")) {
        System.out.println("같음");
    }
}
```

결과는 둘다 "같음" 이다.

결과가 이렇게 나오는 이유는 자바에는 Constant Pool 이라는게 존재하기 때문이다.  
동일한 값을 갖는 객체가 있으면 이전에 이미 만들어놓은 객체를 사용한다.  
그래서 text와 text2는 사실상 같은 객체다.

두 객체의 내용이 같은지를 비교하기 위해서는 equals()를 사용해야한다.
