# Chat Backend

Spring Boot 기반 실시간 채팅 백엔드 서버

##  프로젝트 개요

WebSocket(STOMP)을 활용한 실시간 채팅 시스템의 백엔드 API 서버입니다.

##  기술 스택

- **Java 17**
- **Spring Boot 3.4.10**
- **Spring Data JPA**
- **Spring Security**
- **Spring WebSocket (STOMP)**
- **MySQL** (Production)
- **H2** (Test)
- **JWT** (인증/인가)
- **Lombok**
- **Gradle**

##  주요 기능

- 실시간 채팅 메시지 송수신 (WebSocket/STOMP)
- JWT 기반 인증 및 권한 관리
- 채팅방 생성 및 관리
- 채팅방 참여자 관리
- 메시지 읽음 처리
- 내가 참여한 채팅방 목록 조회

##  실행 방법

### 사전 요구사항

- JDK 17 이상
- MySQL (또는 H2 사용)

### 실행

```bash
# 프로젝트 클론
git clone https://github.com/dengo25/chatWithStomp.git
cd chat_backend

# 빌드 및 실행
./gradlew bootRun
```

### 테스트

```bash
./gradlew test
```

##  프로젝트 구조

```
src/main/java/org/dengo/chat_backend/
├── ChatBackendApplication.java
├── chat/
│   ├── config/          # WebSocket 설정
│   ├── controller/      # REST API & STOMP 컨트롤러
│   ├── domain/          # 엔티티
│   ├── dto/             # 데이터 전송 객체
│   ├── repository/      # JPA 리포지토리
│   └── service/         # 비즈니스 로직
└── util/                # JWT 유틸리티
```

##  API 엔드포인트

### REST API

#### 1. 채팅방 생성/조회
**POST** `/chat/room/private/create`
- 1:1 채팅방 생성 또는 기존 채팅방 조회
- **Query Parameter**: `otherMemberId` (Long) - 상대방 회원 ID
- **Response**: 채팅방 ID (Long)
- **인증**: JWT 필요

**GET** `/chat/my/rooms`
- 내가 참여한 채팅방 목록 조회
- **Response**:
  ```json
  [
    {
      "roomId": 1,
      "roomName": "채팅방 이름",
      "isGroupChat": "N",
      "unReadCount": 3
    }
  ]
  ```
- **인증**: JWT 필요

#### 2. 채팅 메시지 조회
**GET** `/chat/history/{roomId}`
- 특정 채팅방의 이전 메시지 조회
- **Path Parameter**: `roomId` (Long) - 채팅방 ID
- **Response**:
  ```json
  [
    {
      "message": "안녕하세요",
      "senderEmail": "user@example.com"
    }
  ]
  ```
- **인증**: JWT 필요

#### 3. 메시지 읽음 처리
**POST** `/chat/room/{roomId}/read`
- 특정 채팅방의 메시지를 읽음 처리
- **Path Parameter**: `roomId` (Long) - 채팅방 ID
- **Response**: 200 OK
- **인증**: JWT 필요

### WebSocket (STOMP)

#### 연결 설정
- **WebSocket Endpoint**: `/ws`
- **프로토콜**: STOMP over WebSocket
- **인증**: Connection 시 JWT 토큰 필요

#### 메시지 발행 (Publish)
**SEND** `/publish/{roomId}`
- 특정 채팅방에 메시지 전송
- **Path Parameter**: `roomId` (Long) - 채팅방 ID
- **Request Body**:
  ```json
  {
    "message": "안녕하세요",
    "senderEmail": "user@example.com"
  }
  ```

#### 메시지 구독 (Subscribe)
**SUBSCRIBE** `/topic/{roomId}`
- 특정 채팅방의 실시간 메시지 수신
- **Path Parameter**: `roomId` (Long) - 채팅방 ID
- **Receive Message**:
  ```json
  {
    "message": "안녕하세요",
    "senderEmail": "user@example.com"
  }
  ```

##  설정

### 1. 데이터베이스 설정 (`application.yml`)

MySQL 연결 정보 (host, port, username, password) 및 JPA 설정을 구성하세요.


### 2. WebSocket 설정 (`WebSocketConfig.java`)

WebSocket 설정은 Java Config로 구성되어 있습니다:
- **STOMP Endpoint**: `/connect` (SockJS 지원)
- **Allowed Origins**: `http://localhost:5173`
- **Message Broker**: `/topic/*` (구독용), `/publish/*` (발행용)
- **인증**: `StompHandler`를 통한 JWT 토큰 검증


##  라이선스
이 프로젝트는 개인 프로젝트입니다.
