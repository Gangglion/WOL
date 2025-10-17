# power-on/off JAR
홈 네트워크 장비의 작업 스케쥴러에 추가하여 ***부팅 시*** / **시스템 종료 시** 시 지정된 서버로 상태를 전송하는 Kotlin 프로젝트 입니다.

## 설치 및 실행
이 프로그램을 실행하려면, `java 17` 이상이 설치되어 있어야 합니다.
1. **자바 버전 확인**
    ```
    java -version

    // 결과 예시
    openjdk version "17.0.11" 2024-04-16
    OpenJDK Runtime Environment JBR-17.0.11+1-1312.2-nomod (build 17.0.11+1-b1312.2)
    OpenJDK 64-Bit Server VM JBR-17.0.11+1-1312.2-nomod (build 17.0.11+1-b1312.2, mixed mode, sharing)
    ```
2. **프로젝트 클론 및 빌드**
    ```
    ./gradlew shadowJar
    ```
    빌드가 완료되면 /build/libs/ 디렉토리에 jar 파일이 생성됩니다.

3. **jar 실행**  
    jar 파일을 원하는 경로로 복사한 뒤, 요청을 보낼 API URL 을 인자로 전달하여 실행합니다.
    ```
    // 예시 : http://localhost:8080/powerStatus
    java -jar power-off-1.0-SNAPSHOT-all.jar http://localhost:8080/powerStatus
    java -jar power-on-1.0-SNAPSHOT-all.jar http://localhost:8080/powerStatus
    ```
    jar 파일의 이름은 자유롭게 변경 가능합니다.

## Windows 작업 스케쥴러 추가 방법
> macOS / Linux 환경에서는 각 운영체제에 맞는 스케쥴러(cron, systemd 등) 을 사용하세요.
### 부팅 시 실행되는 작업
1. `Win + R` -> `taskschd.msc` 입력 후 작업 스케쥴러를 실행합니다.
2. 오른쪽 **"작업 만들기"** 클릭
3. **일반 탭**
    * 작업 이름을 지정
    * **"사용자의 로그온 여부에 관계없이 실행"** 체크
    * `가장 높은 수준의 권한으로 실행` 체크
4. **트리거 탭**
   * **"새로 만들기"** -> **"작업 시작"** 을 **"시작할 때"** 로 설정
5. **동작 탭**
    * **"새로 만들기"** -> "동작" 을 `프로그램 시작` 으로 설정
    * **프로그램/스크립트** : JDK 가 설치된 경로의 `java.exe` 선택
    * **인수 추가** : 
        ```
        -jar power-on-1.0-SNAPSHOT-all.jar http://localhost:8080/powerStatus
        ```
    * **시작 위치** : jar 가 위치한 폴더 지정
6. **확인 후 암호 입력**
    * 암호는 Windows 계정(Microsoft 계정 포함) 의 로그인 암호입니다.

### 종료(Shutdown) 시 실행되는 작업
1. 1~3번은 부팅 시 작업 추가와 동일합니다.
2. **트리거 탭**
    * **"새로 만들기"** 클릭
    * **"작업 시작"** -> **"이벤트 상태"** 로 설정
   * 설정 : 기본
   * 로그 : 시스템
   * 원본 : User32
   * 이벤트 ID : 1074
        ```
        User32 : 사용자 세션 관련 이벤트(로그온 / 로그오프 / 종료 등)
        1074 : 사용자가 종료/재시작/로그오프 등의 이유로 시스템을 종료했음을 의미
        ```
        >이 설정은 Windows 가 종료, 재시작, 로그오프 등의 이류로 시스템 셧다운을 시작할 때 트리거 되도록 설정합니다.
3. 동작탭
    * 부팅 시 실행되는 작업 추가와 동일하되, 실행할 jar 파일만 `power-off-1.0-SNAPSHOT-all.jar` 로 변경합니다.
  
## 참고
* `power-on` / `power-off` jar 의 실행인자는 API 엔드포인트 URL 입니다.
* 서버에서 해당 요청을 처리하여 장비 상태를 Firebase Cloud Messaging 으로 Android 기기에 전송해줍니다.