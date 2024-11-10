# 중고마켓

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

### 1. 상세조회 로직이 회원여부와 당사자 여부에 따라서 달라진다.
- Controller에서 비회원인지(token is null) 회원인지(token이 존재) 확인한다.
- 비회원일 경우, 그냥 상세 조회 결과를 반환한다.
- 회원일 경우, 거래내역을 포함한 상세 조회 결과를 반환한다.

   controller
   ```
   public ApiResponse getDetailProduct(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long productId) {
       log.info("제품 상세 조회 api");
   
       try {
           if (customUserDetails != null) {
               String username = customUserDetails.getUsername();
               Long userId = userRepository.findByUsername(username).getId();
               return new DataResponse<>(productService.findDetailProductWithTransaction(userId, productId));
           }
           return new DataResponse<>(productService.findDetailProduct(productId));
       } catch (CustomException exception) {
           return new ErrorResponse(exception.getErrorCode(), exception.getMessage());
       }
   }
   ```

   service
   ```
   /* 제품 상세 조회 without 거래내역 */
   @Transactional(readOnly = true)
   public ProductResponse findDetailProduct(Long productId) {
       Product findProduct = productRepository.findById(productId).orElseThrow(
               () ->  new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
   
       return ProductResponse.from(findProduct);
   }
   
   /* 제품 상세 조회 with 거래내역 */
   @Transactional(readOnly = true)
   public ProductDetailResponse findDetailProductWithTransaction(Long userId, Long productId) {
       Product findProduct = productRepository.findById(productId).orElseThrow(
               () ->   new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
   
       List<TransactionResponse> transactionList = new ArrayList<>();
   
       List<Order> orders = orderRepository.findAllByProductId(productId);
       for(Order order : orders) {
           if (order.getSeller().getId().equals(userId) || order.getBuyer().getId().equals(userId)) {
               transactionList.add(TransactionResponse.from(order));
           }
       }
   
       return ProductDetailResponse.builder()
               .product(ProductResponse.from(findProduct))
               .transactions(transactionList)
               .build();
   }
   ```


2. 제품 상태와 주문 상태는 다른 것인가?
   - 사용자는 제품 상태만 알면된다. 다른 사용자와 제품의 관계(주문 상태)를 알 필요가 없다.
   - 따라서 제품 상태와 주문 상태를 분리시키는 것이 좋다.
     - 제품 상태 : 판매중(FOR_SALE), 예약중(IN_RESERVATION), 판매완료(SOLD_OUT)
     - 주문 상태 : 대기중(PENDING), 판매승인(APPROVED), 구매확정(CONFIRMED)
        | 경우 | 이벤트 | 제품 상태 | 주문 상태 |
        | --- | --- | --- | --- |
        | 추가 판매 가능 | 제품 주문 | `FOR_SALE` | `PENDING` |
        |  | 판매 승인 | `FOR_SALE` | `APPROVED` |
        |  | 구매 확정 | `FOR_SALE` | `CONFIRMED` |
        | 추가 판매 불가 | 제품 주문 | `IN_RESERVATION` | `PENDING` |
        |  | 판매 승인 | `IN_RESERVATION` | `APPROVED` |
        |  | 구매 확정 | `SOLD_OUT` | `CONFIRMED` |



3. 구매 과정(주문 -> 승인 -> 확정) 도중에 제품의 가격이 바뀐다면?
   - 확정 가격(confirmedPrice)과 상품 가격(price)을 나눈다.
   - 확정 가격은 주문을 한 순간 정해진다.
   - 주문 시점에 주문 엔티티의 confirmedPrice를 기록하여, 구매 과정에서 제품의 가격이 아닌 확정 가격으로 진행할 수 있게 한다.
   - 다른 사용자는 확정 가격이 아닌 상품 가격을 보기 때문에 바뀐 가격을 보게 된다.
   - 확정 가격을 기록함으로써 `구매한 용품`과 `예약중인 용품` 목록의 정보에서 구매하기 당시의 가격정보도 보여줄 수 있다.



4. 다수의 사용자가 한 제품을 동시에 주문했을 때 정상적으로 작업이 이루어지는가?
   - 문제
     - 여러 개의 스레드를 만들어 동시에 주문하는 테스트를 진행하였다.
     - 동시성 이슈로 데드락이 발생하였다.
   - 해결
      - 처음부터 교착상태를 예방하는 방법을 선택하였다.
      - 데이터베이스 수준에서 해당 제품에 비관적 락을 걸어 구현하였다.
      ```
      @Lock(value= LockModeType.PESSIMISTIC_WRITE)
      @Query("select p from Product p where p.id = :id")
      Product findByWithPessimisticLock(final Long id);
      
      public OrderResponse orderProduct(Long userId, OrderCreateRequest request) {
      	...
      	Product product = productRepository.findByWithPessimisticLock(request.getProductId());
      	...
      }
      ```
