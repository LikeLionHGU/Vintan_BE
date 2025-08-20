# Vintan API 명세서

이 문서는 Vintan 백엔드 API의 사양을 정의합니다.

---

## 👤 User Controller
- **Base URL:** `/auth/login`

### 1. [POST] /register
- **설명:** 신규 유저를 등록합니다.
- **Request Body:**by
```json
{
    "id": "exampleUser",
    "name": "김민준",
    "password": "password123!",
    "email": "example@email.com",
    "businessNumber": 1234567890
}
```
- **Response Body (Success):**
```json
{
    "isRegistered": 1
}
```

### 2. [GET] /duplicate/{userId}
- **설명:** 사용자 ID의 중복 여부를 확인합니다.
- **Path Variable:** `userId` (String) - 확인할 사용자 ID
- **Response Body:**
```json
{
    "isDuplicated": 1
}
```

### 3. [POST] /login
- **설명:** 사용자가 시스템에 로그인하고 세션을 생성합니다.
- **Request Body:**
```json
{
    "id": "exampleUser",
    "password": "password123!"
}
```
- **Response Body (Success):**
```json
{
    "isLogin": 1
}
```

### 4. [POST] /logout
- **설명:** 사용자를 로그아웃 처리하고 세션을 무효화합니다.
- **Response Body (Success):**
```json
{
    "isLogout": 1
}
```

### 5. [GET] /session
- **설명:** 현재 로그인된 사용자의 세션 정보를 확인합니다. (로그인 여부, 사업자 여부)
- **Response Body (Success):**
```json
{
    "isLogin": 1,
    "isBusiness": 1
}
```

---

## 🤖 AI Report Controller
- **Base URL:** `/ai/reports`

### 1. [POST] /generate/{regionId}
- **설명:** 특정 지역에 대한 AI 상권 분석 보고서를 생성합니다. (로그인 필요)
- **Path Variable:** `regionId` (Long) - 지역 ID
- **Request Body:**
```json
{
    "address": "포항시 북구 장량로 20",
    "categoryCode": "CE7",
    "pyeong": 15.5,
    "userDetail": "1층 코너 자리 원해요"
}
```
- **Response Body (Success):**
```json
{
    "reportId": 1
}
```

### 2. [GET] /{reportId}
- **설명:** 생성된 AI 상권 분석 보고서의 상세 내용을 조회합니다.
- **Path Variable:** `reportId` (Long) - 보고서 ID
- **Response Body (Success):**
```json
{
    "id": 1,
    "address": "포항시 북구 장량로 20",
    "category": "카페",
    "finalScore": {
        "floatingPopulationScore": 85,
        "accessibilityScore": 90,
        "competitionScore": 75,
        "overallReviewScore": 80,
        "totalScore": 82
    },
    "competitors": [
        {
            "name": "경쟁업체 A",
            "address": "포항시 북구 장량로 22",
            "description": "유사한 컨셉의 카페"
        }
    ],
    "competitorSummary": "반경 500m 내에 3개의 경쟁업체가 있으며, 평균 평점은 4.2점입니다.",
    "accessibilityAnalysis": {
        "summary": "주요 도로에 인접해 있어 차량 접근성이 우수하며, 버스 정류장이 도보 3분 거리에 위치합니다.",
        "parkingPrice": "공영 주차장 시간당 1000원",
        "landmark": "장량초등학교",
        "publicTransport": "101번, 105번 버스",
        "stationInfo": "장량동 주민센터 정류장",
        "score": 90
    },
    "floatingPopulationAnalysis": {
        "summary": "주중에는 직장인 유동인구가 많고, 주말에는 가족 단위 방문객이 많습니다.",
        "weekdayAnalysis": "평일 평균 5000명",
        "weekendAnalysis": "주말 평균 8000명",
        "nearbyFacilities": "오피스 빌딩, 아파트 단지",
        "ageGroup": "20-40대",
        "score": 85
    },
    "generalOverviewReport": {
        "summary": "전반적으로 긍정적인 평가가 많으며, 특히 청결도와 접근성에서 높은 점수를 받았습니다.",
        "positive": "깨끗하고 넓은 공간, 친절한 직원",
        "negative": "주말에 다소 혼잡함",
        "score": 80,
        "averageCommunityScore": 4.5,
        "averageCleannessScore": 4.8,
        "averagePopulationScore": 4.3,
        "averageReachabilityScore": 4.7,
        "averageRentFeeScore": 3.9
    },
    "finalReportReviewSummary": "종합적으로 볼 때, 해당 입지는 카페 창업에 매우 유리한 조건을 갖추고 있습니다."
}
```

---

## 💬 Community Ask Controller
- **Base URL:** `/regions`

### 1. [GET] /
- **설명:** Q&A 게시판의 전체 질문 목록을 조회합니다.
- **Response Body (Success):**
```json
{
    "askList": [
        {
            "id": 1,
            "title": "여기 상권 어떤가요?",
            "userId": "user123",
            "numberOfComments": 3,
            "date": "2024.08.20"
        }
    ]
}
```

### 2. [GET] /{postId}
- **설명:** 특정 Q&A 게시글의 상세 내용을 조회합니다.
- **Path Variable:** `postId` (Long) - 게시글 ID
- **Response Body (Success):**
```json
{
    "id": 1,
    "title": "여기 상권 어떤가요?",
    "content": "포항시 북구 장량동에 카페 창업을 고민중입니다. 이 지역 상권에 대해 아시는 분 계신가요?",
    "userId": "user123",
    "date": "2024.08.20",
    "commentList": [
        {
            "id": 101,
            "userId": "expert",
            "content": "장량동은 최근에 유동인구가 많이 늘어난 곳이라 괜찮아보여요.",
            "date": "2024.08.21"
        }
    ]
}
```

### 3. [POST] /
- **설명:** 새로운 Q&A 질문을 작성합니다. (로그인 필요)
- **Request Body:**
```json
{
    "title": "새로운 질문입니다.",
    "content": "이 지역의 임대료는 어느정도 수준인가요?"
}
```
- **Response Body (Success):**
```json
{
    "isSuccess": 1
}
```

### 4. [POST] /{postId}/comments
- **설명:** 특정 Q&A 게시글에 댓글을 작성합니다. (로그인 필요)
- **Path Variable:** `postId` (Long) - 게시글 ID
- **Request Body:**
```json
{
    "comment": "제가 알기로는 평당 10만원 선입니다."
}
```
- **Response Body (Success):**
```json
{
    "isSuccess": 1
}
```

---

## 🕶️ Community Blind Controller
- **Base URL:** `/regions/{regionId}/community/blind/reviews`

### 1. [POST] /
- **설명:** 특정 지역에 대한 블라인드 리뷰를 작성합니다. (로그인 필요)
- **Path Variable:** `regionId` (Long) - 지역 ID
- **Request Body:**
```json
{
    "title": "양덕동 카페거리 솔직 후기",
    "positive": "분위기 좋은 카페가 많고 주차가 편리함.",
    "negative": "주말에는 사람이 너무 많아서 자리가 없음.",
    "address": "포항시 북구 양덕동",
    "categoryRate": {
        "cleanness": 5,
        "people": 4,
        "reach": 5,
        "rentFee": 3
    }
}
```
- **Response Body (Success):**
```json
{
    "isSuccess": 1
}
```

### 2. [PATCH] /{reviewId}
- **설명:** 기존 블라인드 리뷰를 수정합니다. (로그인 및 작성자 확인 필요)
- **Path Variables:** `regionId` (Long), `reviewId` (Long)
- **Request Body:** (수정할 필드만 포함)
```json
{
    "title": "양덕동 카페거리 솔직 후기 (수정)",
    "positive": "분위기 좋은 카페가 많고 주차가 편리함. 평일 방문 추천.",
    "negative": "주말에는 사람이 너무 많아서 자리가 없음.",
    "categoryRate": {
        "cleanness": 5,
        "people": 3,
        "reach": 5,
        "rentFee": 3
    }
}
```
- **Response Body (Success):**
```json
{
    "isSuccess": 1
}
```

### 3. [DELETE] /{reviewId}
- **설명:** 블라인드 리뷰를 삭제합니다. (로그인 및 작성자 확인 필요)
- **Path Variables:** `regionId` (Long), `reviewId` (Long)
- **Response Body (Success):**
```json
{
    "isSuccess": 1
}
```

### 4. [GET] /{reviewId}
- **설명:** 특정 블라인드 리뷰의 상세 내용을 조회합니다.
- **Path Variables:** `regionId` (Long), `reviewId` (Long)
- **Response Body (Success):**
```json
{
    "totalRate": 4.25,
    "blind": {
        "title": "양덕동 카페거리 솔직 후기",
        "positive": "분위기 좋은 카페가 많고 주차가 편리함.",
        "negative": "주말에는 사람이 너무 많아서 자리가 없음.",
        "address": "포항시 북구 양덕동",
        "date": "2024.08.19",
        "categoryRate": {
            "cleanness": 5,
            "people": 4,
            "reach": 5,
            "rentFee": 3
        }
    }
}
```

### 5. [GET] /
- **설명:** 특정 지역의 모든 블라인드 리뷰 요약 목록을 조회합니다.
- **Path Variable:** `regionId` (Long)
- **Response Body (Success):**
```json
{
    "totalRate": 4.3,
    "clean": 4.8,
    "people": 4.1,
    "accessibility": 4.5,
    "rentFee": 3.8,
    "blind": [
        {
            "id": 1,
            "rate": 4.25,
            "title": "양덕동 카페거리 솔직 후기",
            "date": "2024.08.19",
            "userId": "reviewer1"
        },
        {
            "id": 2,
            "rate": 4.5,
            "title": "장성동 신상 맛집 리뷰",
            "date": "2024.08.18",
            "userId": "reviewer2"
        }
    ]
}
```

---

## 👤 My Page Controller
- **Base URL:** `/api/me`

### 1. [GET] /
- **설명:** 현재 로그인된 사용자의 마이페이지 정보를 조회합니다. (로그인 필요)
- **Response Body (Success):**
```json
{
    "email": "example@email.com",
    "id": "exampleUser",
    "blind": {
        "id": 10,
        "totalRate": 4.0,
        "title": "내가 쓴 블라인드 리뷰",
        "address": "포항시 남구",
        "date": "2024.08.15",
        "categoryRate": {
            "cleanness": 4,
            "people": 4,
            "reach": 4,
            "rentFee": 4
        },
        "positive": "장점입니다.",
        "negative": "단점입니다."
    },
    "ask": [
        {
            "id": "exampleUser",
            "title": "내가 쓴 Q&A 질문 1",
            "countComment": 2,
            "date": "2024.08.16"
        },
        {
            "id": "exampleUser",
            "title": "내가 쓴 Q&A 질문 2",
            "countComment": 0,
            "date": "2024.08.17"
        }
    ],
    "name": "김민준",
    "point": 500,
    "businessNumber": 1234567890,
    "aiReport": {
        "id": "exampleUser",
        "address": "포항시 북구 장량로 20",
        "reportCount": 3,
        "date": "2024.08.20"
    }
}
```