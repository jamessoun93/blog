# Best Practices

### 기본적으로 모든 데이터 변경 및 로직들은 가급적 transaction 안에서 실행되어야해서 서비스 클래스에서 `@Transactional` 애노테이션을 활용하자.

- 예시 (조회하는 부분들은 readOnly 속성을 추가하여 성능 부분에서 이점을 얻을 수 있기에 추가)

```java
@Service
@Transactional(readOnly = true)
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> members = memberRepository.findByName(member.getName());
        if (!members.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
```

### 테스트 시 실제 외부 DB를 이용하지 말고 h2를 활용한 인메모리 DB를 활용하자.

- `runtimeOnly 'com.h2database:h2'` 필요
- 테스트 시 connection url이 `jdbc:h2:mem` 로 잡히는 것을 확인할 수 있음.
- `test` -> `resources` 추가 -> test 용 `application.yml` 추가

### 비즈니스 로직을 데이터를 가지고 있는 쪽에 두자. (객체지향적으로 접근)

- 핵심 비즈니스 로직을 엔티티에 직접 넣는 방법.
- 비즈니스 로직이 대부분 엔티티에 있게 되면서 서비스 레이어가 깔끔해짐.
- 즉, 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 함.
- 이렇게 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 **도메인 모델 패턴** 이라고 함.
- 엔티티에 핵심 비즈니스 로직들이 모여있기 때문에 더 철저한 unit test도 가능하게 함.
- 반대로 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 **트랜잭션 스크립트 패턴** 이라고 함.

### 서비스 계층에서 필요한 엔티티 객체의 식별자만 컨트롤러에서 넘겨주고 서비스 레이어에서 객체를 불러오자. (merge 대신 dirty checking 사용)

- 컨트롤러에서 엔티티 객체를 찾아 서비스로 넘겨주게 되면 트랜잭션이 진행되는 동안의 호출이 아니기 때문에 영속 상태가 깨짐.
- 영속 상태가 깨지면 바뀌는 값에 대한 처리가 자동으로 일어나지 않음.
- 그래서 컨트롤러에서는 식별자만 넘겨주고, 서비스에서 repository를 통해 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경.
- 트랜잭션 커밋 시점에 변경 감지가 실행됨.
