# JPA: 지연 로딩 (Lazy Loading)

## 지연 로딩 LAZY를 사용해서 프록시로 조회

```java
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY) // <--
    @JoinColumn(name = "team_id")
    private Team team;

    // getters & setters
}
```

위와 같이 Member와 Team 엔티티가 다대일 관계라고 할 때 