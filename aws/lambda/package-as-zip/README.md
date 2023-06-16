# 람다 함수 zip 으로 패키징하기

람다 함수에서 3rd party 패키지가 필요한 경우 AWS 콘솔 내 코드 에디터로만은 이용할 수 없다.

이럴 때는 로컬 머신에 람다용 디렉토리를 만들어서 원하는 패키지 매니저를 이용해 필요한 패키지를 다운받아 설치할 수 있다.

개인적으로 람다 함수는 파이썬으로 작성하는게 편하다고 생각하여 pip 를 이용했다.

1. 디렉토리 생성

2. 람다 함수 생성

```python
import os
import pymysql
# ... 외 필요 모듈

def lambda_handler(event, context):
    # ... 코드
```

3. 필요한 패키지 설치 (생성한 디렉토리에서)

```sh
pip3 install pymysql -t .
```

나는 pymysql이 필요하기 때문에 pymysql을 설치

4. 람다 업로드를 위한 zip (생성한 디렉토리에서)

```sh
zip -r ../lambda_function.zip .
```

5. 람다에 업로드

직접 패키징을 해야하는 경우 위와 같이 할 수 있지만 깃헙 액션을 이용해 S3 업로드 후 deploy 되게끔 자동화하는 방식을 추천한다.
