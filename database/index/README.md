# Index란 무엇일까?

1. Full Table Scan
2. Index
3. Index 생성 방법
4. 비교
5. Index 단점
6. 자동 생성 Indexes

Index가 뭔지 알아보기 전에 Full Table Scan에 대해서 알아보겠습니다.

---

## Full Table Scan
```sql
SELECT *
FROM users
WHERE username = 'jamessoun93';
```

위 쿼리는 `users` 테이블 내 존재하는 모든 데이터 중 `username` 컬럼값이 `jamessoun93`인 데이터를 반환하는 쿼리입니다.

이런 쿼리를 실행할 때 Postgres 내부에서는 어떤일이 일어날까요?

제가 이전에 작성한 [Postgres는 데이터를 어디에 어떤 형태로 저장할까?](https://seunghyunson.tistory.com/16) 하단의 **Postgres는 데이터를 어떤 형태로 저장할까?** 파트를 보면, 테이블 하나에 대한 데이터는 하나의 **Heap File**로 저장된다는 것을 알 수 있습니다.

하지만 해당 테이블의 전체 데이터는 여러개의 **Block**으로 그룹지어져 저장되어 있는 형태이기 때문에 Postgres가 바로 **Heap File**에 접근해서 스캔을 시작할 수 없습니다.

그래서 스캔할 대상이 되는 모든 데이터를 메모리에 모아놓고 메모리에서 순차적으로 하나하나 확인을 하면서 원하는 데이터를 찾습니다. (아래 다이어그램 확인)

![1](./images/1.png)

Heap 파일로부터 모든 데이터를 메모리로 옮겨 데이터 하나하나 원하는 조건에 맞는지 확인하는 작업이 바로 **Full Table Scan**입니다.

메모리로 옮겨야할 데이터가 많으면 많을수록 전체적인 비용이 커지고 성능에 영향을 주는 작업입니다.

그러므로 꼭 **Full Table Scan**이 필요한지에 대한 고민이 필요하고 더 성능 효율적인 방법이 존재한다면 다른 방법을 선택할 수 있어야합니다.

---

## Index

```sql
SELECT *
FROM users
WHERE username = 'jamessoun93';
```

그렇다면 위와 같은 쿼리를 날릴 때 매번 메모리에 데이터를 로드한 뒤 Full Table Scan을 하는것이 아니라, 내가 원하는 데이터가 정확하게 어떤 Block의 몇번째 인덱스에 존재하는지 알려줄 수 있는 방법은 없을까요?

**Index**를 활용하면 가능합니다.

![2](./images/2.png)

데이터베이스 Index는 요청한 데이터가 어떤 Block의 몇 번째 인덱스에 존재하는지를 알려주는 자료구조입니다.

인덱스는 내가 찾고자 하는 데이터를 담고 있는 컬럼을 기준으로 생성합니다.  
해당 컬럼값을 찾을 때 속도 효율을 높여주기 위해서죠.