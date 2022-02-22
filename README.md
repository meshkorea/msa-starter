[![](https://api.travis-ci.com/appkr/msa-starter.svg)](https://travis-ci.com/github/appkr/msa-starter)

## msa-starter
[jhipster](https://www.jhipster.tech/)를 사용하지 않고, 메쉬코리아의 스택과 업무 표준에 따르는 스프링부트 마이크로서비스 베이스 프로젝트를 만드는 스타터 앱입니다([유튜브 플레이리스트](https://www.youtube.com/watch?v=cfU5f2wAdDc&list=PL7LvACI5jQlrojoPKfmY6yzG0dyeLUFhu)). 복제/포크해서 스타터 앱(`src`)이나 템플릿(`templates`) 부분을 자유롭게 고쳐서, 각자의 상황에 맞는 새로운 스타터 앱을 만들 수 있을 겁니다

이하 가이드에 따라 빌드된 결과물로 API Spec 작성부터 바로 시작할 수 있습니다.

**메쉬코리아 스택이 없는 일반 스프링부트 마이크로서비스 베이스 프로젝트도 만들 수 있습니다**

[![asciicast](https://asciinema.org/a/381871.svg)](https://asciinema.org/a/381871)

### Install
스타터 앱은 Node.js로 만든 프로젝트입니다. 스타터 앱으로 `build`한 결과물이 스프링부트 프로젝트입니다
```bash
$ git clone https://github.com/appkr/msa-starter.git
```

### Run
`build` spring-boot 마이크로서비스 프로젝트를 만듭니다
```bash
~/msa-starter $ node dist/app.js build
# Non-vroong project(n)? Or vroong project(v) (default:n)?
# Which Java version do you want to use, 8 or 11? 8
# What is the project name (default:example)?
# What is the group name (default:dev.appkr)?
# What is the web server port (default:8080)?
# What is the media type for api request & response (default:application/vnd.appkr.private.v1+json)?
# build/example/.gitignore created
# ...
#
# info: A spring-boot application was created in build/example.
# You can publish the build result with "publish" command
```

`publish` `build`한 프로젝트를 다른 폴더로 발행합니다. 발행한 폴더의 README.md에서 프로젝트를 시작하기 위한 가이드를 참고하세요
```bash
~/msa-starter $ node dist/app.js publish
# What is the build artifact path you want to publish(e.g. ./build/example)? ./build/example
# Where do you want to publish the build(e.g. ~/example)? ~/example
# publishing project:
# {
#   buildDir: '/Users/appkr/msa/msa-starter/build/example',
#   publishDir: '/Users/appkr/example'
# }
# May I continue with these values(y|NO, default:y)? y
# info: done! Read /Users/appkr/example2/README.md to learn how to start.

$ ls ~/example
# README.md  build.gradle  docker  gradle  gradle.properties  gradlew  gradlew.bat  settings.gradle  src
```

`clean` 빌드 결과물을 삭제합니다
```bash
~/msa-starter $ node dist/app.js clean
# info: done!
```

### What's in the spring-boot project generated by the msa-starter
스타터 앱으로 만든 스프링부트 프로젝트에 포함된 내용입니다.

- spring-boot 2.5.0
- Basic setup including actuator, spring-configuration-processor, lombok, mapstruct, logback etc
- docker-compose for local development environment; Which includes MySQL, Kafka
- Persistence
    - JPA, MySQL/H2 driver
    - [Liquibase](https://github.com/liquibase/liquibase-gradle-plugin) integration for schema management
    - QueryDSL, JPA Specification integration
- API first development
    - [openapi-generator gradle plugin](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin) integration
    - [Zalando's RFC7807 problem web](https://github.com/zalando/problem-spring-web) integration
- Scheduler
    - Async TaskExecutor configuration
    - [ShedLock](https://github.com/lukas-krecan/ShedLock) integration for scheduler lock
    - Kafka binder integration
- Log tracing
    - [Zalando's logbook](https://github.com/zalando/logbook) integration for API Request & Response logging
    - [Sleuth](https://spring.io/projects/spring-cloud-sleuth) integration for distributed log tracing
    - [Sentry](https://docs.sentry.io/platforms/java/guides/logback/) integration for exception tracing
- Security
    - [Jhipster UAA](https://www.jhipster.tech/using-uaa/) integration
    - ResourceServer integration
- CI & CD
    - [git-properties gradle plugin](https://github.com/n0mer/gradle-git-properties) integration
    - [jib gradle plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin) for docker image build
- Frequently-used utilities
    - e.g. `PaginationUtils`
- Examples
    - Example API incl. MockMvc test
    - PersitentEvent for "at-least-once" message publishing

### Inter-operation with Auth Server
[jhipster-uaa.zip](./jhipster-uaa.zip) 은 로컬 컴퓨터에서 사용할 수 있는 Oauth2 인증 서버입니다. 아래 가이드에 따라 MySQL, Kafka, jhipster-uaa 를 한번에 띄울 수 있습니다

```bash
~ $ cp msa-starter/jhipster-uaa.zip ./
~ $ unzip jhipster-uaa.zip && cd jhipster-uaa && ./gradlew jibDockerBuild
# 위 2 줄은압축을 풀고, jhipster-uaa 도커 이미지를 빌드하는 명령으로, 최초 한번만 실행하면 됩니다
~ $ cd ~/your-project && ./gradlew clusterUp
```
압축을 푼 `jhipster-uaa/README.md` 파일에서 jhipster-uaa 프로젝트에 대한 더 자세한 설명을 볼 수 있습니다 

### Contribution
`src` 폴더 아래에 있는 스타터 앱뿐만아니라, `templates` 스프링부트 프로젝트도 기여할 수 있습니다

#### Folder Structure
```bash
├── tsconfig.json           # typescript 컴파일 설정
├── nodemon.json            # nodemon 설정
├── src
│   ├── BuildCommand.ts     # build 커맨드 클래스
│   ├── CleanCommand.ts     # clean 커맨드 클래스
│   ├── PublishCommand.ts   # publish 커맨드 클래스
│   ├── app.js              # main
│   ├── libs
│   │   ├── ControlUtils.ts # 흐름 제어를 위한 헬퍼 모음
│   │   └── FileUtils.ts    # 파일 IO 관련 헬퍼 모음
│   └── model
│       ├── BuildInfo.ts    # build 커맨드에서 사용자로부터 받은 값으로 만든 빌드 컨텍스트
│       ├── JavaVersion.ts  # 자바 버전 열거 타입
│       ├── ProjectType.ts  # 부릉과 개인 프로젝트를 구분하기 위한 열거 타임
│       └── PublishInfo.ts  # publish 커맨드에서 사용자로부터 받은 값으로 만든 발행 컨텍스트
├── templates               # Handlebar 템플릿을 포함하고 있는 스프링부트 프로젝트
└── dist                    # typescript로부터 컴파일된 javascript를 담은 폴더
```

#### Commands
커맨드|클래스|역할
---|---|---
`build`|`BuildCommand`|사용자에게 입력 받은 값을 `templates` 폴더에 담긴 스프링부트 프로젝트의 Handlebar 템플릿에 바인딩한 후 `build` 폴더에 파일로 쓴다
`publish`|`PublishCommand`|사용자로부터 받은 소스 폴더의 내용물을 목표 폴더로 복사하고 깃 최초 커밋을 한다
`clean`|`CleanCommand`|`build` 폴더 하위의 파일을 전부 지운다

#### Dev

```bash
$ cd msa-starter && yarn
```

커맨드|역할
---|---
`yarn start:dev`|`src` 폴더의 변경을 감지하고 컴파일해서 `dist` 폴더로 발행하는 데몬 실행
`yarn build`|`dist` 폴더의 내용을 삭제한 후 `src` 폴더를 컴파일해서 `dist` 폴더로 발행하는 커맨드
