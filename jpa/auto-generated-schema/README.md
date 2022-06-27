# JPA: 데이터베이스 스키마 자동 생성 기능

- DDL을 애플리케이션 실행 시점에 자동 생성해줌.
- 개발환경에서만 사용 권장.
- 운영서버에서는 사용하지 않거나, 적절히 다듬은 후 사용해야함.
- 데이터베이스 방언을 활용해서 데이터베이스에 맞는 적절한 DDL 생성 (VARCHAR, VARCHAR2 이런거)

```java
<property name="hibernate.hbm2ddl.auto" value="create" />
```

속성

- create: DROP + CREATE
- create-drop: create와 같으나 종료 시점에 DROP
- update: 변경분만 반영 (운영 DB 사용 X, 지우는 건 안되고 업데이트만 가능)
- validate: 엔티티와 테이블이 정상 매핑되었는 확인
- none: 사용하지 않음

주의할 점은 운영 장비에는 절대 create, create-drop, update를 사용하면 안된다.

- 개발 초기 단계에는 create 또는 update
- 테스트 서버는 update 또는 validate
- staging, prod 서버는 validate 또는 none
- 그래도 되도록이면 테스트 서버 및 개발 서버에서도 쓰지 않는게 권장사항
- 만들어주는 sql문 정도만 검수 꼼꼼히해서 활용하는게 좋은 방법
