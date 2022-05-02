# 예외 처리할 때 특히 신경 써야 할 부분 (자원 관리)

어떠한 오류가 이런 걸 발생했을 때 익셉션을 발생시 오류로 인해서 프로그램 종료가 될 수도 있지만 익셉션을 발생시켜가지고 사용자가 알릴 수 있는 기능

finally는 try에서 끝나던 catch에서 끝나던 무조건 실행되는 것들.

exception을 처리할 때 자원을 항상 신경써야한다.

자원?

트라이 캐치하고 그냥 끝나버리면 어플리케이션이 그냥 죽어버릴 수 있다. 트라이 캐치에 특정 자원 사용 종료에 대한 정의를 안하면 무조건 가지고 있게됌.

접근을 해야하는데 못하는 상황. 리소스를 재사용해야하는데 재사용을 못하는 케이스가 발생함.

그래서 무조건 try catch를 쓸때는 꼭 close를 해줘야하고 보통 finally에서 해주게 됌.

JDBC라던지 네트워크 통신 이런거 다 close 해줘야하지만 대부분 안하는 경우가 많다.

어플리케이션이 내려갈 때 특히나 웹 애플리케이션 같은 경우에는 WAS에서 종료가 될 때 가끔 가다가 당진 jdbc 리소스를 close를 안 해서 내가 close 하겠다 라는 메시지가 나올 때가 가끔 있어요.

JDBC의 경우 WAS에서 알아서 해주니까 리소스 leak에 대해서는 걱정을 안해도 되지만, 파일 같은건 리소스 leak이 발생할 가능성이 있다.

###########
JDBC connection leak 관련 부분 잘 이해를 못함 아직
###########

```java
Connection conn = new Connection(); // 1번
conn.getConnection();

// 변수를 재사용한다고 새로운 DB 커넥션을 재할당
conn = new Connection(); // 2번
conn.getConnection();

conn.close();

// 2번 작업으로 인해 1번의 레퍼런스에 더이상 접근할 방법이 없고 자원을 반납할 방법이 없고, 이는 resource leak이 발생하는 예시다.
```

# try-with-resources

- 자바 7에서 공개됨.
- try 다음의 소괄호에 자원을 할당하고 이는 try - resources문이 종료되면 자동 반환한다.
- 이때 소괄호 안에 들어가는 자원은 꼭 AutoClosable(마커인터페이스)를 구현해야한다.
- try catch에서 자원반납을 까먹는 경우가 있는데 이를 사용하면 무조건 반환하니 사용하는 것도 좋은 선택이 될 것임.

---

## References

- https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
