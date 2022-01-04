# INTERVAL

서버개발을 하면서 날짜 혹은 시간 계산이 필요한 경우가 많았습니다. 

Node.js 서버에서는 [Moment.js](https://momentjs.com/)라는 라이브러리를 자주 활용했고, Python에서는 자체 제공 [datetime](https://docs.python.org/ko/3/library/datetime.html)이라는 내장 모듈을 활용하곤 했습니다.

그러다가 날짜/시간 계산을 데이터베이스 쿼리문으로 처리할 수 없을까하는 궁금증이 생겨 알아보다가 `INTERVAL`이라는 데이터타입에 대해 알게되었습니다.

`1 day` => `1 day`

`1 D` => `1 day`

`1 D 1 M 1 S` => `1 day 1 minute 1 second`

Think of as duration of time

```sql
SELECT ('1 D'::INTERVAL);
```

```sql
SELECT ('1 D 20 H 30 M 45 S'::INTERVAL);
```

We can actually do numeric operations on intervals.

Subtraction on INTERVALS
```sql
SELECT ('1 D 20 H 30 M 45 S'::INTERVAL) - ('1 D'::INTERVAL);
```

|id|column|
|------|---|
|1|20:30:45|

Add or subtract from time from dates
```sql
SELECT ('NOV-20-1980 1:23 AM EST'::TIMESTAMP WITH TIME ZONE) - ('1 D'::INTERVAL);
```

|id|column|
|------|---|
|1|1980-11-18 23:23:00-07|

```sql
SELECT ('NOV-20-1980 1:23 AM EST'::TIMESTAMP WITH TIME ZONE) - ('4 D'::INTERVAL);
```

|id|column|
|------|---|
|1|1980-11-15 23:23:00-07|

between two timestamps or dates
```sql
SELECT ('NOV-20-1980 1:23 AM EST'::TIMESTAMP WITH TIME ZONE) - ('NOV-10-1980 1:23 AM EST'::TIMESTAMP WITH TIME ZONE);
```

|id|column|
|------|---|
|1|10 days|

between different times and timezones as well
```sql
SELECT ('NOV-20-1980 1:23 AM EST'::TIMESTAMP WITH TIME ZONE) - ('NOV-10-1980 5:43 PST'::TIMESTAMP WITH TIME ZONE);
```

|id|column|
|------|---|
|1|9 days 16:40:00|


We can make use of database to do all these duration calculations instead of having to get some kind of specialized library at your server.