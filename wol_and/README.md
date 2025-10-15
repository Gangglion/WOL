# WOL 프로젝트 - Android 클라이언트
Android 앱은 홈 네트워크 장비의 전원 제어와 상태 모니터링을 담당하며,  
**Clean Architecture + MVVM + Flow 기반 반응형 구조**로 설계되었습니다.

앱은 전체 WOL 시스템에서 클라이언트 역할을 수행하며, 서버와 안전하게 통신하고  
장치 상태를 실시간으로 UI 에 반영합니다.

## 🛠️ 설계 의도
- 기능 구현 중점이 아닌, 유지보수성과 확장성을 고려하며 Modern Android 개발 패러다임 적용을 목표로 했습니다.
- 각 Layer 간 의존성을 명확히 분리하고, UseCase 단위로 비즈니스 로직을 관리하였습니다.
- 각 Flow 기반의 데이터 스트림 처리를 통해 실시간 UI 업데이트를 가능하게 했습니다.

## 🎯 기술적 특징 요약
* **Clean Architecture + MVVM** : UI/Domain/Data 계층 명확하게 분리, 유지보수성 및 확장성 확보
* **Coroutine + Flow** : 비동기 통신과 반응형 UI 구현
* **Room + DataStore** : 로컬 데이터 안정적 저장 및 동기화
* **Retrofit2 + JWT 인증** : 서버 통신 및 토큰 갱신 로직
* **Firebase Cloud Messaging** : 서버에서 오는 장치 상태 알림 처리
* **Jetpack Compose UI** : 모듈화된 화면 설계 및 재사용성 강화
* **Crypto 모듈 활용** : 암호화 모듈 적용으로 민감 데이터 안전하게 전송

## ⚡ 문제 상황 & 해결 과정
1. **서버 통신 시 민감 데이터 보호**
    - **문제** : 장치 식별 정보를 평문으로 전송하면 외부에서 확인될 수 있음
    - **원인** : 초기 서버 통신이 암호화 되지 않은 상태였음
    - **해결** : RSA/AES 하이브리드 암호화를 적용 및 서버에 SSL 구성
    - **결과** : 민감 데이터가 안전하게 보호되고, 외부 노출 위험 감소
2. **JWT AccessToken 갱신 시 Race Condition 및 만료 문제**
    - **문제** : 여러 번 refreshToken 시 갱신된 Token 이 적용되지 않고 이전 토큰 사용
    - **원인** : Race condition 발생으로 인해 여러 스레드가 동시에 토큰 값을 수정
    - **해결** : Mutex 와 synchronized 사용하여 한 스레드만 토큰 갱신 가능하도록 제한
    - **결과** : 토큰이 여러 번 갱신되더라도 항상 최신 토큰으로 인증 수행
3. **장치 상태 UI 실시간 반영 문제**
    - **문제** : 서버에서 받은 장치 상태를 DB 에 업데이트 한 뒤, UI 갱신을 위해 명시적 Select 호출 필요
    - **원인** : 기존 suspend 조회 방식으로 데이터가 일회성으로만 조회됨
    - **해결** : Coroutine + Flow를 활용하여 Room 데이터를 UI 가 구독하도록 구조 변경
    - **결과** : 데이터 변경 시 자동으로 UI 에 반영되어 명시적 Select 불필요
4. **앱 종료 상태에서 Push Data 수신 문제**
    - **문제** : 앱이 종료된 상태에서는 서버에서 보내는 장치 상태 알림이 UI 에 반영되지 않음  
    - **원인** : 앱이 꺼진 상태에서 복호화 할 수 있는 키를 가져오지 못함
    - **해결** : 키 값을 안전하게 불러와 복호화 할 수 있도록 crypto-module 수정  
    - **결과** : 앱 접속 여부 상관없이 Push 수신 시 변경된 기기 상태 반영

## 📂 프로젝트 구조
Clean Architecture 기반의 3-Layer 구조로 구성되어 있습니다.  
각 Layer 는 역할에 따라 명확히 분리되어 있습니다.
```
wol_and/
    ├─ data # Data Layer 입니다.
    │   ├─ api/ # API 통신 관련 DTO, DataSource, Service, Interceptor 등이 정의되어 있습니다.
    │   ├─ auth/ # Authorization AccessToken 을 관리를 담당합니다. 
    │   │          TokenManager 및 Interceptor, Authenticator 가 정의되어 있습니다.  
    │   ├─ crypto/ # 암복호화 시 사용되는 키 관련 DataSource 가 정의되어 있습니다.
    │   ├─ datastore/ # 앱 내부저장소(DataStore) 관련 Key, DataSource 가 정의되어 있습니다.
    │   ├─ db/ # Room Database 관련 Entity, Dao, DataSource 가 정의되어 있습니다.
    │   ├─ mapper/ # DTO <-> Model 변환을 담당하는 Mapper 코드가 위치합니다.
    │   └─ repository/ # Repository 구현체가 위치합니다. Domain Layer 의 Interface 를 구현합니다.
    ├─ di/ # hilt 의존성 주입 모듈입니다.
    │        Retrofit, Room Database, Repository, CoroutineScope 등의 객체를 주입합니다.
    ├─ domain/ # Domain Layer 입니다.
    │   ├─ model/ # UI layer 에서 사용될 데이터 Model 입니다.
    │   ├─ repository/ # Repository Interface 가 정의되어 있습니다. (Data Layer 에서 구현)
    │   └─ usecase/ # 비즈니스 로직을 담당하는 UseCase 입니다. 
    │                 결과를 FlowResult(Success / Error / Loading) 타입으로 반환합니다.
    ├─ ui/ # UI(Presentation) Layer 입니다.
    │        ViewModel, Compose UI, Navigation 코드가 포함되어 있습니다.
    ├─ util/ # 앱 전역에서 사용되는 Extension, Util 함수들이 정의되어 있습니다.
    ├─ FCMService # FirebaseMessagingService 를 상속받아 Push 알림을 처리합니다.
    ├─ MainActivity.kt # 앱의 메인 MainActivity. 전체 Navigation 그래프를 관리합니다.
    └─ WolApp # Application 클래스. 앱의 진입점입니다.
crypto-module/ # 암복호화 관련 경량 모듈입니다.
               # Android Project 와 분리되어 있으며, RSA/AES 키 관리와 복호화 유틸을 제공합니다.
```

## 🔗 Crypto Module
암호화 관련 기능은 별도 Repository 로 관리됩니다.  
해당 모듈은 KeyGenerate, Encrypt, Decrypt 를 담당하는 경량 모듈입니다.  
[crypto 모듈 바로가기](https://github.com/Gangglion/CryptoModule)