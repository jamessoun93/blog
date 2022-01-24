# GitHub Actions로 Amazon S3에 자동으로 배포하기

React로 개발한 프론트엔드 SPA 어플리케이션을 Amazon S3로 호스팅할 때 수정사항 혹은 추가기능이 생길 때 어떻게 하시나요?

build 관련 파일들을 담고 있는 S3 버킷에 직접 들어가서 모든 컨텐츠를 지우고 새로 build한 결과물을 업로드하시나요?

만약 그렇게 진행하고 있으시다면 배포 과정을 전부 자동화하고 싶어서 이것저것 찾아보다가, 귀차니즘 + 어려운 개념으로 GG를 치고 익숙한 방법으로 진행을 하고 계실 것 같습니다.

그런분들을 위해 GitHub Actions를 활용한 S3 배포 자동화 가이드를 준비했습니다.

시작해보실까요?

> 해당 포스팅은 React 혹은 다른 SPA 프레임워크로 개발한 프론트엔드 어플리케이션을 Amazon S3로 배포해본 경험이 있으신분들을 대상으로 작성했습니다.

---

# GitHub Actions란 무엇일까? CI/CD란 무엇일까?

GitHub Actions는 우리가 개발한 어플리케이션의 빌드, 테스트, 배포를 자동화할 수 있게 해주는 CI/CD 파이프라인입니다.

우리가 GitHub Repo에 코드를 push하거나 새로운 PR을 생성할 때마다 자동으로 진행됐으면 하는 액션들을 직접 정의해서 자동화할 수 있다는 뜻입니다.

대표적인 예로 빌드&테스트가 있습니다.

내가 작성한 코드를 push할때마다 staging 혹은 production 환경으로 올리기 전에 해당 변경사항으로 인해 생기는 문제는 없는지 빌드 후 작성한 유닛테스트를 돌려 확인할 수 있습니다.

CI/CD 파이프라인이 없다면 이런 과정을 매번 직접 진행해야하는데, 굉장히 자주 해야하는 일 치고 공수가 정말 많이드는 작업이기 때문에 자동화할 수 있으면 정말 좋은 작업입니다.

우리는 CI/CD 파이프라인을 통해 우리가 원하는 작업을 자동화할 수 있으며 자동화할 수 있는 작업의 범위는 엄청나게 넓습니다.

---

# GitHub Actions 이해하기

지금부터 설명드릴 내용은 GitHub Actions 공식문서에 상세하게 나와있는 내용들입니다. 하지만 한글로 번역된 버전이 존재하지 않기때문에 최대한 이해하기 쉽게 설명을 해보겠습니다. (한글 번역으로 contribute 해보려 했으나 외부 번역 PR은 받고있지 않다고 합니다 😭)

---

```yml
name: gh-actions

on:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-node@v2
        with:
          node-version: '16'
      - run: npm install
      - run: npm run build
      - uses: jakejarvis/s3-sync-action@master
        with:
          args: --acl public-read --follow-symlinks --delete
        env:
          AWS_S3_BUCKET: ${{ secrets.AWS_S3_BUCKET }}
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          AWS_REGION: 'ap-northeast-2'
          SOURCE_DIR: 'build'
```