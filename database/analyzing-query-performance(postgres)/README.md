# 쿼리 성능 분석하기 (PostgreSQL)

(INTRO)

(예제로 쓸 SQL 쿼리문)
```sql
SELECT *
FROM users
WHERE username = 'jamessoun93';
```
# Query Processing Pipeline
**Query Processing Pipeline**이란 우리가 작성한 SQL 쿼리문을 실행한 뒤 결과를 볼 때까지 Postgres 내부에서 거치는 과정들입니다.

쿼리 성능을 분석하기 전에 내부 동작 과정을 간단하게라도 파악하고 넘어가면 분석 단계에서 더 많은 도움이 되겠죠?

## Parser (Stage)
Parser Stage는 제공된 SQL문의 단어 하나하나를 확인하여 올바른 문법으로 작성되었는지 확인하는 단계이고 그 과정은 아래와 같습니다.

### 1. 먼저 제공된 SQL문을 tree 형태로 만듭니다.

위에서 작성한 SQL문을 읽어들여 컴퓨터 프로그램이 이해할 수 있는 논리적 단계 형태로 만드는 작업입니다.

예를 들면,
* 가장 먼저 보이는 `SELECT`문은 트리 상단에 `"SelectStmt"` 그리고 `*` 즉 모든 컬럼을 뜻하는 `"ColumnRef"`, `"A_Star"`의 형태로 변환됩니다.
* `FROM`은 `"fromClause"`, 테이블명인 `users`는 **Postgres**에서 테이블명(relation명)을 뜻하는 `relname`을 이용하여 `"relname"=>"users"`의 형태로 변환됩니다.
* `WHERE`도 마찬가지로 `"whereClause"`, `"str" => "="`, `"columnRef"`, `"str" => "username"`, `"str" => "jamessoun93"`의 형태로 변환됩니다.

(실제 Parser Stage의 결과 전체가 아닌 이해할 수 있는 부분들만 가져온 것이니 참고해주세요.)

## Rewriter
(View 부분)

## Planner
Planner는 앞 스테이지에서 넘겨받은 쿼리 트리를 통해 어떤 데이터를 가져와야 하는지 확인한 뒤, 해당 데이터를 가져오기 위한 여러가지 방법 중 가장 빠르고 효율적인 방법을 파악합니다.

**index**를 활용하여 포인터가 가리키는 `user` 데이터를 `users` Heap File에서 가져오거나, 모든 `users` 테이블 데이터를 불러와 하나하나 확인하여 찾는 방법 두 가지 중 더 빠른 방법을 택해 다음 단계인 **Executer**로 넘긴다는 뜻입니다.

## Executer
전체 과정의 마지막인 실행 단계로, 플래너로부터 넘겨받은 방법으로 쿼리를 실행한 뒤 데이터를 받아오는 단계입니다.

---

# 쿼리 성능 파악하기

위의 Query Processing Pipeline 중 우리가 집중해야할 부분은 Planner 단계이고, 이 단계를 분석하여 슬로우 쿼리를 파악하고 튜닝할 수 있습니다.

먼저, 쿼리 성능 분석의 기초인 키워드 두개를 소개드리겠습니다.

1. `EXPLAIN`
2. `EXPLAIN ANALYZE`

### EXPLAIN
`EXPLAIN`은 query plan을 생성해 여러가지 정보를 제공합니다.  
하지만 실제로 EXPLAIN 뒤에 오는 쿼리를 **실행하지는 않습니다.**

### EXPLAIN ANALYZE
`EXPLAIN ANALYZE`도 마찬가지로 query plan을 생성하여 여러가지 정보를 제공합니다.  
하지만 `EXPLAIN`과는 다르게 **실제 쿼리를 호출한 뒤 쿼리를 실행하는데 걸린 시간 등을 포함한 통계를 제공합니다.**
