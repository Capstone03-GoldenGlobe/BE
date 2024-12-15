# GoldenGlobe

## 프로젝트 개요
이 프로젝트는 시니어와 그 가족들이 효율적으로 여행을 준비할 수 있도록 돕는 LLM과 RAG 기반의 맞춤형 여행 챗봇 및 체크리스트 서비스입니다. 
시니어가 여행지 정보를 쉽게 파악하고 준비할 수 있도록 돕는 챗봇 기능과, 여행지에 맞춘 공유 체크리스트 기능을 통해 여행 준비의 효율성을 높입니다.

### 주요 기능
1. 챗봇
2. 체크리스트

### 사용 기술
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=postman&logoColor=white) ![JPA](https://img.shields.io/badge/JPA-000000?style=flat-square&logo=data:image/svg+xml;base64,PHN2ZyB4bWxu...%29) ![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white) ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white) ![AWS](https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat-square&logo=github-actions&logoColor=white) ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=white) ![Lombok](https://img.shields.io/badge/Lombok-2C4F72?style=flat-square&logo=lombok&logoColor=white)


---

## 시작하기
### 사전준비
- 데이터베이스 준비
  - MySQL
  - AWS S3
  - Redis

- 부가 기능 api키 준비
  - 전화번호 인증 : [coolSMS](https://coolsms.co.kr/), 건 당 20원
  - 기온 : [weatherbit](https://www.weatherbit.io/)
  - 도시 이름 번역 : [deepl](https://www.deepl.com/ko/your-account/keys)
    
### How to Build
1. Repository 클론
`git clone https://github.com/Capstone03-GoldenGlobe/BE.git`

2. `src/main/resources` 폴더 아래에 `application.properties` 파일 추가
  ```
spring.application.name=goldenglobe
spring.datasource.url={데이터베이스 링크}
spring.datasource.username={데이터베이스 유저이름}
spring.datasource.password={데이터베이스 비밀번호}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.properties.hibernate.show_sql=true
spring.jpa.hibernate.ddl-auto=update

jwt.secret={jwt토큰키를 생성하는 비밀키. 원하는 글자로 32자 이상 입력}

coolsms.api.key={sms인증을 위한 coolsms 키}
coolsms.api.secret={sms인증을 위한 coolsecret 키}
coolsms.from.number={sms인증 문자를 발신할 전화번호}

spring.data.redis.host={redis database 주소}
spring.data.redis.port=10365
spring.data.redis.password={redis database 비밀번호}

spring.cloud.aws.credentials.accessKey={aws 접속키}
spring.cloud.aws.credentials.secretKey={aws 비밀키}
aws.s3.profile-bucket={aws s3 버켓이름}
aws.s3.pdf-bucket={aws s3 버켓이름}
spring.cloud.aws.region.static={aws EC2 region ex) ap-northeast-2}

spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

springdoc.packages-to-scan=com.capstone03.goldenglobe
springdoc.default-consumes-media-type=application/json;charset=UTF-8
springdoc.default-produces-media-type=application/json;charset=UTF-8
springdoc.swagger-ui.path=/swagger-ui
springdoc.swagger-ui.disable-swagger-default-url=true
springdoc.swagger-ui.display-request-duration=true
springdoc.swagger-ui.operations-sorter=alpha
springdoc.api-docs.path=/api-docs

server.http-to-https-redirect=true
server.tomcat.redirect-context-root=true

weatherbit.api.key={weatherbit api 키}
deepl.api.key={deepl api 키}
```

3. 빌드
`./gradlew build`


### How to Install
- 서버 실행 `./gradlew bootRun`

### How to Test
- 서버 실행 후, `http://localhost:8080/health`에 접속
- 'OK'가 좌측 상단에 실행되면 서버 실행 중

### (Option) 배포 자동화
- `Repository` > `Settings` > `Security - Secretes and variables` > `Actions` > `Repository secrets` > `New repository secret`
- `APPLICATION`, `DOCKER_USERNAME`, `DOCKER_PASSWORD`, `SERVER_HOST`, `SSH_PRIVATE_KEY` 추가
- [참고자료](https://velog.io/@tdddt/docker-springboot-cicd-githubactions-%EA%B5%AC%EC%B6%95)

---
## 🌱 담당 기능
| 🍀 이름 | 🍀 담당 기능 |
|:---:|:---|
| [김근주](https://github.com/tdddt) | - **ERD, REST API 설계, DB연결** <br>- **유저 API**: 회원가입, 로그인, 로그아웃, SMS 인증 발송 및 조회, 프로필 이미지 추가 및 조회, 회원탈퇴 <br> - **여행지 API**: 여행지 추가, 수정, 삭제, 여행지 및 공유 여행지 조회 <br> - **날씨 API**: 기온 조회 <br> - **공유 API**: 체크리스트 공유, 공유 사용자 조회, 공유받은 체크리스트 해제, 공유한 체크리스트 해제 <br> - **체크리스트 API**: 체크리스트 추가, 조회, 그룹 메모 추가/수정/삭제, 그룹 추가/수정/삭제, 아이템 추가/수정/삭제/체크/그룹 변경 <br> - **PDF API**: PDF 이름 목록 조회, URL 목록 조회, 삭제 <br> - **Health Check API** <br> - **API 응답 통일** <br> - **에러 핸들러** <br> - **Swagger 적용** <br> - **배포**: EC2 HTTPS 배포, Docker + GitHub Actions로 배포 자동화 |
| [원재영](https://github.com/jaeyeong13) | - **ERD, REST API 설계, DB연결** <br> - **유저 API**: 회원가입, 로그인, 사용자 정보 조회 및 수정, 아이디 찾기, 비밀번호 찾기, 회원탈퇴 <br> - **여행지 API**: 여행지 추가, 조회 <br> - **챗봇 API**: 챗봇 추가, 챗봇 PDF 조회, 챗봇 로그 조회 <br> - **PDF API**: PDF 업로드 |

---

## 🗂️ 폴더 구조
```
📂 src/main/java/goldenglobe
├─ 📂 chatBot  ▶️ 챗봇 
├─ 📂 chatBotLog  ▶️ 챗봇 로그 
├─ 📂 checkList  ▶️ 체크리스트 
├─ 📂 groupMemo  ▶️ 체크리스트 그룹 메모
├─ 📂 listGroup  ▶️ 체크리스트 그룹
├─ 📂 listItem  ▶️ 체크리스트 아이템
├─ 📂 pdfList  ▶️ PDF
├─ 📂 profileImage  ▶️ 프로필이미지 
├─ 📂 sharedList ▶️ 공유 리스트 
├─ 📂 sms ▶️ 문자 인증 
├─ 📂 travelList ▶️ 여행지 
├─ 📂 user ▶️ 유저
│   └─ 📂 blackList ▶️ 로그아웃
├─ 📂 weather ▶️ 날씨 
├─ APIResponseSetting ▶️ API 응답 형식 통일
├─ BasicController ▶️ 로그인 후 메인페이지. 여행지 및 공유 여행지 조회
├─ CheckListAuthCheck ▶️ 체크리스트 권한 확인 서비스
├─ GoldenGlobeApplication
├─ HealthCheckController ▶️ 서버 상태 확인용
├─ MyExceptionHandler ▶️ 전역 예외 처리
├─ SecurityConfig ▶️ JWT 토큰 설정, Cors설정, Swagger 설정
├─ SwaggerConfig ▶️ Swagger 설정 파일
└─ TravelAuthCheck ▶️ 여행지 권한 확인 서비스
```
<br>

## 📚 오픈소스
