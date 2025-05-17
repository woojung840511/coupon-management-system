# 🎟️ 쿠폰 관리 시스템

쿠폰 발급, 사용, 조건 관리 등을 포함하는 백엔드 시스템입니다. 사용자(B2B/B2C)에게 쿠폰을 발급하고, 사용 이력을 관리하며, 보험 계약 정보와 연계하여 할인 혜택을 적용할 수 있습니다.

## 📋 프로젝트 개요

이 프로젝트는 마케팅 활동을 지원하기 위한 쿠폰 관리 시스템으로, 다양한 종류의 쿠폰을 유연하게 관리하고 사용할 수 있는 백엔드 API를 제공합니다.

### 주요 기능
- 쿠폰 정보 관리 (CRUD)
- 쿠폰 발급 및 사용
- 쿠폰 조건 관리
- 쿠폰 사용 이력 및 보유 현황 조회
- 보험 상품 및 계약 정보 연동

## 🛠️ 기술 스택

- **백엔드**: Java 17, Spring Boot 3.2.5
- **데이터베이스**: H2 Database (인메모리)
- **ORM**: Spring Data JPA, Querydsl
- **API 문서화**: Springdoc OpenAPI (Swagger UI)
- **빌드 도구**: Gradle
- **테스트**: JUnit 5, Spring Boot Test

## 🏗️ 시스템 아키텍처

### 주요 모듈
- 쿠폰 정보 관리 (CouponInfo)
- 쿠폰 발행 관리 (CouponBook)
- 사용자 쿠폰 관리 (PersonalCoupon)
- 쿠폰 로그 관리 (CouponLog)
- 사용자 관리 (Person)
- 보험 상품 및 계약 관리 (InsuranceProduct, InsuranceContract)

## 💡 핵심 비즈니스 로직

### 쿠폰 발급 프로세스
1. **쿠폰 정보 생성** (관리자)
  - 쿠폰 타입, 발행 방식, 사용 조건 등 설정

2. **쿠폰 코드 발행** (시스템)
  - UNI 타입: 고정 코드 생성 (동일 코드 여러 사용자에게 발급 가능)
  - POLY 타입: 임의 코드 생성 (1인 1코드 방식)

3. **사용자 쿠폰 발급** (관리자 → 사용자)
  - 특정 사용자에게 쿠폰 발급
  - 발급 제한 조건 검증 (중복 발급 여부, 발급 가능 시간 등)

### 쿠폰 사용 프로세스
1. **쿠폰 사용 요청** (사용자)
  - 특정 보험 계약에 쿠폰 적용 요청

2. **쿠폰 사용 검증** (시스템)
  - 쿠폰 유효성 검증 (사용 가능 시간, 사용 가능 횟수 등)
  - 쿠폰 사용 조건 검증 (최소 금액, 특정 상품 제한 등)

3. **쿠폰 사용 처리** (시스템)
  - 쿠폰 사용 정보 기록
  - 쿠폰 사용 로그 저장

## 🔍 주요 개념 및 설계 특징

### 쿠폰 사용 유형
- **ONCE**: 1회만 사용 가능한 쿠폰
- **MULTI**: 여러 번 사용 가능한 쿠폰
- **LIMIT**: 제한된 횟수만큼 사용 가능한 쿠폰

### 쿠폰 발행 유형
- **UNI**: 고정 코드 방식 (하나의 코드를 여러 사용자에게 발급)
- **POLY**: 임의 코드 방식 (각 발급마다 고유한 코드 생성)

### 쿠폰 조건 관리
다양한 조건 유형을 지원하여 유연한 쿠폰 정책 설정 가능:
- 최대 할인 금액
- 최소 사용 금액
- 회원 전용
- 특정 상품 전용
- 중복 사용 기간 허용
- 쿠폰 만료 정책

## 📊 주요 API 엔드포인트

### 쿠폰 정보 API
- `POST /api/coupons`: 쿠폰 정보 생성
- `GET /api/coupons/{id}`: 쿠폰 정보 조회
- `PATCH /api/coupons/{id}`: 쿠폰 정보 수정
- `DELETE /api/coupons/{id}`: 쿠폰 정보 삭제

### 쿠폰 조건 API
- `POST /api/coupons/{couponId}/conditions`: 쿠폰 조건 생성
- `GET /api/coupons/{couponId}/conditions/{conditionId}`: 쿠폰 조건 조회
- `PATCH /api/coupons/{couponId}/conditions/{conditionId}`: 쿠폰 조건 수정
- `DELETE /api/coupons/{couponId}/conditions/{conditionId}`: 쿠폰 조건 삭제

### 쿠폰북 API
- `POST /api/coupon-books`: 쿠폰북 발행

### 사용자 쿠폰 API
- `POST /api/personal-coupons`: 사용자 쿠폰 발급
- `POST /api/personal-coupons/{personCouponId}/use`: 쿠폰 사용
- `POST /api/personal-coupons/user/{personId}/search`: 사용자 쿠폰 검색

### 쿠폰 로그 API
- `GET /api/coupon-logs/user/{personId}/details`: 사용자별 쿠폰 사용 이력 조회

## 🧪 테스트 케이스

시스템의 중요한 비즈니스 로직에 대한 테스트 케이스를 구현했습니다:

- 단위 테스트: 각 서비스 로직 검증
- 통합 테스트: 쿠폰 발급 및 사용 플로우 검증

### 주요 테스트 시나리오
- 쿠폰 정보 생성 및 조회 검증
- UNI 타입 쿠폰북 생성 검증
- POLY 타입 쿠폰북 생성 검증
- 쿠폰 발급 및 사용 전체 플로우 검증
- 쿠폰 사용 횟수 제한 검증

## 💻 실행 방법

### 요구사항
- JDK 17 이상
- Gradle 8.x

### 빌드 및 실행
```bash
# 프로젝트 클론
git clone https://github.com/yourgithub/coupon-management-system.git
cd coupon-management-system

# 빌드
./gradlew build

# 실행
./gradlew bootRun
```

### API 문서 확인
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- API Docs: http://localhost:8080/api/api-docs

## 🌟 프로젝트 특징 및 개발 포인트

1. **도메인 주도 설계(DDD) 접근**
  - 비즈니스 로직을 도메인 계층에 집중
  - 풍부한 도메인 모델과 비즈니스 규칙

2. **계층형 아키텍처**
  - 컨트롤러, 서비스, 레포지토리로 명확히 구분된 계층
  - 각 계층의 역할과 책임 분리

3. **효과적인 예외 처리**
  - 도메인별 커스텀 예외 클래스
  - 일관된 에러 응답 포맷

4. **데이터 접근 최적화**
  - Querydsl을 활용한 동적 쿼리 구현
  - 복잡한 조회 조건에 대응

5. **Swagger를 활용한 API 문서화**
  - 자동화된 API 문서 생성
  - API 테스트 용이성 제공

## 📈 향후 개선 계획

- 사용자 인증 및 권한 관리(Spring Security)
- 비동기 이벤트 처리(Spring Events)
- 캐싱 적용(Redis)
- 클라우드 환경 배포(Docker, Kubernetes)
- 모니터링 시스템 연동