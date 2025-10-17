# wol-server(Ktor)
Jetbrains의 공식 프레임워크인 Ktor 을 사용하여 개발된 WOL 서버 애플리케이션 입니다.  
홈 네트워크 내 장비의 전원 제어, 상태 수신, Firebase Cloud Messaging(FCM) 전송을 담당합니다.

## 개요
`wol-server` 는 WOL 시스템의 백엔드로,
Android 앱과 홈 네트워크 장비(Jar 클라이언트) 간의 **중계 역할**을 수행합니다.

전체 동작 흐름은 다음과 같습니다.
1. Android 앱 -> 서버  
   사용자가 전원 켜기(WOL) 을 요청하면, 서버가 해당 장비로 매직 패킷을 전송합니다.
2. 장비 -> 서버  
   장비가 부팅 완료 시, 서버에 "전원 켜짐" 상태를 전송합니다.  
   장비의 전원이 꺼질 경우, 서버에 "전원 꺼짐" 상태를 전송합니다.
3. 서버 -> Android 앱  
   서버는 장비의 상태를 수신하면, Firebase Cloud Messaging(FCM) 을 통해 Android 앱에 알림을 보냅니다.

## 기술 스택
|       항목        |           사용 기술        |
|:---------------:|:--------------------------:|
|    Language     |           Kotlin           |
|    Framework    |  [Ktor](https://ktor.io/)  |
|  Communication  |          REST API          |
|    Messaging    |  Firebase Cloud Messaging  |
|  Authorization  |         JWT Token          |
|    Security     |     AES / RSA 암호화 키 관리 |


## 설치 및 실행
이 프로그램을 실행하기 위해, `Java 17` 이상이 설치되어 있어야 합니다.  
또한, http 를 지원하지 않으며, 사용 시 SSL 인증서 정보를 jar 실행 시 주입해주어야 합니다.  
실행포트는 `resources/application.yaml` 에서 변경 가능합니다.

사용 시 FirebaseCloudMessaging 의 계정 키 json 파일을 `resources` 하위에 넣어주어야 push 기능이 동작합니다.

.env 를 루트경로에 생성하여 키가 저장될 폴더의 경로, 사용중인 Iptime 공유기의 주소, 계정, JWT 관련 정보들을 넣어주어야 합니다.

WOL 동작은 기본적으로 wol-server -> iptime 공유기에 WOL 기능 요청 의 순서로 동작하기에, 본 서버 애플리케이션은 iptime 공유기와  
동일한 대역폭에서 실행되어야 합니다.