# Springboot Kotlin MVC Template

## 프로젝트 설명
- 본 프로젝트는 제 개발 라이프에서 경험한 다양한 상황, 다양한 기능들에 대해 정리한 프로젝트입니다.

  이 프로젝트 자체로는 매력적인 서비스를 구현한 것이 아니지만, **잘 관리된 공구함**이라 생각하시면 좋습니다.

  예를 들어 설명하겠습니다.

  개발을 진행하며 처음으로 구현하는 기능을 맞닥뜨렸다고 합시다.
  그렇다면 그 기능을 분석하고, 이해하고, 구현하는데에 처음에는 애를 먹을 것입니다.
  해당 기술이 복잡하다면 다음에 구현할 때에 처음보다야 수월하기는 할지라도 애를 먹을 수도 있고요.

  그런 상황을 방지하고자, 한번 만들어본 기능들을 모듈화하여 깔끔하게 정리해둔 프로젝트에 먼저 그 기능을 구현해보고, 테스트하고, 리펙토링 한 후 이식하는 방식으로 개발하는 것입니다.

  이리하면 단순히 기능을 구현하는 것이 아니라, 추후 재활용이 쉬우며, 라이브러리 버전업 등의 이유로 인해 테스트가 필요한 시점에도 부담없이 테스트가 가능하고, 기술 응용 및 다른 기능과의 충돌까지 방지할 수 있습니다.

  개발자의 입장에서는 개발을 진행할 수록 이렇게 만든 템플릿 프로젝트에 기능이 쌓이고, 수중에 사용할 수 있는 기술들이 쌓이는 것이기에 실력을 쌓는 데에도 좋은 방식이라 생각합니다.
- 본 프로젝트는 한번 구현한 기능과 직접 작성한 코드라고 할지라도 낯설어지고 어색해지는 것을 방지하고자 하는 취지에서 만들었습니다.

  제가 진행하는 프로젝트에서 경험했거나, 혹은 앞으로 필요하다고 생각하는 기술들을 분리하기 좋은 형태로 만들어 재활용이 쉽게 하였으며, 그로 인하여 **코드 구현에 구애 받지 않고 보다 고도의 기능 구현에 집중할 수 있도록 정리하는 것이 목적**인 프로젝트입니다.
- 만약 본 템플릿 프로젝트를 읽는 법을 배우신다면 본 프로젝트에서 제공하는 기능들에 대해 바로 적용 및 응용이 가능하게 될 것이며, 그에 나아가 템플릿을 수정하고 본인의 스타일에 맞게 작성 하는 법을 배우신다면 제가 그러했듯 개발 공부에 보람이 생기며, 실무에서도 큰 도움이 되리라 생각이 됩니다.


## 프로젝트 구조 설명
먼저, 본 프로젝트의 파일, 폴더 구조에 대해 설명하겠습니다.

여러 기능들을 구현하기 위하여 스스로 생각한 방식들을 과감하게 사용하였으므로, 일반적인 방식과는 다른 구조를 지녔을 수 있으며, 작명법 역시 처음 본다면 이해하기 어려운 부분이 있을 수도 있습니다.


### {Root}
- 프로젝트의 최외곽 폴더 내에는 .gitignore 파일, build.gradle.kts 파일 등이 존재합니다. 되도록 이 위치에는 기본 파일 외에는 추가하지 않습니다.


### .gitignore
- VCS git 에서 버전관리에 포함 시키지 않을 목록을 설정하는 파일입니다. 본 프로젝트에서 처리한 특이점은,
  /by_product_files 폴더 내의 모든 파일을 git 에서 무시하라고 설정한 것으로,

  뒤에서 설명할 by_product_files 폴더는 log 파일 등과 같이 프로젝트가 실행되며 그 부산물로 생성되는 파일이므로 VCS 에서 제외한 것입니다.


### build.gradle.kts
- Gradle 설정 파일입니다. 본 프로젝트에만 적용되는 특이한 점은 없으며, 상황에 맞게 설정하여 사용하시면 됩니다.
  주의점으로는, 종속성 라이브러리의 버전 변경, 설정 변경시에는 관련된 기능들을 반드시 테스트 하셔야 합니다.
  후에 설명드릴 Swagger 웹 문서를 이용하신다면 테스트를 편히 수행하실 수 있습니다.


### TODO.txt
- 프로젝트 전역에 있어 처리해야 할 일들을 정리하여 게시하는 용도의 텍스트 파일입니다.


### by_product_files
- 이 폴더는 프로젝트가 실행되며 생성되는 여러 파일 분산물들을 모아두는 폴더입니다.
  대표적으로는 로그 파일이 그러합니다.
- 이 폴더의 모든 파일들은 VCS 에서 관리하지 않습니다.
- 요약하자면, 프로젝트 실행 과정에서 생성되는 파일이자, VCS 에서 관리하지 않는 파일들은 이곳에 저장하도록 하면 됩니다.


### external_files
- 개발자가 임의로 프로젝트와 함께 저장하는 파일은 이곳에 모아둡니다.
- 본 프로젝트에서는 본 프로젝트에서의 테스트를 실행하기 위한 외부 서비스(Redis, MongoDB 등)를 간편하게 실행시키기 위한 설정이 모여있는 dockers 폴더, API 테스트시에 사용할 용도로 준비된 파일들을 모아둔 files_for_api_test 폴더, 그리고 개인적인 개발 노하우 등을 정리해둔 문서들을 보관한 knowledges 폴더가 존재합니다.


### src/test
- Springboot 테스트용 @SpringBootTest 클래스를 모아두는 폴더입니다. 저의 경우는 TDD 를 따로 하지 않고, 별도 클라이언트 프로젝트를 만들거나, Swagger 문서로 수동으로 테스트 하는 방식을 사용하는데, 혹 TDD 를 하실 분이라면 적극적으로 사용하세요.
- 현재는, @ActiveProfiles("dev", "prod") 이 설정으로 인하여 gradle build 중에 자동으로 dev, prod 설정으로 프로젝트 연결 테스트(JPA 검증, 데이터베이스 연결 검증, 종속성 라이브러리 검증, 실행 검증)만을 수행합니다.


### src/main/resources/static
- springboot 기본 생성 폴더로, 외부에서도 접근 가능한 static 리소스 파일을 저장하는 위치입니다.
  Springboot 웹 페이지에서 사용되는 favicon.ico 파일이나, 이외에 외부에 공개되어도 상관 없는 리소스 파일들을 저장하세요.
- static 리소스 폴더의 파일에 접근하기 위해선, 프로젝트 서비스 접근 주소 + static 폴더 내의 파일명을 입력하면 됩니다.
  예를들어 static 폴더 바로 아래에 위치한 favicon.ico 파일의 경우는, http://127.0.0.1:8080/favicon.ico 위와 같이 접근할 수 있고, test 라는 서브 폴더 안의 sample1.txt 파일의 경우는, http://127.0.0.1:8080/test/sample1.txt 위와 같이 접근할 수 있습니다.
- 파일 분류 규칙, 폴더명 작성 규칙이 별도로 존재합니다.
  본 프로젝트에서는 파일들을 구분 없이 static 폴더 안에 넣어두어 사용하는 것이 아니라, 모든 파일들을 폴더별로 분류하여 저장하는 방식을 사용합니다.
  스프링 프로젝트에서 resource 파일을 사용하는 경우는, 보통 특정 api 에서 해당 파일이 필요하기 때문인 경우가 많습니다. 고로 파일들을 api 를 기준으로 나누면 좋겠다는 생각을 하였습니다. 본 프로젝트에서는 파일을 사용하는 주체가 되는 **api 의 일련번호와 제목을 합친 이름(for_{api 코드}_{api 이름})** 의 폴더를 생성하고, 해당 api 에서 사용할 모든 파일들을 이 안에 저장하는 방식으로 분류하였습니다.
  예를들어 for_sc1_n1_home_page 이러한 resource static 폴더명이 존재할 수 있습니다.
  이 이름의 뜻은, sc1-n1 이라는 코드와, homePage 라는 제목을 지닌 api 에서 사용할 resource 파일들을 모아둔 서브 폴더라는 뜻입니다.
  또한, 만약 api 단위로 구분을 지을 수 없는 유형의 파일이라면, for_global 서브 폴더 안에 넣으면 됩니다.
  이 폴더는 api 로 구분지을 수 없으며, 전역에서 사용할 파일들을 모아둔 static 파일 서브 폴더라는 뜻입니다.
  앞에서 예시로 든 for_sc1_n1_home_page 이러한 작명법에서 사용한 api 코드, api 이름에 대한 내용은 뒤에서 api 작명법을 설명할 때 다시 언급하겠습니다.


### src/main/resources/templates
- Springboot 의 Thymeleaf 에서 사용하는 HTML 파일들을 모아두는 폴더입니다.
  Controller 내의 api 반환값으로, "home_page" 이런식으로 값을 반환한다면, resources/templates/home_page.html 이 반환됩니다.
- resources/templates 의 파일 분류 규칙과 폴더명 작성 규칙은 resources/static 과 동일합니다.
  api 를 기준으로 하여 html 을 분류하고, 분류에 사용된 서브폴더의 폴더명 작성 규칙 역시 resources/static 와 동일하게, **api 의 일련번호와 제목을 합친 이름(for_{api 코드}_{api 이름})** 이러한 규칙으로 작명 합니다.


### src/main/resources/application.yml
- 프로젝트 설정 yaml 파일입니다. 라이브러리에 따라, 구현하려는 기능에 따라 작성하시면 됩니다.
- 본 프로젝트에서 특이한 점으로는, 설정 파일을 프로필 별로 분할하고, 그 안에서도 기능별로 분할이 가능하도록 하였습니다.
  application-dev, application-prod, application-local 파일은
  각각 개발 서버 환경, 배포 서버 환경, 로컬 환경을 가정한 설정으로,
  application.yml 의 spring.profiles 에 보시면 이를 설정하고 사용하는 부분을 확인하실 수 있습니다.
  group 에 보시면 local 과 port8080 를 결합하여 local8080 이라는 프로필 그룹을 만들어낸 것을 볼 수 있습니다.
  여기에 사용된 local 은 application-local 에서 local 을 의미하고, port8080 은, application-port8080 에서 port8080 을 의미합니다.
  local 에서 사용할 설정과 8080 포트를 사용하는 설정을 합쳐서 이렇게 지정할 수 있으며, local8080 프로필로 서버를 실행시키려면, Java 실행 옵션에
  > -Dspring.profiles.active=local8080

  위와 같이, group 에서 설정한 프로필명을 입력하면 됩니다.
  실행시 프로필 설정을 하지 않은 경우에 사용될 프로필 설정은, spring.profiles.default 에 설정하면 됩니다.
  본 프로젝트에서는 local8080 을 기본 프로필로 설정하였으므로, 만약 프로필 설정을 하지 않고 서버를 실행시켰다면, local8080 설정이 적용되는 것입니다.
- 아시다시피 프로젝트 내에서 사용할 상수값, 설정값은 이곳에 모아두어 사용하는 것이 좋습니다.
  이때에 사용할 상수값은 custom-config 아래에 작성하도록 할 것입니다.


### src/main/resources/logback-spring.xml
- springboot 의 log 정책을 설정하는 파일입니다.
  > private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)
  > classLogger.info("test")

  위와 같이 코드상으로 로그를 남기라고 함수를 사용했을 때, 로그의 출력은 어디에 할 것인지, 어떤 수준의 로그만 출력할 것인지 등의 출력 처리를 어떻게 하는지에 대한 설정을 하는 것입니다.
- 본 프로젝트에서는 springboot 프로필 별 설정을 따로 두었으며,
  로그 출력 위치, 파일로 로그를 저장할 때에 로그 파일 작명법 설정, 보관 일자 설정, 파일 분할 방식에 대해 설정하였습니다.
  필요하다면 에러 메시지를 Slack 의 특정 유저에게 발송하거나 이메일로 전송하는 등의 처리도 가능합니다.


### src/main/kotlin/{packageNames}/ApplicationMain.kt
- Framework 의 시작점으로, main 함수가 존재하는 파일입니다.
  본 프로젝트에서는 fun init() = CommandLineRunner 함수를 @Bean 으로 등록하였으며,
  이곳에서 프로젝트 실행 초기에 실행해야 하는 작업에 대해 작성해 두었습니다.
- 서버 타임존 설정은, GlobalVariables 이라는 파일 내의 Global 변수인 SYSTEM_TIME_ZONE 에
  설정한 타임존 설정을 사용하도록 하였습니다.
  런타임 설정 가져오기의 경우에는, 프로젝트에서 사용할 런타임에 수정이 가능한 설정 데이터를, by_product_files/runtime_config.json 파일 내에서 가져와서 메모리에 로드하는 작업으로,
  이에 대해서는 아래에서 자세히 설명하겠습니다.


### src/main/kotlin/{packageNames}/ApplicationScheduler.kt
- @Scheduled 어노테이션을 적용할 함수, 즉, 일정 시간에 맞춰서 실행될 스케쥴러 함수를 등록하는 파일입니다. 주석
  예시를 참고하여 원하는 설정으로 원하는 작업을 수행하도록 하면 됩니다.


### src/main/kotlin/{packageNames}/annotations
- 커스텀 Annotation 을 사용하고 싶을 때에는 이곳에 annotation class 를 모아두어 사용하세요.
- 본 프로젝트에서 지원하는 annotation 은, datasources 에 대한 트랜젝션 처리용 Annotation 을
  추가하여 사용중입니다.


### src/main/kotlin/{packageNames}/aop_aspects
- AOP 를 구현하기 위하여 @Aspect 어노테이션이 붙은 클래스를 저장하는 폴더입니다.
- 본 프로젝트에서 처리한 AOP Aspect 는, annotations 에 선언한 datasources 에 대한 각 트랜젝션
  처리를 구현한 코드가 존재합니다.


### src/main/kotlin/{packageNames}/configurations
- springboot 에서 사용하는 @Configuration 어노테이션이 붙은 설정 Bean 클래스를 모아두는 폴더입니다.
- 눈여겨볼 곳으로는 database_configs, mongo_db_configs, redis_configs 와 같은 설정 파일 모음의 서브 폴더입니다.  
  일반적으로 springboot 에서 database, mongoDB, Redis 와 같은 데이터 소스 접근용 라이브러리 설정은, 1개 프로젝트에 1개의 소스를 기본으로 합니다.
  1개의 프로젝트 내에 여러 소스에 접근하는 처리를 하기 위해선 일반적이지 않은 처리가 필요한 것이죠.
  그러한 처리를 하는 것이 바로 위와 같은 설정 파일들입니다.
- 저의 경우는 1개의 소스를 사용하느냐 복수개의 소스를 사용하느냐에 따라 추가 설정 및 트랜젝션 처리 등의 프로젝트 구조가 바뀌는 부분이 많다고 판단하여, **다른 프로젝트와의 구조적 통일성과 추후 코드 수정시의 용이성을 위해**, 설령 소스가 1개만 사용하더라도  이와 같이 멀티 소스 기반으로 구조를 만든 것입니다.


### src/main/kotlin/{packageNames}/controllers
- springboot 에서 실질적으로 네트워크 Request 를 받는 창구 역할을 하는 Controller 클래스를 모아두는
  폴더입니다.
- **본 프로젝트에서 컨트롤러의 분류는, 해당 컨트롤러가 담당하는 주소체계, 즉 @RequestMapping 어노테이션 설정에 따라 분리됩니다.**
  만약 해당 Controller 클래스가 @RequestMapping("/service1/tk/v1/request-test") 이렇게 설정이 된 상태라고 합시다.
  해당 컨트롤러의 API 들은 Springboot 에 들어오는 요청과, 그 응답에 대한 테스트를 하기 위한 기능을 담당합니다.
  이렇게 된다면, controllers 안의 서브폴더 명은,  
  {c or sc}{컨트롤러 생성 순서 일련번호}\_{RequestMapping 주소}
  이러한 규칙으로 작명하게 되므로, 기본적으로 존재하는 1번 컨트롤러를 제외하면 이 컨트롤러가 처음 만들어졌을 때가 2번째 컨트롤러가 되는 것이므로,
  c2_service1_tk_v1_requestTest
  이러한 서브폴더가 되는 것입니다.
- 위 작명법에서 c 와 sc 의 차이는, 간단히 말해서 Rest API 용으로 만들어진 것인지\(c\), Web API 로 만들어진 것인지(sc) 에 따라 나누는 것입니다.
  c는 간단하게 controller 의 약자를 생각하여 작명한 것이고, sc 는 Session/Cookie 를 사용하는 인증체계를 사용하는 controller 라는 의미에서 작명 했던 것이 굳어진 것입니다.
- 컨트롤러별 C1, C2 와 같이 일련번호를 붙인 이유는, 본 프로젝트가 Swagger API 를 사용하는데,
  이를 참고하는 클라이언트 분들과 소통할 때에 편하게 하기 위해서 입니다.
  예를들어,
  ""/service1/tk/v1/request-test" 의 api 를 수정해달라"
  이런 방식으로 주소를 사용하여 소통하는 것 보다는,
  "C1 의 N34 API 가 이상이 있다."  혹은 "api 1-34 가 이상이 있다."
  이런식으로 소통하는 것이 깔끔하리라 생각했기 때문입니다.
  고로, 서버 개발자는 이 일련번호를 한번 정해지고 공개되었다면 다음에는 바뀌어선 안될 고유 값으로 다뤄야 합니다.
- 앞서 지나쳤던, resources 의 (for_{api 코드}_{api 이름}) 형태의 작명법에 대해 여기서 짚고 넘어가겠습니다.
  여기서 api 코드는, 앞서 설명한 Controller 일련번호와 API 일련번호를 합친 것과 같습니다.
  for_c2_n15_return_text_html_test
  위와 같은 이름에서 c2_n15 를 방금 알게된 지식으로 해석하자면, 2번 컨트롤러의 15번 api 를 의미하는 것을 알 수 있죠.
  이렇게 하여 중복되지 않는 작명이 가능하며, 더불어 해당 리소스가 사용되는 출처도 빠르게 찾아낼 수 있는 것입니다.
  또한, api 이름의 경우는, 본 프로젝트의 Controller 클래스 내부의 코드를 보면, api 함수별로 일련번호 외에 이름이 적혀있는데, 중복 방지와 구분은 api 코드로 가능하다고 하더라도, 작명을 코드만으로 한다면 개발자들 사이의 해석이 어려워질 수 있으므로, 코드명 외에 알아보기 쉬운 함수명을 붙인 것입니다.
  요약하자면, **중복 방지를 위한 api 코드명과 해석을 위한 api 함수명을 합친 이름을 사용한 것**입니다.
- 앞서 설명한 기본적으로 생성되는 c1 에 대해 설명드립니다.
  c1의 컨트롤러에는 @RequestMapping 어노테이션이 붙어있지 않은데, 이는 c1 의 컨트롤러가 특정한 주소체계에 얽매이지 않는 다는 것을 의미합니다.
  대표적으로는, 서버에 127.0.0.1:8080 이렇게만 입력하고, 뒤에 어떠한 주소도 붙이지 않았을 때의 요청을 처리하는 루트 api 가 존재합니다.
  당연하게도 c1 에는 RequestMapping 주소 가 없으므로, 위에서 설명한 작명법에 따라, c1 은 그냥 c1 일 뿐입니다.
- c 나 sc 로 구분이 불가능한 컨트롤러도 존재합니다.
  현재로써는 springboot 에서 Websocket 의 STOMP 를 구현할 때에 사용하는 Controller 클래스가 그것입니다.
  이는 본 프로젝트에 작성된 것과 같이 websocket_stomp 라는 서브 폴더만이 존재합니다.
- 본 프로젝트에서 하나의 컨트롤러는 두개의 파일로 이루어집니다.
  Controller 파일과 Service 파일입니다.
  Controller 파일은, API 의 인터페이스를 작성하는 역할을 합니다.
  Controller 를 네트워크에서 오는 요청에 대한 민원 창구로 보았을 때,
  고객을 응대하고, 고객에게 정확한 요청 방식을 요구하는 창구 직원들을 모아둔 곳이라 생각하면 됩니다.
  Service 파일은, Controller 파일이 받아온 요청을 실질적으로 처리하는 내부 처리 부서라고 생각하시면 됩니다.
  이렇게 파일을 두 개로 나눠둔 이유는, 위와 같은 관점으로 파일을 나누었을 때, 각 파일에 작성되는 코드량이 거의 동일하게 분배가 되며, 각 파일이 처리하는 코드의 역할을 나눔으로써 코드가 깔끔해지고, 추후 유지보수에 도움이 되기 때문입니다.
- Controller 파일과 Service 파일의 작명법은, 바로 상위의 서브폴더 명을 파스칼 표기법으로 바꾸고, Controller 파일이라면 뒤에 Controller, Service 파일이라면 뒤에 Service 를 붙이는 방식으로 작명하면 됩니다.
  예를들어 c2_service1_tk_v1_requestTest 서브폴더의 각 파일은,
  C2Service1TkV1RequestTestController 와 C2Service1TkV1RequestTestService 로 작명할 수 있습니다.


### src/main/kotlin/{packageNames}/custom_classes
- Kotlin 의 Class 파일을 모아둔 폴더입니다.


### src/main/kotlin/{packageNames}/custom_components
- Springboot 에서 @Component 가 붙은 Bean 클래스 파일을 모아둔 폴더입니다.


### src/main/kotlin/{packageNames}/custom_objects
- Kotlin 의 Object 파일을 모아둔 폴더입니다.


### src/main/kotlin/{packageNames}/data_sources
- data_sources 폴더는, 프로젝트 내에서 사용할 다양한 데이터 소스들에 대한 코드를 모아둔 폴더입니다.
  네트워크, 데이터베이스, 로컬 파일, 메모리 등의 다양한 위치에 저장된 데이터에 대한 직접적인 접근은 이곳에 모인 파일들과 코드가 처리한다고 생각하면 됩니다.
- GlobalVariables 파일은, 프로젝트 전역에서 사용하는 전역 변수를 모아두는 파일입니다.
- RuntimeConfig 파일 역시 전역 변수를 모아두는 파일입니다. 자세한 내용은 아래의 추가 상세 설명에서
  설명하겠습니다.
- database_jpa, mongo_db_sources, network_retrofit2, memory_redis 는,
  각각 JPA 데이터베이스, MongoDB NoSQL 데이터베이스, Retrofit2 네트워크 요청 라이브러리, Redis In-Memory 데이터베이스에 대한 코드 모음용 서브폴더입니다.
- 이곳에 저장되는 database_jpa, mongo_db_sources, memory_redis 의 클래스 작명법은 조금 독특합니다.
  기본적으로 파스칼 표기법을 따름에도 '_' 로 구분이 되어있는 부분이 있는데,
  이는 본 프로젝트가 복수 개의 소스를 가정한 구조이기 때문입니다.
  예를들어 127.0.0.1:3306 주소의 데이터베이스와 127.0.0.1:3306 주소의 데이터베이스를 프로젝트 내에 동시에 설정했다고 합시다.
  두 데이터베이스 내에서 동일한 스키마, 동일한 테이블명이 존재할 수 있습니다.
  그러므로 스키마와 테이블명만으로 작명을 한다면 동일한 Bean 을 생성하게 되어 중복 에러가 날 것입니다.
  이를 방지하기 위하여, 데이터베이스에는 db{index}, mongodb 는 mdb{index}, redis 는 redis{index} 라는 접두사를 붙여서, 데이터베이스의 구분을 작명에 나타낸 것입니다.
  즉, data_sources 의 각 데이터베이스 타입별 서브폴더 작명법은,
  ** {데이터베이스 인덱스 접두사}\_{데이터베이스 이름}**
  으로 이루어져 있으며,
  그 안에 존재하는 각 클래스들은,
  **{데이터베이스 인덱스 접두사}_{스키마 이름}_{테이블 이름}**
  위와 같은 형식으로 이루어져 있습니다.
  클래스가 파스칼 표기법을 따르면서도 언더바를 사용하는 이유는, 어디까지가 데이터베이스 종류이고, 어디까지가 스키마 이름인지를 구분하기 쉽도록 분리하기 위해서입니다.
- 자세한 내용은 아래의 추가 상세 설명에서 설명하겠습니다.


### src/main/kotlin/{packageNames}/filters
- Springboot Filter 들을 모아둔 폴더입니다.
  본 프로젝트에는 /actuator 로 시작되는 주소에 대한 화이트리스트 접근 처리,
  모든 Request 에 대해, Request 와 Response 를 자동으로 로깅하도록 처리한 로깅 필터가 존재합니다.


### src/main/kotlin/{packageNames}/kafka_consumers
- Kafka Consumer 코드를 저장하는 폴더입니다.
  자세한 내용은 아래의 추가 상세 설명에서 설명하겠습니다.


### src/main/kotlin/{packageNames}/web_socket_handlers
- STOMP 를 제외한 WebSocket 처리 핸들러를 모아둔 폴더입니다.
  configurations 의 WebSocketConfig 에서 addHandler 를 할 때에 연결할 핸들러들을 만들어 이곳에 저장합니다.


## 코딩 규칙

### 표기법 정리
- 폴더명은 snake 표기법을 사용합니다. (ex : test_folder)
- Kotlin 파일명은 파스칼 표기법을 사용합니다. (ex : TestClass)
- Kotlin 파일명 작명 규칙에 예외가 존재합니다.
  src/main/kotlin/{project}/data_sources 폴더 안의
  database_jpa, mongo_db_sources, memory_redis 안의 파일명은 파스칼 표기법을 따르면서도, 언더바(_) 가 포함됩니다.(ex : Db0_TestSchema_TestTable)
- Kotlin 코드 내의 변수명, 함수명, 클래스명 등의 명명 규칙은 Kotlin 코딩 규칙을 따르며, 예외가 있을 시 이곳에 표시할 것입니다.
- application.yml 의 key 는 케밥 표기법을 사용합니다. (ex : test-key)


### 개발 참고사항
- 날짜 데이터 관련하여 기본 형태는 **yyyy_MM_dd_'T'_HH_mm_ss_SSS_z** 형태를 기본으로 하여, 상황에 따라 조절하세요. 글로벌 서비스를 대비하여 타임존은 필수로 표기하면 좋습니다.
- 본 프로젝트에서 자동 로깅 필터는 multipart/form-data 형식의 Request Body 는 로깅하지 않습니다.
  이 경우에는 수동으로 로깅 처리를 해주셔야 합니다.

## 주의사항
- 본 프로젝트는 Stateless 를 지향합니다.<br>
  전역 변수이며, 런타임에 수정이 필요한 변수를 사용시에는 주의해야하며,
  변수 수정시 Kafka 를 사용하여 Produce 하고, 이를 Consume 해서 변수에 할당하도록 처리하세요.
- 본 프로젝트에서 주의해야 하는 전역변수는 아래와 같습니다.<br><br>

  TestWebSocketHandler 의 webSocketSessionHashMap<br>
  SseEmitterWrapper 의 emitterMap, eventHistoryMap<br>
  SseClient 의 client 접속 처리<br>
  WebSocketStomp 접속자 정보<br><br>

  ScaleOut 을 위한 프로젝트 복제시 문제가 생기지 않도록 처리하세요.

## 추가 상세 설명