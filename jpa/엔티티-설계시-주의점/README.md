# 엔티티 설계시 주의점

### 엔티티에는 가급적 Setter를 사용하지 말자

- Setter가 모두 열려있게 되는 경우 변경 포인트가 너무 많아서 유지보수가 어려운 문제가 있다.

### 모든 연관관계는 지연로딩(`LAZY`)으로 설정 (`EAGER` 금지!)

- 즉시로딩(`EAGER`): 예를 들어 Member와 Order가 1:N의 관계로 매핑되어있다고 가정할 때, Member 조회 시점에 연관된 모든 Order를 다 불러오는 것.
- 즉시로딩의 경우 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할 때 N+1 문제가 자주 발생한다.
- 모든 연관관계는 꼭 지연로딩으로 설정해야 한다.
- `@ManyToOne` 는 default 가 `EAGER`, `@OneToMany` 는 default 가 `LAZY`
- `@ManyToOne` 과 `@OneToOne` 은 `LAZY` 로 꼭 변경해줘야 한다.
