# 공지사항 관리 REST API

## 개발할 때 마주한 문제들 및 해결 방법
1. [공지사항 조회수 동시성 이슈 해결하기](https://github.com/Yiseull/notice-management-backend/wiki/%EA%B3%B5%EC%A7%80%EC%82%AC%ED%95%AD-%EC%A1%B0%ED%9A%8C%EC%88%98-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0)
    - 문제 상황: JMeter를 사용하여 1000명의 동시 사용자가 특정 공지사항을 조회할 때, 예상했던 조회수 1000명 대신 396명의 조회수만 카운트되는 동시성 문제가 발생했습니다.
    - 해결 방안: 비관적 락을 선택하여 대용량 트래픽 환경에서의 충돌을 효율적으로 관리하기로 결정했습니다. 낙관적 락과 Redis를 사용한 락은 각각 충돌 처리 비용과 보안상의 이유로 배제되었습니다.
2. [인수 테스트에서 테스트 격리하기](https://github.com/Yiseull/dev-qna/issues/13)
    - 문제 상황: 공지사항 등록 기능에 대한 인수 테스트 중, 서비스가 생성한 공지사항의 ID 확인 시 한 테스트는 성공했으나 다른 테스트에서는 ID 값이 예상과 달리 2L로 나타나 실패했습니다.
    - 해결 방안: 트랜잭션 롤백 시 자동 증가 값이 롤백되지 않는 것이 문제의 원인이었습니다. ```TRUNCATE TABLE notices RESTART IDENTITY;``` 명령어를 사용하여 테스트마다 테이블과 자동 증가 값을 초기화하기로 결정했습니다.

## 실행 방법
> H2 데이터베이스가 설치되어 있어야 합니다.
- 프로젝트를 클론하고 싶은 디렉토리로 이동합니다.
- 터미널에서 ```git clone https://github.com/Yiseull/notice-management-backend.git``` 명령어를 입력합니다.
- IntelliJ IDEA에서 notice-management-backend 프로젝트의 build.gradle 파일을 오픈합니다.
- 프로젝트를 실행합니다.
- http://localhost:8080/swagger-ui/index.html 에서 API 명세를 확인하고 테스트할 수 있습니다.

## 기술 스택
- Gradle
- Java 17
- Spring Boot 3.2.4
- Spring Data JPA
- H2
- Junit5
- Swagger