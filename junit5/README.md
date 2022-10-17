# junit 5

- public 불필요
- @BeforeAll, @AfterAll, @BeforeEach, @AfterEach
- @DisplayName('테스트 명')
- assertEquals(a, b, message);
  - 여기서 메세지에 연산이 들어간다던지 하면 성능개선 차원에서 람다식으로 넘기면 됨. 해당 자리에 supplier가 들어감.

---

- 예외 발생 테스트

```java
@Test
void 중복_회원_예외() throws Exception {
    // given
    Member member1 = new Member();
    member1.setName("kim");

    Member member2 = new Member();
    member2.setName("kim");

    // when
    memberService.join(member1);

    // then
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
}
```
