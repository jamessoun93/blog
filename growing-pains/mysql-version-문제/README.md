# MySQL Version 문제

## 문제
현재 사내 데이터베이스는 mysql 5.7버전을 사용하고 있고 나 역시 로컬에서 같은 버전을 맞춰 사용하고 있음.

`registration_tokens` 테이블의 `expiration_date` 컬럼이 `migration` 파일 상 `expiration_date TIMESTAMP NOT NULL` 로 지정되어 `DEFAULT`가 없이 생성됌.

그런데도 로컬 mysql에는 DEFAULT가 `CURRENT_TIMESTAMP`로 생성되어 있었음.

그래서 NOT NULL 인데도 Internal API 뮤테이션에서 expiration_date 없이 INSERT를 날려도 아무 문제가 없었음.

그러다 로컬에서 mysql 8버전을 사용하고 있는 팀원의 유닛테스트에서 해당 부분 문제가 발생함.


## 해결 (수정 필요)
알고보니 5.7 버전과 8버전 간 [explicit_defaults_for_timestamp](https://dev.mysql.com/doc/refman/5.7/en/server-system-variables.html#sysvar_explicit_defaults_for_timestamp) 옵션의 차이가 있었음.