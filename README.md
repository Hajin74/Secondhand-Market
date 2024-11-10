# 중고마켓
- 원티드 6월 챌린지 (주제 : 테스트코드) 요구사항에 맞추었습니다.
- 테스트 코드 작성을 통해 코드 품질 향상을 목적으로 진행하였습니다.

<br>

## 요구사항
<details>
<summary>1단계</summary>

- 제품 등록과 구매는 회원만 가능합니다.
- 비회원은 등록된 제품의 목록 조회와 상세 조회만 가능합니다.
- 등록된 제품에는 세 가지(제품명, 가격, 예약상태) 정보가 포함되어야 합니다.
- 제품 상태는 세 가지(판매중, 예약중, 완료) 중 하나여야 합니다.
- 구매 과정
  1. 구매자가 제품 상세 페이지에서 구매하기 버튼을 누르면 거래가 시작됩니다.
  2. 판매자와 구매자는 제품 상세 정보에서 당사자 간 거래 내역을 확인할 수 있습니다.
  3. 모든 사용자는 다음 목록을 확인할 수 있어야 합니다.
    - 내가 구매한 용품 (내가 구매자인 경우)
    - 내가 예약 중인 용품 (내가 구매자/판매자인 경우)
  4. 판매자는 거래 진행 중인 구매자에 대해 판매 승인*을 하면 거래가 완료됩니다.

</details>

<details>
<summary>2단계</summary>

- 제품 정보에 수량이 추가되어, 네 가지 정보(제품명, 가격, 예약상태, 수량)가 포함되어야 합니다.
- 구매 과정 확장
  1. 다수의 구매자가 한 제품에 대해 구매하기를 요청할 수 있습니다.
  2. 단, 한 명이 구매할 수 있는 수량은 1개로 제한됩니다.
  3. 구매 확정 단계가 추가되어, 구매자는 판매자가 판매 승인한 제품에 대해 구매 확정을 할 수 있습니다.
- 제품 상태 변경
  - `판매중`: 추가 판매가 가능한 수량이 남아있는 경우
  - `예약중`: 추가 판매가 불가능하고, 구매 확정을 대기 중인 경우
  - `완료`: 모든 수량에 대해 모든 구매자가 구매 확정을 완료한 경우
- 가격 기록
  - 구매한 용품과 예약 중인 용품 목록에는 구매 당시의 가격이 표시됩니다.
    - 예: 구매자 A가 제품 B를 구매 요청할 당시 가격이 3000원이었고 이후 4000원으로 변경되었더라도, 목록에는 3000원으로 표시됩니다.

</details>

<br>

## RESTful API
▶️ [포스트맨 API 명세서](https://documenter.getpostman.com/view/34589851/2sA3XQfgRh#intro)
| **HTTP 메서드** | **URL**             | **설명**              |
|-----------------|----------------------|------------------------|
| POST            | `/api/user/join`     | 회원가입 |
| POST            | `/login`             | 로그인 |
| POST            | `/api/product`       | 제품 등록 |
| GET             | `/api/product`       | 제품 전체 조회 |
| GET             | `/api/product/{productId}`  | 제품 상세 조회  |
| GET             | `/api/product/completed`    | 구매한 제품 조회 |
| GET             | `/api/product/reserved`     | 예약 중인 제품 조회 |
| GET             | `/api/product/me`    | 내가 등록한 제품 조회 |
| PATCH           | `/api/product/{price}`    | 제품 가격 수정 |
| POST            | `/api/order`    | 제품 주문 |
| GET             | `/api/order/me`    | 내 거래내역 조회 |
| PATCH           | `/api/order/(orderId}/approve`    | 주문 승인 |
| PATCH           | `/api/order/(orderId}/confirm`    | 구매 확정 |
<br>

## 고민했던 것들

### 1. 제품 상태와 주문 상태는 다른 것인가?
- 사용자는 제품 상태만 알면된다. 
- 다른 사용자와 제품의 관계(주문 상태)를 알 필요가 없다.
- 따라서 제품 상태와 주문 상태를 분리시키는 것이 좋다.
- 제품 상태 → 판매중(`FOR_SALE`), 예약중(`IN_RESERVATION`), 판매완료(`SOLD_OUT`)
- 주문 상태 → 대기중(`PENDING`), 판매승인(`APPROVED`), 구매확정(`CONFIRMED`)

  | 경우 | 이벤트 | 제품 상태 | 주문 상태 |
  | --- | --- | --- | --- |
  | 추가 판매 가능 | 제품 주문 | `FOR_SALE` | `PENDING` |
  |  | 판매 승인 | `FOR_SALE` | `APPROVED` |
  |  | 구매 확정 | `FOR_SALE` | `CONFIRMED` |
  | 추가 판매 불가 | 제품 주문 | `IN_RESERVATION` | `PENDING` |
  |  | 판매 승인 | `IN_RESERVATION` | `APPROVED` |
  |  | 구매 확정 | `SOLD_OUT` | `CONFIRMED` |

<br>

### 2. 구매 과정(주문 → 승인 → 확정) 도중에 제품의 가격이 바뀐다면?
- 확정 가격(confirmedPrice)과 상품 가격(price)을 분리한다.
- 확정 가격은 주문 시점에 결정되고 변경되지 않는다.
- 구매는 확정된 가격으로 진행한다.
- 다른 사용자는 상품 가격만 확인한다.
- 구매 과정 중에 제품 가격이 바뀌더라도 `구매자는 확정 가격으로 구매`하고 `다른 사용자는 변경된 상품 가격을 보기` 때문에 문제가 없다.
- 확정 가격을 기록함으로써 구매한 용품과 예약중인 용품 목록의 정보에서 구매 당시의 가격도 확인할 수 있다.

<br>

### 3. 상세조회 로직이 회원여부와 당사자 여부에 따라서 달라진다.
- Controller에서 회원여부를 (Token 여부) 확인한다.
- 비회원일 경우, 그냥 상세 조회 결과를 반환한다.
- 회원일 경우, 거래내역을 포함한 상세 조회 결과를 반환한다.
- [코드 바로 가기](https://github.com/Hajin74/Secondhand-Market/blob/feature/hajin/src/main/java/org/example/market/service/ProductService.java)

<br>

### 4. 여러 사용자가 한 제품을 동시에 주문했을 때 정상적으로 작업이 이루어지는가?
#### 문제
- 여러 개의 스레드를 만들어 동시에 주문하는 테스트를 진행하였다.
- 주문이 여러 개가 되는 동시성 이슈가 발생하였다.
- [코드 바로 가기](https://github.com/Hajin74/Secondhand-Market/blob/feature%2Fhajin/src/test/java/org/example/market/service/OrderMultiThreadTest.java)
#### 해결
- DB 수준에서 해당 제품에 비관적 락을 걸어, 하나의 스레드씩만 접근할 수 있도록 처리하였다.
- JPA의 @Lock 어노테이션을 사용하였다.
- [코드 바로 가기](https://github.com/Hajin74/Secondhand-Market/blob/feature/hajin/src/main/java/org/example/market/repository/jpa/ProductJpaRepository.java)

<br>

### 5. 객체를 mocking한 테스트가 과연 신뢰성이 있는가?
- 의존 객체를 Mocking 결과, 행위만 검증은 가능했으나 상태 변화 검증이 부족했다.
- 협력 객체를 Mocking 대신 실제 객체로 교체했다.
- 주문 시 재고가 감소, 주문 취소 시 재고 복구와 같은 상태 변화 검증할 수 있었다.
- [코드 바로 가기](https://github.com/Hajin74/Secondhand-Market/blob/feature%2Fhajin/src/test/java/org/example/market/service/OrderServiceTest.java)


