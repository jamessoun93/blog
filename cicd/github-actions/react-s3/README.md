# [GitHub Actions #2] GitHub Actions로 Amazon S3에 자동으로 배포하기

React로 개발한 프론트엔드 SPA 어플리케이션을 Amazon S3로 호스팅할 때 수정사항 혹은 추가기능이 생길 때 어떻게 하시나요?  
build 관련 파일들을 담고 있는 S3 버킷에 직접 들어가서 모든 컨텐츠를 지우고 새로 build한 결과물을 업로드하시나요?

만약 그렇게 진행하고 있으시다면 배포 과정을 전부 자동화하고 싶으실텐데요. 이럴 때 필요한 개념이 바로 CI/CD죠.

GitHub을 사용하신다면 **GitHub Actions**를 활용하여 원하는 CI/CD 파이프라인을 구축할 수 있습니다.

이번 포스팅에서는 **GitHub Actions**를 활용하여 새로운 코드를 push할 때마다 지정해둔 Amazon S3 버킷을 이용해 자동으로 배포하는 워크플로우를 생성해보겠습니다.

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