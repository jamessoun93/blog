# [Docker #3] Docker Networks

지금까지는 도커를 활용하여 간단하게 컨테이너를 만들어보고 만든 컨테이너를 활용해봤습니다.

그렇다면 서로 다른 이미지를 활용하여 만든 컨테이너끼리 연결하는 방법은 없을까요?

내가 만든 **Node.js** 애플리케이션을 컨테이너로 띄우고, 해당 **Node.js** 애플리케이션에서 연결할 **MySQL** 컨테이너를 띄웠을 때 서로 연결이 가능해야하겠죠?

**Docker Network**를 사용하면 가능합니다.

도커를 사용하여 컨테이너를 시작하면 도커 내 **bridge**라 불리는 가상 네트워크에 연결됩니다.

같은 가상 네트워크에 존재하는 컨테이너들 끼리는 `-p` or `--publish` 옵션을 줘서 **port**를 열지 않아도 서로 연결이 가능합니다.

`my_web_app` 이라는 네트워크 내에 **Node.js** 애플리케이션 컨테이너와 **MySQL** 컨테이너를 함께 두는 것이죠.

이렇게 되면 해당 **Node.js** 컨테이너와 **MySQL** 컨테이너는 포트를 열지 않아도 서로 연결이 가능합니다.

하나의 컨테이너는 하나 혹은 여러개의 가상 네트워크에 포함시킬 수 있고 필요에 따라 아무 가상 네트워크에 포함시키지 않고 host 네트워크를 직접 사용할 수도 있습니다.

---

## Commands

### Network 리스트 확인
```sh
docker network ls
```

### Network inspect 하기
```sh
docker network inspect
```

### 새로운 Network 생성
```sh
docker network create --driver
```

### 컨테이너에 네트워크 연결
```sh
docker network connect
```

### 컨테이너에 네트워크 연결 끊기
```sh
docker network disconnect
```