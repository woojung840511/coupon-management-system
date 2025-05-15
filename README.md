# backend_apply_woojung840511
Backend Apply Test

# 쿠폰 관리 시스템

## 프로젝트 개요
이 프로젝트는 쿠폰의 발급, 사용, 조건 관리 등을 포함하는 백엔드 시스템입니다.

## 개발 환경
- Java 17
- Spring Boot 3.4.5
- H2 Database
- Gradle 8.13

## 기능 요약
- 쿠폰 정보 관리 (CRUD)
- 쿠폰 발급 및 사용
- 쿠폰 조건 관리
- 쿠폰 사용 이력 및 보유 현황 조회
- 사용자 정보 관리
- 보험 상품 및 계약 정보 관리

## API 문서
API 문서는 Swagger를 통해 확인할 수 있습니다.
- http://localhost:8080/api/swagger-ui.html

## 과제 요구사항

### 배경
쿠폰 관리 시스템은 고객에게 다양한 혜택을 제공하고, 쿠폰 사용 및 발급 기록을 관리하기 위한 필수적인 기능입니다. 이번 과제는 쿠폰의 발급, 사용, 조건 관리 등을 포함하는 백엔드 시스템을 개발하는 것입니다.

### 요구 사항

#### 쿠폰 정보 관리
- coupon_info 테이블을 기반으로 쿠폰의 기본 정보(CRUD) API를 구현합니다.
- 쿠폰의 사용 유형, 발급 유형, 조건 등을 관리할 수 있어야 합니다.

#### 쿠폰 발급 및 사용
- 쿠폰을 발급할 수 있는 API를 작성합니다. 이때, 난수 쿠폰 코드는 1인에게만 등록 가능하도록 합니다.
- 발급 기능 구현시 멤버/상품/기간 에 대한 기능을 우선으로 합니다.
- 쿠폰 사용 시, coupon_log에 발급자 및 사용자 정보를 기록하는 API를 구현합니다.

#### 쿠폰 조건 관리
- 쿠폰에 걸려 있는 조건을 관리하기 위한 API를 작성합니다. 조건 유형에 따라 적절한 조건을 추가하거나 수정할 수 있어야 합니다.

#### 쿠폰 사용 이력 및 보유 현황
- 사용자가 보유하고 있는 쿠폰을 확인할 수 있는 API를 구현합니다.
- 사용 이력을 기록하고 조회할 수 있는 API를 작성합니다. 이력에는 발급자 및 사용자 정보가 포함되어야 합니다.

#### 사용자 정보 관리
- person 은 sample 데이터를 생성하여 진행 합니다.

#### 보험 상품 및 계약 정보 관리
- 보험 상품 및 계약 정보를 관리하기 위한 API를 구현합니다. 사용자가 쿠폰을 사용할 때 관련된 보험 계약 정보를 기록할 수 있어야 합니다.
- 보험 상품 정보는 sample 데이터를 생성하여 진행 합니다.

#### Enum 관리
- 정의된 Enum 값들을 활용하여 각 API에서 적절한 검증을 수행하도록 합니다.

### 테이블 명세

#### coupon_info
- coupon_info_seq: 쿠폰 순번
- coupon_type: 쿠폰의 사용 유형
- coupon_name: 쿠폰의 명칭
- target_type: B2B, B2C 서비스 대상
- coupon_publish_type: 쿠폰의 발행 유형
- purpose_type: 쿠폰의 용도
- purpose_value: 용도의 값
- coupon_badge_type: 쿠폰에 붙일 배지 유형
- coupon_image_url: 쿠폰 이미지 URL
- press_count: 쿠폰 발행 숫자
- use_count: 쿠폰 사용 숫자
- limit_count: 쿠폰 사용 제한 숫자
- is_able: 사용 여부
- is_duplicate: 중복 사용 가능 여부
- coupon_start_at: 쿠폰 적용 시작 일시
- coupon_end_at: 쿠폰 적용 종료 일시

#### coupon_condition
- coupon_condition_seq: 조건 순번
- coupon_info_seq: 쿠폰 정보 순번
- condition_type: 조건 유형
- main_value: 주요 값
- sub_value: 참조 값
- condition_desc: 조건 설명

#### coupon_book
- coupon_book_seq: 쿠폰 코드 순번
- coupon_info_seq: 쿠폰 정보 순번
- coupon_code: 쿠폰 코드 값
- is_used: 사용 여부
- expire_at: 사용 종료 일시

#### personal_coupon
- person_coupon_id: 사용자의 쿠폰 정보 Guid
- person_id: 사용자 Guid
- coupon_code: 보유하거나 사용한 쿠폰 코드
- use_at: 사용 일시
- insurance_subscription_details_id: 사용한 보험의 계약 정보
- use_data: 사용 데이터

#### coupon_log
- coupon_log_seq: 쿠폰 로그 순번
- coupon_code: 쿠폰 코드 값
- person_id: 사용자 값
- log_type: 로그 유형
- log_desc: 로그 설명

#### person
- person_id: 사용자 고유 식별자 (Guid)
- name: 사용자 이름
- email: 사용자 이메일
- phone: 사용자 전화번호
- created_at: 사용자 등록 일시
- updated_at: 사용자 정보 수정 일시

#### insurance_product
- product_id: 보험 상품 고유 식별자 (Guid)
- product_name: 보험 상품명
- premium: 보험료
- created_at: 상품 등록 일시
- updated_at: 상품 정보 수정 일시

#### insurance_contract
- contract_id: 계약 고유 식별자 (Guid)
- person_id: 계약자 (사용자) 식별자
- product_id: 보험 상품 식별자
- premium: 보험료
- contract_start_date: 계약 시작 일시
- contract_end_date: 계약 종료 일시
- status: 계약 상태 (예: 활성, 만료, 해지)
- created_at: 계약 등록 일시
- updated_at: 계약 정보 수정 일시

### Enum 명세

#### CouponType
- ONCE: 1회
- MULTI: 다회
- LIMIT: 제한

#### CouponPublishType
- UNI: 고정 코드
- POLY: 임의 코드

#### TargetType
- B2B
- B2C
- ALL

#### PurposeType
- RATE: 할인율
- AMOUNT: 차감 비용

#### CouponBadgeType
- NEW
- BEST

#### ConditionType
- MAX_DISCOUNT: 최대 할인 금액
- MIN_AMOUNT: 최소 사용 금액
- ONLY_MEMBER: 회원 전용
- ONLY_PRODUCT: 상품 전용
- TERM: 중복 사용 기간 허용
    - 기간 단위: YEAR, MONTH, DAY
- EXPIRE: 쿠폰 만료 정책

#### LogType
- PUBLISH: 발행
- USE: 사용
- TRANS: 전송

## 기술 스택
- 프레임워크: Spring Boot (Java)
- 데이터베이스: H2

## 제출물
- API 문서: Swagger
- 코드: GitHub 업로드

## 평가 기준
- 코드의 가독성 및 구조
- API의 기능 구현 여부
- API 문서의 완전성과 명확성
- 테스트 케이스의 포함 여부
