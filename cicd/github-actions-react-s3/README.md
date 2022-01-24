# GitHub Actions로 Amazon S3에 자동으로 배포하기

React로 개발한 프론트엔드 SPA 어플리케이션을 Amazon S3로 호스팅할 때 수정사항 혹은 추가기능이 생길 때 어떻게 하시나요?

build 관련 파일들을 담고 있는 S3 버킷에 직접 들어가서 모든 컨텐츠를 지우고 새로 build한 결과물을 업로드하시나요?

만약 그렇게 진행하고 있으시다면 배포 과정을 전부 자동화하고 싶어서 이것저것 찾아보다가, 귀차니즘 + 어려운 개념으로 GG를 치고 익숙한 방법으로 진행을 하고 계실 것 같습니다.

그런분들을 위해 GitHub Actions를 활용한 S3 배포 자동화 가이드를 준비했습니다.

시작해보실까요?

> 해당 포스팅은 React 혹은 다른 SPA 프레임워크로 개발한 프론트엔드 어플리케이션을 Amazon S3로 배포해본 경험이 있으신분들을 대상으로 작성했습니다.

---

```yaml
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