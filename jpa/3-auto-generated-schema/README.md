# [JPA #3] 데이터베이스 스키마 자동 생성 기능

[Tistory 블로그 포스팅 바로가기](https://seunghyunson.tistory.com/31)

JPA에는 데이터베이스 스키마를 자동으로 생성해주는 기능이 존재합니다.

이 기능은 DDL을 애플리케이션 실행 시점에 자동 생성해줍니다.

당연히 개발환경 에서만 사용이 권장되는 기능이고, 운영환경 에서는 사용하지 않거나 필요에 따라 적절하게 다듬은 뒤 사용해야 합니다.

DB 스키마 자동 생성 기능은 데이터베이스 방언을 활용해서 사용하는 데이터베이스에 맞는 적절한 DDL을 생성해줍니다.

---

## hibernate.hbm2ddl.auto 속성

- create: DROP + CREATE
- create-drop: create와 같으나 종료 시점에 DROP
- update: 변경분만 반영 (운영 DB 사용 X, 지우는 건 안되고 업데이트만 가능)
- validate: 엔티티와 테이블이 정상 매핑되었는 확인
- none: 사용하지 않음

해당 속성들을 필요에 따라 아래와 같이 사용할 수 있습니다.

```java
<property name="hibernate.hbm2ddl.auto" value="create" />
```

주의할 점은 운영 장비에는 절대 create, create-drop, update를 사용하면 안됩니다.

- 개발 초기 단계에는 create 또는 update
- 테스트 서버는 update 또는 validate
- staging, prod 서버는 validate 또는 none

되도록이면 테스트 서버 및 개발 서버에서도 쓰지 않는게 권장사항입니다.

만들어주는 SQL문 정도만 꼼꼼히 검수해서 활용하는게 좋은 방법입니다.

---

JPA의 데이터베이스 스키마 자동 생성 기능에 대한 정리를 마치며, 이 기능을 운영환경에서 사용하면 안되는 이유를 실제 사례를 통해 설명해주는 재미있는 유투브 영상이 있어 소개합니다.

제가 정말 좋아하는 개발바닥 채널의 영상인데요.

진행자 중 한분이신 호돌맨님이 배민에 계실 때 실수로 일으켰던 역대 탑3 장애라고 하는데 굉장히 재미있게 봤습니다.

👉 [재난급 서버 장애내고 개발자 인생 끝날뻔 한 썰 - 납량특집! DB에 테이블이 어디로 갔지?](https://www.youtube.com/watch?v=SWZcrdmmLEU)
