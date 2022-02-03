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
* `WHERE`도 마찬가지로 `"whereClause"`, `"str" => "="`, `"columnRef"`, `"str" => "username"`, `"str" => "Jamessoun93"`의 형태로 변환됩니다.

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

```sql
EXPLAIN SELECT username, contents
FROM users
JOIN comments ON comments.user_id = users.id
WHERE username = 'Jamessoun93';
```

![explain](./images/1.png)

### EXPLAIN ANALYZE
`EXPLAIN ANALYZE`도 마찬가지로 query plan을 생성하여 여러가지 정보를 제공합니다.  
하지만 `EXPLAIN`과는 다르게 **실제 쿼리를 호출한 뒤 쿼리를 실행하는데 걸린 시간 등을 포함한 통계를 제공합니다.**

```sql
EXPLAIN ANALYZE SELECT username, contents
FROM users
JOIN comments ON comments.user_id = users.id
WHERE username = 'Jamessoun93';
```

![explain-analyze](./images/2.png)

## EXPLAIN ANALYZE 결과 분석하기

`EXPLAIN`과 `EXPLAIN ANALYZE`의 결과화면을 보면 몇몇 부분에 `->` 처럼 화살표가 보이는 부분이 있습니다.  
이 부분들이 바로 쿼리 노드(Query Nodes)입니다.

쿼리 노드는 실제 데이터를 가져오기 위해 접근하거나 여러가지 작업을 처리하는 부분입니다.

`->` 표시는 붙어있지 않지만, 가장 위 `Hash Join`문 또한 쿼리 노드입니다.

지금부터 각 쿼리 노드에서 수행하는 작업이 어떤 순서로 진행되는지 알아보겠습니다.

이해를 돕기 위해 [pgAdmin 4](https://www.pgadmin.org/)의 Explain Analyze 툴을 실행하여 나온 결과 이미지를 활용했습니다.

![explain-analysis](./images/3.png)
각 쿼리 노드의 스텝을 알기 쉽게 표시해뒀습니다.

![explain-graphical](./images/4.png)
단순히 결과만 보고는 파악하기 어려운 순서를 이렇게 다이어그램으로 확인하여 이해를 할 수 있습니다.

1. **Index Scan**이 가장 먼저 실행된 후 해당 결과가 **Hash**로 넘어갑니다.  
2. **Hash** 스텝이 실행됨과 동시에 **Seq Scan** 스텝이 실행됩니다.
3. **Hash** 스텝과 **Seq Scan** 스텝의 결과가 함께 **Hash Join** 스텝으로 넘겨지게 됩니다.
4. **Hash Join** 스텝에서 최종 결과를 반환합니다.

첫번째 쿼리 노드인 **Hash Join(Explain Analyze)** 의 결과값을 살펴보겠습니다.

![hash-join-parts](./images/5.png)

해당 노드에서 어떤 방식으로 데이터를 처리하는지 방법이 명시되어있으며 해당 처리 과정 중 필요한 처리 능력이 어느정도 되는지와 예상되는 결과 row수와 row당 평균 byte수도 포함하고 있습니다.

여기서 잠시 **EXPLAIN**의 **Hash Join** 쿼리 노드 결과값도 살펴보겠습니다.

![hash-join-rows-width](./images/6.png)

이상하지 않나요?

**EXPLAIN**은 **EXPLAIN ANALYZE**와 달리 쿼리를 실행하지 않아서 실제 데이터에 접근을 하지 않았을텐데 예상 rows와 width를 어떻게 포함하고 있는걸까요?

**Postgres**가 테이블 내부에 존재하는 데이터에 대한 정보가 없는 상황에서 각 스텝을 실행하는 데 걸리는 시간을 어떻게 예측할 수 있을까요?

그 이유는 바로 **Postgres**가 실제로 각 테이블 대한 상세한 통계를 보관하고 있기 때문입니다.

아래 쿼리를 실행해서 한번 확인해보겠습니다.

```sql
SELECT *
FROM pg_stats
WHERE tablename = 'users';
```

![pg-stats](./images/7.png)

`pg-stats`라는 테이블에서 `users` 테이블에 대한 데이터만 가져온 결과입니다.

이 테이블에는 각 컬럼별로 여러가지 데이터를 종합하고 있습니다.

- 컬럼별 평균 byte 수(`avg_width`)
- 가장 자주 등장하는 값(`most_common_vals`)
- 가장 자주 등장하는 값의 빈도(`most_common_freqs`)

등등 많은 데이터를 저장하고 있습니다.

**Postgres**는 이 테이블 내 데이터를 통해서 **쿼리 계획(Query Plan)**의 각 단계에서 필요한 예상값을 계산할 수 있기 때문에 실제 쿼리 호출을 하지 않더라도 예상값을 반환할 수 있었던 것입니다.

위에서 봤던 **Hash Join**의 평균 width 값은 `users` 테이블의 `username` 컬럼의 평균 width 값인 `13`과 `comments` 테이블의 `contents` 컬럼의 평균 값인 `68`을 더해 `81`이 반환된 것이죠.