# WOL (Wake-On-LAN) 프로젝트
홈 네트워크 상의 장비를 원격으로 제어하고, 상태를 관리하기 위한 멀티플랫폼 Wake-On-Lan 시스템  
**Modern Android Architecture (Clean Architecture + MVVM + Flow)를 적용하여 반응형 구조로 설계한 개인 프로젝트** 입니다.

## 🧭 프로젝트 개요
집에 있는 데스크탑이나 NAS 등의 홈 네트워크 장비를 외부에서도 제어할 수 있도록 설계한 프로젝트 입니다.    
단순히 WOL 패킷을 보내는 수준이 아니라,  
**Android 앱 - 서버 - Kotlin 클라이언트** 간의 통신 구조를 직접 설계하여
외부 네트워크 환경에서도 보안이 확보된 전원 제어 및 상태 모니터링이 가능하도록 구현했습니다.  
이를 통해 Android 의 Clean Architecture + MVVM 패턴과  
RSA / AES 하이브리드 암호화 구조를 적용한 안전한 홈 네트워크 관리 시스템을 완성했습니다.

## 🚀 주요 목표
* **Modern Android 개발 패러다임인** Clean Architecture + MVVM 구조 적용
* **외부 네트워크 환경에서도 안전하게** 홈 네트워크 내 장비의 전원 제어
* **서버를 중계 노드로 활용**하여 WOL 패킷을 안전하게 송신
* **FCM 알림 + Room DB 연동**을 통해 실시간 상태를 UI 에 반영

## ⚙️ 시스템 구성
```
/
├─ wol_and/        # Android 앱 (클라이언트)
├─ wol_server/     # Kotlin Ktor 기반 서버
├─ wol_jar/        # 클라이언트 전원상태 전송용 jar 실행 파일
```

## 🛠️ 기술 스택
| 영역      | 기술                                        |
| ------- | ----------------------------------------- |
| 언어      | Kotlin                              |
| Android | Jetpack (Compose, Hilt, Navigation, DataStore, Room), Retrofit2, FCM, Coroutine, Flow       |
| Server  | Kotlin (Ktor), Let’s Encrypt, FCM, JWT       |
| Device Client  | Kotlin (JAR Application)
| Infra   | NAS (Synology, Docker Container)              |

### 📱 Android (Android Native)
* Kotlin + Clean Architecture 기반 MVVM 구조
* Jetpack Compose 로 UI 구성
* Room DB + DataStore 로 로컬 데이터 저장
* Coroutine + Flow 를 통한 반응형 프로그래밍
* Retrofit2 + JWT 인증을 이용한 서버 통신 및 토큰 갱신 로직 구현
* Firebase Cloud Messaging (FCM) 으로 서버로부터 기기 상태 알림 수신
* RSA/AES 하이브리드 암호화 구조를 적용하기 위한 유틸 모듈 직접 구현
  * encrypt / decrypt / key generation 기능 중심의 경량 모듈로, 재사용성과 프로젝트 간 확장성을 고려하여 분리 설계

### 🖥️ Server (Ktor)
* 클라이언트 요청 수신 -> WOL 패킷 전송
* RSA/AES 하이브리드 암호화로 안전한 데이터 송수신
* JWT 기반 사용자 인증 및 토큰 발급
* HTTPS(SSL) 환경 구성 (Let’s Encrypt 인증서 적용)
* Kotlin 클라이언트로부터 장치 상태 수신 -> FCM 전송
* Docker Container 환경에서 NAS 위에 지속적으로 구동

### 💻 Device Client (Kotlin Application)
* JAR 형태의 실행 애플리케이션, 전원을 제어할 장비에서 직접 실행
* Windows 작업 스케쥴러 / Synology 스케쥴러에 등록 가능
* 장치 상태(전원 ON/OFF)를 감지하여 서버로 Push 요청 전송

## 🏆 주요 성과
* Clean Architecture + MVVM 패턴 적용
  * UI / Domain / Data 계층을 명확히 분리하여 유지보수성과 확장성 확보
  * ViewModel 과 Flow 를 활용하여 데이터 변경 시 UI 가 자동 반응하도록 구현
* Android 비동기/반응형 구조 구현
  * Coroutine + Flow 를 이용해 Room DB 와 DataStore 기반 로컬 데이터를 효율적으로 처리
  * 서버 통신과 UI 반응을 안정적으로 연결
* 서버 - 클라이언트 - 디바이스 통합 설계 경험
  * Ktor 서버와 Kotlin 클라이언트, Android 앱 간 End-to-End 통신 구조 설계
  * JWT 인증 및 FCM 기반 실시간 알림 연동
* 인프라 운영 경험
  * NAS Docker Container 기반 서버 구동 및 SSL(HTTPS) 환경 구성
  * 실제 홈 네트워크 환경에서 안정적으로 동작하는 엔드 투 엔드 시스템 구축
* 재사용성과 확장성을 고려한 모듈화
  * 암호화 기능을 모듈화하여 추후 프로젝트에서도 재사용 가능

## 🙏 개선 방향
* NAS 의 Docker Container 상에서 서버가 구동되어 NAS 가 꺼져있으면 서비스가 중단되는 제약 존재  
    -> 차후 Raspberry Pi 기반의 독립 중계 서버를 도입하여 NAS 의 전원까지 제어 가능하도록 확장 예정
* CI/CD 자동화 미비  
  -> Github Actions 기반 APK 자동 빌드 및 배포 파이프라인 구축 계획

## 💻 기여
* 개발자 : Gangglion(본인)
* 역할 : 전체 시스템 아키텍처 설계 및 구현
  * Android 앱 (클라이언트)
  * Ktor 서버 (백엔드)
  * Kotlin 기반 디바이스 클라이언트
  * NAS 기반 인프라 환경 구축(Docker)
* 개발 기간
  * 초기 프로토타입 : 2025.03.11 ~ 03.12
  * 본 개발 : 2025.09 ~ 2025.10.15

## 스크린샷
<table>
<tr>
  <td><img src="https://host.ggoggo.duckdns.org/WOL/1_home_empty.jpg" width="100"></td>
  <td><img src="https://host.ggoggo.duckdns.org/WOL/1-1_drawer.jpg" width="100"></td>
  <td><img src="https://host.ggoggo.duckdns.org/WOL/1-2_drawer_device.jpg" width="100"></td>
</tr>
<tr>
  <td><img src="https://host.ggoggo.duckdns.org/WOL/2-2_setting_limit.jpg" width="100"></td>
  <td><img src="https://host.ggoggo.duckdns.org/WOL/2-3_setting_add_complete.jpg" width="100"></td>
  <td><img src="https://host.ggoggo.duckdns.org/WOL/2-4_delete.jpg" width="100"></td>
</tr>
<tr>
  <td><img src="https://host.ggoggo.duckdns.org/WOL/3_request_wakeup.jpg" width="100"></td>
  <td><img src="https://host.ggoggo.duckdns.org/WOL/3-1_power_on.jpg" width="100"></td>
  <td><img src="https://host.ggoggo.duckdns.org/WOL/3-2_power_off.jpg" width="100"></td>
</tr>
</table>