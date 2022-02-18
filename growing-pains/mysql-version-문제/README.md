# MySQL Version 문제

## 배경
현재 사내에서 Internal API 개발을 메인으로 하고있고 데이터베이스는 MySQL 버전 5.7을 사용하고 있습니다.

최근 트랜잭션 중간에 registration_token(UUID)을 발급해주는 기능을 추가했었고 모든 리뷰과정과 테스트과정을 거쳐 머지가 된 상태입니다.

그런데 팀원 한분이 pull 받은 뒤 로컬 환경에서 유닛테스트가 실패한다는 문제를 공유해주셨습니다.

`registration_tokens` 테이블에 `expiration_date(토큰만료날짜)` 컬럼에 DEFAULT가 존재하지 않아 INSERT문이 정상적으로 처리되지 않아 트랜잭션이 롤백되는 문제였습니다.

## 해결 (수정 필요)
그래서 마이그레이션 파일을 찾아보니 `registration_tokens` 테이블의 `expiration_date` 컬럼이 `migration` 파일 상 `expiration_date TIMESTAMP NOT NULL` 로 지정되어 `DEFAULT`가 없이 생성됌.
- 그런데도 제 로컬 mysql에는 DEFAULT가 `CURRENT_TIMESTAMP`로 생성되어 있었음.
- 알고보니 5.7 버전과 8버전 간 [explicit_defaults_for_timestamp](https://dev.mysql.com/doc/refman/5.7/en/server-system-variables.html#sysvar_explicit_defaults_for_timestamp) 옵션의 차이가 있었음.