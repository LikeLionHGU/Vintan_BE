
# Vintan (빈땅) - AI 상권 분석 플랫폼 (Backend) 🚀

<p align="center">
  <img src="https://raw.githubusercontent.com/hyeokkiyaa/Vintan_FE/main/public/logo.png" alt="Vintan Logo" width="200"/>
</p>

<p align="center">
  <strong>"사장님, 그 가게 정말 괜찮을까요?" Vintan이 AI로 미리 알려드립니다.</strong>
</p>

<p align="center">
    <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white">
    <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white">
    <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
    <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white">
    <img src="https://img.shields.io/badge/Github%20Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white">
</p>

**Vintan**은 예비 창업자들이 데이터에 기반한 합리적인 의사결정을 내릴 수 있도록 돕는 AI 상권 분석 플랫폼입니다. 멋쟁이사자처럼 13기 중앙 해커톤에서 **한동대학교 멋쟁이사자처럼 '먹짱'팀**이 개발했습니다. 이 저장소는 Vintan의 백엔드 서버입니다.

---

## ✨ 주요 기능

Vintan은 다음과 같은 핵심 기능들을 API로 제공합니다.

*   **🤖 AI 상권 분석 리포트**
    *   사용자가 지정한 위치와 업종에 대한 종합 분석 리포트를 생성합니다.
    *   **유동인구 분석**: 주변 버스 정류장 정보를 활용하여 유동인구를 추정합니다.
    *   **경쟁사 분석**: 카카오맵 API를 기반으로 주변 경쟁사 정보를 분석합니다.
    *   **접근성 분석**: 주변 교통 편의성 등 입지 접근성을 평가합니다.
    *   **종합 의견 및 점수**: Gemini API를 활용하여 분석 데이터를 기반으로 한 종합적인 의견과 최종 점수를 제공합니다.

*   **🗣️ 커뮤니티**
    *   **Q&A 게시판**: 창업과 관련된 질문과 답변을 주고받는 공간입니다.
    *   **블라인드 게시판**: 익명으로 자유롭게 소통할 수 있는 공간입니다.

*   **👤 사용자 기능**
    *   회원가입, 로그인 등 기본적인 사용자 인증 기능을 제공합니다.
    *   **마이페이지**: 자신이 작성한 게시글과 발급받은 AI 리포트 목록을 확인할 수 있습니다.

## 🛠️ 기술 스택

*   **Framework**: Spring Boot
*   **Language**: Java 17
*   **Build Tool**: Gradle
*   **Database**: (사용하시는 DB를 여기에 적어주세요, 예: MySQL, PostgreSQL)
*   **External APIs**:
    *   Google Gemini API
    *   Kakao Map API
    *   Pohang Bus API (공공데이터포털)

## 📖 API 명세서

전체 API에 대한 자세한 명세는 아래 링크에서 확인하실 수 있습니다.

➡️ **[Vintan API 명세서 바로가기](./api_명세서.md)**

## ⚙️ 시작하기

### 1. 프로젝트 클론

```bash
git clone https://github.com/your-github-id/Vintan_BE.git
cd Vintan_BE
```

### 2. `application.properties` 설정

`src/main/resources/` 경로에 `application.properties` 파일을 생성하고, 아래 내용을 참고하여 외부 API 키 등 필요한 환경 변수를 설정해야 합니다.

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:your_database_url
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update

# External API Keys
gemini.api.key=your_gemini_api_key
kakao.api.key=your_kakao_api_key
pohang.bus.api.key=your_pohang_bus_api_key

# Spring Security & JWT
jwt.secret.key=your_jwt_secret_key
```

### 3. 빌드 및 실행

```bash
# Gradle을 사용하여 프로젝트 빌드
./gradlew build

# 빌드된 JAR 파일 실행
java -jar build/libs/Vintan-0.0.1-SNAPSHOT.jar
```

서버가 정상적으로 실행되면 `http://localhost:8080`에서 접근할 수 있습니다.

## 🤝 기여자

<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/hyeokkiyaa"><img src="https://avatars.githubusercontent.com/u/80053442?v=4" width="100px;" alt="hyeokkiyaa"/><br /><sub><b>hyeokkiyaa</b></sub></a><br /></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/HaeseokPark"><img src="https://avatars.githubusercontent.com/u/122941953?v=4" width="100px;" alt="HaeseokPark"/><br /><sub><b>HaeseokPark</b></sub></a><br /></td>
    </tr>
  </tbody>
</table>

## 📝 커밋 컨벤션

Vintan 프로젝트는 다음과 같은 커밋 메시지 컨벤션을 따릅니다.

*   `feat`: 새로운 기능 추가
*   `fix`: 버그 수정
*   `refactor`: 코드 리팩토링
*   `update`: 기능 수정 (버그 수정이나 리팩토링이 아닌 경우)
*   `docs`: 문서 수정
*   `style`: 코드 포맷팅, 세미콜론 누락 등 (코드 변경이 없는 경우)
*   `chore`: 빌드 관련 파일 수정, 패키지 매니저 설정 등

> 예시: `feat: AI 리포트 생성 기능 추가 (#12)`

---

*This project was created by Team '먹짱' from Handong Global University for the LikeLion 13th Central Hackathon.*
