# {{projectName}}

## 개발 환경

- amazonaws corretto jdk 17을 사용합니다
```shell
$ brew install homebrew/cask-versions/corretto17 --cask
$ jenv add /Library/Java/JavaVirtualMachines/amazon-corretto-17.jdk/Contents/Home
$ jenv versions
```

- 아래 명령으로 MySQL(3306), Kafka(9092)등을 구동합니다
```shell
~/{{projectName}} $ ./gradlew composeUp
# To stop and delete the cluster, ./gradlew composeDown
```

- src/main/resources/db/schema.sql을 임포트합니다.

- 애플리케이션을 구동합니다
```shell
~/{{projectName}} $ export SPRING_PROFILES_ACTIVE=local; export USER_TIMEZONE="Asia/Seoul"; ./gradlew clean bootRun -x :clients:bootRun
$ curl -s http://localhost:{{portNumber}}/management/health
```

### 계정

Infra

docker service|username|password
---|---|---
mysql|root|secret

## 개발

- java code는 [google style guide](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)를 따릅니다 (hard wrap은 120까지 허용)
- 패키지 구조는 [육각형 구조](https://reflectoring.io/spring-hexagonal/)를 따릅니다

### 프로젝트 최신화

```shell
~/{{projectName}} $ ./gradlew dependencyUpdates
```

## 빌드

- [Jenkins BlueOcean]() 화면에서 빌드합니다

## openApi/asyncApi 스펙 확인

- redoc 또는 openApi로 렌더링하여 확인할 수 있습니다

## 배포

- 릴리스 브랜치에서 `gradle.properties`에 애플리케이션 버전을 부여합니다
- [ArgoCD]()를 사용합니다 

## 모니터링

- [Grafana]()
- [Kibana]()
- [Sentry]()
