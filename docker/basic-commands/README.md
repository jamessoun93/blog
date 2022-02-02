# Docker Basic Commands

설치된 Docker 버전 확인 (verify cli can talk to the engine)
```sh
docker version
```

상세정보 확인 (most config values of the engine)
```sh
docker info
```

실행 가능한 명령어 리스트
```sh
docker
```
- docker <command> <sub-command> (options)
- old way: docker <command> (options)

---

컨테이너 실행
```sh
docker container run
```
1. 로컬 이미지 캐시에서 이미지를 찾고 없으면 Docker Hub 같은 리모트 이미지 repository를 확인
2. 최신 버전 다운로드 (ex. `nginx:latest`)
3. 해당 이미지로 새로운 컨테이너 생성
4. 도커 엔진 내부 private network에 가상 ip를 할당
5. `--publish` 옵션이 있다면 명시된 포트를 열고 아니면 열지 않음
6. `Dockerfile` 내 `CMD`를 활용해 컨테이너 실행

nginx 서버 실행
```sh
docker container run --publish 80:80 nginx
```
1. Docker Hub으로부터 nginx 이미지 다운로드
2. 다운로드 받은 이미지를 활용해 새로운 컨테이너 생성
3. 호스트 IP의 80 포트를 열고 컨테이너 IP의 80 포트로 라우팅

백그라운드에서 실행
```sh
docker container run --publish 80:80 --detach nginx
```

실행 시 컨테이너 NAME 지정
```sh
docker container run --publish 80:80 --detach --name <container name> nginx
```

실행중인 컨테이너 리스트 확인
```sh
docker container ls
```
```sh
docker ps
```

모든 컨테이너 리스트 확인
```sh
docker container ls -a
```

컨테이너 실행 중지
```sh
docker container stop <container id>
```

컨테이너 로그 확인
```sh
docker container logs <container name>
```

컨테이너 삭제 (여러개라면 스페이스로 구분한 container id 리스트 입력)
- 실행중인 컨테이너는 삭제 불가능하고 강제로 삭제하려면 `-f` 옵션 제공
```sh
docker container rm <container id>
```

특정 컨테이너 내 실행중인 프로세스 리스트
```sh
docker top <container name>
```

이미지 리스트 확인
```sh
docker image ls
```