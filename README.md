# Springboot MVC 종합 테스팅 프로젝트

## 프로젝트 설명
본 프로젝트는 멀티 모듈 구조의 종합 테스팅 프로젝트입니다.<br>
모듈 분리의 기준은 독립적 기능 중심 분리에서 시작하여 서비스 구현으로 나뉘어집니다.<br>

## 프로젝트 설명 상세
### 모듈 분리 기준
본 프로젝트에서 모듈은 크게 세가지로 나누었습니다.<br>
1. module-idp-* 접두사로 시작되는 independent 모듈은, 다른 모듈에 종속성이 없는 독립적인 모듈입니다.<br>
모듈 분리는 idp 모듈을 분라하는 것에서 시작되며, <br>
독립 모듈로 분리가 가능한 적절한 크기, 뚜렷한 토픽을 지닌 모든 코드는 최대한 독립 모듈로 분리하여 모듈화의 장점을 최대화 하였습니다.<br><br>

2. module-api-* 접두사로 시작되는 api 모듈은, 서버의 창구 역할을 하는 API Controller 를 구현하기 위한 모듈입니다.<br>
api 모듈은 가장 클라이언트 요청에 닿아있으며, 되도록 기능 구현은 맡지 않고 말 그대로 api 의 개발에만 전념한 모듈로 다뤄져서 관심사를 분리합니다.<br>
Springboot 를 실행시키는 main 함수는 이곳에 위치하며, 즉 한 프로젝트 안에 서버를 동작 시킬 수 있는 Starter 모듈이 여러개 있을 수 있고, 그것이 바로 api 모듈입니다.<br><br>

3. module-dpd-* 접두사로 시작되는 dependent 모듈은, idp 모듈과는 달리 다른 모듈에 종속적이면서 api 모듈과 차이점이 존재하는 모듈입니다.<br>
대표적인 차이점으로, dpd 모듈은 Springboot main 함수가 없기에 서버를 실행시킬 주체적 권한이 없는 비독립 모듈이라 생각하면 되며,<br>
idp 모듈과 api 모듈의 사이에서 api 모듈에게 사용되는 역할입니다.

위와 같은 설명을 다시 요약하자면,<br>
idp 모듈은 기능중심으로 최대한 독립적으로 설계되고 분리된 모듈이며,<br>
api 모듈은 서비스 단위의 api 작성 + 서버 실행용 모듈,<br>
dpd 모듈은 idp 모듈과 api 모듈의 사이에 있는 모듈로,<br>
<br>
idp 모듈은 api, dpd 모듈에게 사용되고,<br>
dpd 모듈은 api 모듈 혹은 동일한 dpd 모듈에게 사용되지만,<br>
api 모듈은 그 누구에게도 사용될 수 없습니다.<br>
<br>
![화면 캡처 2024-10-17 085755](https://github.com/user-attachments/assets/adda2b5e-c631-4b5d-bf82-98e538d17698)

<br>
위와 같은 구분법으로 인하여, idp 모듈은 한번 만들어지면 다른 모듈의 변경에 신경을 쓰지 않아도 되고,
종속성 모듈의 경우는 build.gradle 을 확인하여 명확한 모듈 종속성에 따라 처리할 수 있게 됩니다.

## 프로젝트 실행 방법
본 프로젝트를 실행시키기 위해서 필요한 조건이 존재합니다.<br>
MySQL or MariaDB, Redis, MongoDB, Kafka<br>
위의 네가지 서비스가 로컬상에 동작해야하며,<br>
RDBMS 는 JPA 모듈에서 설정한 Entity 의 스키마, 테이블 구조를 따라야 합니다.<br>
<br>
위와 같은 서비스가 설치되어 있지 않다고 한다면, external_files 안의 docker 모음으로 설치가 가능하며,<br>
<br>
![화면 캡처 2024-10-17 091734](https://github.com/user-attachments/assets/3ac771c4-2005-4243-a545-c0ea44f25ee0)

<br>
각 서비스들에 대한 설정은 각 구현 모듈 설정을 변경시켜 실행시킬 수 있습니다.<br>
<br>
프로젝트를 정상적으로 실행시켜 Open 된 포트에 웹 브라우저로 접속한다면, 서버의 Welcome 페이지를 확인하실 수 있고,<br>
<br>

![화면 캡처 2024-10-17 091224](https://github.com/user-attachments/assets/17236284-8e49-45ce-9fd8-46760e9b1b23)

![화면 캡처 2024-10-17 091333](https://github.com/user-attachments/assets/78fdda85-ea8a-4630-bc00-492d64ff92d5)

<br>
서버에서 제공해주는 Swagger 문서를 통하여 본 프로젝트에서 구현된 여러 기능들을 테스트 하실 수 있습니다.

## 모듈 단위 설명
### idp-common 모듈
별도의 독립 모듈로 떼어놓기에는 규모가 작은 유틸리티성 코드를 모아둔 모듈입니다.<br>
api 모듈에 속하지 않는 독립성을 지닌 코드인데, 분류가 곤란흔 모든 코드를 여기에 모아두고,<br>
추후 특정 기능의 규모가 커진다면 모듈을 분리합니다.

### idp-jpa 모듈
jpa 독립 모듈은 jpa 라이브러리를 사용하여 데이터베이스 테이블을 매핑하는 Entity 클래스나,<br>
테이터베이스를 사용하는 Repository 클래스를 구현하는 데에 사용됩니다.

### idp-retrofit2 모듈
OkHttp 기반의 네트워크 요청 라이브러리 Retrofit2 의 코드를 모아두는 모듈입니다.<br>

### idp-mongodb 모듈
mongodb 라이브러리 사용을 담당하여 mongoDB 를 조작하는 모듈입니다.<br>
일부 기능을 사용하려면 main 함수 위치에서 어노테이션 세팅이 필요합니다.

### idp-redis 모듈
Redis 사용 라이브러리입니다.<br>
본 프로젝트에서는 Redis 의 사용성을 높이기 위한 API 래핑을 통해 보다 편하게 Redis 를 사용하도록 개선하였습니다

### idp-aws 모듈
AWS 를 다루는 API 를 모아둔 모듈입니다.<br>
본 프로젝트에서는 주로 AWS S3 파일 업로드, 다운로드, 삭제 기능을 구현하였으며,<br>
추후 기능을 추가 할 수 있습니다.

### dpd-common
idp-common 과 동일한 이유로, 별도의 토픽으로 분리할 수는 없는 비독립적인 기능을 작성할 때 사용하는 모듈입니다.

### dpd-sockjs, dpd-socket-stomp
Springboot 소켓 구현 모듈입니다.<br>
api 모듈에 의해 사용되며, 현재로는 다른 모듈에 대한 종속성이 없지만,<br>
기능의 본질적인 특성상 추후 얼마든지 모듈 종속성이 발생할 수 있기에 dpd 모듈로 작성하였습니다.

### dpd-scheduler
Springboot 스케쥴러 구현 모듈입니다.<br>
main 함수 위치에서 어노테이션 세팅이 필요합니다.

### dpd-kafka
이벤트 스트리밍 서비스 Kafka 구현 모듈입니다.<br>
Socket 과 동일한 이유로 dpd 모듈로 작성하였습니다.

### api-sample
서비스 단위 api 모듈로, 본 프로젝트에서는 다른 모듈들의 각 기능들을 테스트하는 데에 최적으로 설계된 api 를 제공하는 역할을 맡습니다.<br>
api 컨트롤러는 인터페이스를 설계하는 역할을 맡은 controller 파일과,<br>
그 기능을 구현하는 역할을 맡은 service 파일의 쌍으로 구성됩니다.<br>
또한, Swagger API 문서 라이브러리 규격에 맞추어 코딩이 진행되므로, controller 파일은 api 설명을 강제당하게 되어, 주석 사용 및 방식을 코딩 단계에서 통일 시킬 수 있게 하였습니다.<br>
이 모듈에서 신경써야 할 것은,
1. build.gradle 로 타 모듈에 대한 종속성 주입
2. application.yml 로 타 모듈의 설정 파일 적용
3. logback 로깅 설정
4. swagger 문서 설정
5. api 작성

위와 같습니다.

## 기능 단위 설명
본 프로젝트에서 구현한 대표적인 기능들은 아래와 같습니다.

### jpa
다중 데이터베이스 접속 구조의 JPA 설정 방식 구현<br>
다중 데이터베이스 접숙 구조에서의 Transactional 어노테이션 구현<br>
외례키, 유니크 등의 제약 조건 처리<br>
확장성 있는 회원 정보 구조<br>
검색, 페이징, 좌표계산 등의 쿼리문 수록<br>
JpaRepository, Native Query, QueryDSL 사용<br>

### mongoDB
다중 데이터베이스 접속 구조의 설정 방식 구현<br>
MongoDB Replicaset 사용 설정<br>
다중 데이터베이스 접숙 구조에서의 Transactional 어노테이션 구현<br>
MongoDB CRUD 구현

### Redis
다중 소스 접속 구조의 설정 방식 구현<br>
Redis Cluster 사용 설정<br>
Redis 보안 설정<br>
Key-Value 기반의 사용성 개선 코드 래핑

### AWS
AWS S3 파일 조작 함수 구현

### Socket
Springboot 로 구현하는 SockJS 및 STOMP 기반 개발 방식 정리<br>
클라이언트 메시지 수신시 정해진 메시지를 반환하는 테스트용 에코 시스템 구축<br>
비동기 안정성 확보

### Scheduler
일정 시간마다 코드를 실행시키는 스케쥴링 기능 구현

### kafka
다중 소스 접속 구조의 설정 방식 구현<br>
SASL 보안 설정 및 Kafka 클러스터 사용<br>
Kafka Consumer, Producer 샘플 기능 구현

### api
기능 테스트용 Rest API 및 기본 홈 화면 Web 페이지 구현<br>
Swagger 문서 사용 및 접속 가능<br>
logback 을 통한 로그 형식 설정<br>
application.yml 멀티 프로필(모듈 포함) 기능<br>
Request,Response 자동 로깅 기능<br>
Springboot Actuator 설정 및 접속 ip 화이트리스트 방식 적용

### 회원 기능
회원을 구분짓는 가장 근본적인 정보는 데이터베이스 회원 테이블의 고유값으로 두었습니다.<br>
회원 가입시에는 문자 인증, Email 인증, OAuth2 인증 등으로 본인임을 증명할 수 있도록 하였고,
유저 권한에 차등을 줄 수 있도록 설계를 하였으며, 회원 계정 정지 테이블을 추가하여 서비스 접근 금지 처리, 
휴면 회원 처리가 가능하도록 설계하였습니다.<br>
<br>
API 의 인증/인가 처리는 JWT 를 사용하였습니다.<br>
토큰 정보는 Stateless 한 JWT 인증/인가 방식의 장점을 살리기 위하여 메모리에 저장하지 않으며,
다만 실시간 접근 금지 처리를 위하여 블랙리스트 방식을 사용하여, 이에 등록된 토큰은 만료 처리가 되도록 처리하였습니다.<br>
블랙리스트 정보는 서버 복제를 통한 Scale Out 을 고려하여 공유 메모리인 Redis 에 저장하고, 
토큰 만료 시점에 자동으로 메모리에서 사라지도록 ExpireTime 을 설정하였습니다.<br>
JWT 내 중요 정보는 AES256 알고리즘을 통해 암호화 한 상태입니다.

### 그외
SSE, 지도 좌표계 좌표 계산, 이메일 전송, SMS, Kakao Talk 전송, 비디오/오디오 스트리밍, 암복호화 등의 기능 구현
