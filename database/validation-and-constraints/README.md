# Validation & Constraints

(모든 예시 SQL문들은 PostgreSQL 기준임)
<br/>

## NULL Constraint

### 테이블 생성시 설정

```sql
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    department VARCHAR(50),
    price INTEGER NOT NULL,
    weight INTEGER
);
```

### 이미 생성된 테이블에 설정 추가

```sql
ALTER TABLE products
ALTER COLUMN price
SET NOT NULL;
```
<br/>

## Default Column Values

### 테이블 생성시 설정
    
```sql
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    department VARCHAR(50) NOT NULL,
    price INTEGER DEFAULT 999,
    weight INTEGER
);
```
    
### 이미 생성된 테이블에 설정 추가
    
```sql
ALTER TABLE products
ALTER COLUMN price
SET DEFAULT 999;
```
<br/>

## Unique Constraint to One Column

### 테이블 생성시 설정
    
```sql
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE,
    department VARCHAR(50),
    price INTEGER,
    weight INTEGER
);
```
    
### 이미 생성된 테이블에 설정 추가
    
```sql
ALTER TABLE products
ADD UNIQUE (name);
```
<br/>

## Unique Constraint to Multiple Columns

### 테이블 생성시 설정
    
```sql
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    department VARCHAR(50),
    price INTEGER,
    weight INTEGER,
    UNIQUE(name, department)
);
```
    
### 이미 생성된 테이블에 설정 추가
    
```sql
ALTER TABLE products
ADD UNIQUE (name, department);
```
<br/>

## Validation Check

유효성 검사는 작업하고 있는 ROW에서만 작동함.

### 테이블 생성시 설정
    
```sql
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    department VARCHAR(50) NOT NULL,
    price INTEGER CHECK (price > 0),
    weight INTEGER
);
```
    
### 이미 생성된 테이블에 설정 추가
    
```sql
ALTER TABLE products
ADD CHECK (price > 0);
```
<br/>

## Validation Checks Over Multiple Columns

```sql
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    est_delivery TIMESTAMP NOT NULL,
    CHECK (created_at < est_delivery)
);
```

```sql
INSERT INTO orders (name, created_at, est_delivery)
VALUES ('Shirt', '2000-NOV-20 01:00AM', '2000-NOV-10 01:00AM');
```