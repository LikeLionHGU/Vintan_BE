# **📄 Vintan API 명세서**

본 문서는 **Vintan 백엔드 API**의 사양을 정의합니다.

---

## **📑 목차**

1. [👤 User API](#-1-user-api)
2. [🤖 AI Report API](#-2-ai-report-api)
3. [💬 Community Ask API](#-3-community-ask-api)
4. [🕶️ Community Blind API](#-4-community-blind-api)
5. [👤 My Page API](#-5-my-page-api)

---

## **1. 👤 User API**

* **Base URL:** `/auth/login`

---

### **1.1 회원가입**

**\[POST]** `/auth/login/register`
**설명:** 신규 유저를 등록합니다.

**Request**

```json
{
    "id": "exampleUser",
    "name": "김민준",
    "password": "password123!",
    "email": "example@email.com",
    "businessNumber": 1234567890
}
```

**Response**

```json
{
    "isRegistered": 1
}
```

---

### **1.2 아이디 중복 확인**

**\[GET]** `/auth/login/duplicate/{userId}`
**설명:** 사용자 ID의 중복 여부 확인

**Response**

```json
{
    "isDuplicated": 1
}
```

---

### **1.3 로그인**

**\[POST]** `/auth/login/login`
**설명:** 사용자 로그인 및 세션 생성

**Request**

```json
{
    "id": "exampleUser",
    "password": "password123!"
}
```

**Response**

```json
{
    "isLogin": 1
}
```

---

### **1.4 로그아웃**

**\[POST]** `/auth/login/logout`
**설명:** 세션 무효화

**Response**

```json
{
    "isLogout": 1
}
```

---

### **1.5 세션 확인**

**\[GET]** `/auth/login/session`
**설명:** 로그인 여부 및 사업자 여부 확인

**Response**

```json
{
    "isLogin": 1,
    "isBusiness": 1,
    "name" : "권혁민"
}
```

---

## **2. 🤖 AI Report API**

* **Base URL:** `/ai/reports`

---

### **2.1 AI 보고서 생성**

**\[POST]** `/ai/reports/generate/{regionId}`
**설명:** 특정 지역에 대한 AI 상권 분석 보고서 생성 (로그인 필요)

**Request**

```json
{
    "address": "포항시 북구 장량로 20",
    "categoryCode": "카페",
    "pyeong": 15.5,
    "userDetail": "1층 코너 자리 원해요",
    "addressName": "포항시 양덕동"
}
```

**Response**

```json
{
    "reportId": 1
}
```

---

### **2.2 보고서 상세 조회**

**\[GET]** `/ai/reports/{reportId}`
**설명:** 생성된 AI 상권 분석 보고서 조회

**Response**

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
        "summary": "주요 도로에 인접해 차량 접근성이 우수하며, 버스 정류장이 도보 3분 거리에 위치합니다.",
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
    "postCount": 3,
    "finalReportReviewSummary": "종합적으로 볼 때, 해당 입지는 카페 창업에 매우 유리한 조건을 갖추고 있습니다.",
    "addressName": "포항시 양덕동"
}
```

---

## **3. 💬 Community Ask API**

* **Base URL:** `/regions/{regionId}/community/ask`

---

### **3.1 질문 목록 조회**

**\[GET]** `/regions/{regionId}/community/ask`

**Response**

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

---

### **3.2 질문 상세 조회**

**\[GET]** `/regions/{regionId}/community/ask/{postId}`

**Response**

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

---

### **3.3 질문 작성**

**\[POST]** `/regions/{regionId}/community/ask/write`

**Request**

```json
{
    "title": "새로운 질문입니다.",
    "content": "이 지역의 임대료는 어느정도 수준인가요?"
}
```

**Response**

```json
{
    "isSuccess": 1
}
```

---

### **3.4 댓글 작성**

**\[POST]** `/regions/{regionId}/community/ask/{postId}/comments`

**Request**

```json
{
    "comment": "제가 알기로는 평당 10만원 선입니다."
}
```

**Response**

```json
{
    "isSuccess": 1
}
```

---

## **4. 🕶️ Community Blind API**

* **Base URL:** `/regions/{regionId}/community/blind/reviews`

---

### **4.1 블라인드 리뷰 작성**

**\[POST]** `/regions/{regionId}/community/blind/reviews`

**Request**

```json
{
    "title": "양덕동 카페거리 솔직 후기",
    "positive": "분위기 좋은 카페가 많고 주차가 편리함.",
    "negative": "주말에는 사람이 너무 많아서 자리가 없음.",
    "address": "포항시 북구 양덕동 201",
    "addressName" : "포항시 북구 양덕동",
    "categoryRate": {
        "cleanness": 5,
        "people": 4,
        "reach": 5,
        "rentFee": 3
    }
}
```

**Response**

```json
{
    "isSuccess": 1
}
```

---

### **4.2 블라인드 리뷰 수정**

**\[PATCH]** `/regions/{regionId}/community/blind/reviews/{reviewId}`

**Request**

```json
{
    "title": "양덕동 카페거리 솔직 후기 (수정)",
    "positive": "분위기 좋은 카페가 많고 주차가 편리함. 평일 방문 추천.",
    "negative": "주말에는 사람이 너무 많아서 자리가 없음.",
    "addressName" : "포항시 북구 양덕동",
    "categoryRate": {
        "cleanness": 5,
        "people": 3,
        "reach": 5,
        "rentFee": 3
    }
}
```

**Response**

```json
{
    "isSuccess": 1
}
```

---

### **4.3 블라인드 리뷰 삭제**

**\[DELETE]** `/regions/{regionId}/community/blind/reviews/{reviewId}`

**Response**

```json
{
    "isSuccess": 1
}
```

---

### **4.4 블라인드 리뷰 상세 조회**

**\[GET]** `/regions/{regionId}/community/blind/reviews/{reviewId}`

**Response**

```json
{
    "totalRate": 4.25,
    "blind": {
        "title": "양덕동 카페거리 솔직 후기",
        "positive": "분위기 좋은 카페가 많고 주차가 편리함.",
        "negative": "주말에는 사람이 너무 많아서 자리가 없음.",
        "address": "포항시 북구 양덕동 201",
        "addressName" : "포항시 북구 양덕동",
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

---

### **4.5 블라인드 리뷰 목록 조회**

**\[GET]** `/regions/{regionId}/community/blind/reviews`

**Response**

```json
{
    "totalRate": 4.3,
    "clean": 4.8,
    "people": 4.1,
    "accessibility": 4.5,
    "rentFee": 3.8,
    "addressName" : "포항시 북구 양덕동",
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

## **5. 👤 My Page API**

* **Base URL:** `/api/me`

---

### **5.1 마이페이지 조회**

**\[GET]** `/api/me`

**Response**

```json
{
  "email": "example@email.com",
  "id": "exampleUser",
  "blind": {
    "id": 10,
    "totalRate": 4.0,
    "title": "내가 쓴 블라인드 리뷰",
    "address": "포항시 남구",
    "date": "2025.08.15",
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
      "date": "2025.08.16"
    },
    {
      "id": "exampleUser",
      "title": "내가 쓴 Q&A 질문 2",
      "countComment": 0,
      "date": "2025.08.17"
    }
  ],
  "name": "김민준",
  "point": 500,
  "businessNumber": 1234567890,
  "aiReport": [
    {
      "id": "1",
      "address": "포항시 북구 장량로 20",
      "reportCount": 3,
      "category" : "카페",
      "date": "2025.08.20"
    },
    {
      "id": "3",
      "address": "포항시 남구 지곡로 50",
      "reportCount": 3,
      "category" : "카페",
      "date": "2025.07.11"
    }
  ]
}
```
