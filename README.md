# OCR_PAY_API_TEST

토이 프로젝트에 앞서 외부 API(NAVER CLOVA OCR API, TOSS PAYMENTS pay API)를 테스트용으로 사용하는 레포지토리입니다. 

또한, 직렬화(Serialization), 역직렬화(Deserialization) 연습공간입니다.

카드 등록 -> (충전) -> 카드 결제 시나리오입니다.

##### 1. 실제 사용카드(신용카드, 체크카드) 촬영으로 OCR을 활용해 카드 정보 인식을 합니다. (카드명의, 카드번호, 유효연월)
<img width="257" alt="image" src="https://github.com/gkdbssla97/OCR_PAY_API_TEST/assets/55674664/e0a20cf7-c15a-4507-9726-51fdd99b43cf">

##### 2. 카드 정보를 회원에게 등록 후 카드 결제 API로 주문 ID, 정보와 카드 정보를 Request Body로 결제를 진행합니다.
<img width="1117" alt="image" src="https://github.com/gkdbssla97/OCR_PAY_API_TEST/assets/55674664/f67ff3fb-07f2-42f3-aee1-4d36a72c0e71">

##### 3. 카드 결제 내역 조회 API 사용 예정
