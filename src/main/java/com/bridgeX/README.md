# - Package Info.

```
- 최초 작성일 : 25.11.20
- 마지막 수정일 : -
```

> 본 문서는 src/main/java 내의 패키지 구조에 대해 설명하는 문서이며, 추후에 추가될 수 있다.


## - 상위 Package 구조

### 1. **user** Package (**com.bridgeX.user** Package)

> 로그인 및 회원가입을 포함한 사용자(User)와 관련된 작업을 처리하는 패키지

### 2. **forum** Package (**com.bridgeX.forum** Package)

> 사용자 커뮤니티(forum)와 관련된 작업을 처리하는 패키지


## - 하위 Package 구조

- 1. `*.controller` : 프론트엔드에서 들어온 **API 요청을 구분하여 알맞게 호출**을 불러오는 패키지

- 2. `*.service` : controller에 의해 **호출되는 함수 부분**을 담고 있는 패키지

- 3. `*.repository` : **데이터베이스(DB)와 소통**하는 역할을 담당하는 패키지

- 4. `*.dto` : **D**ate **T**ransfer **O**bject. 데이터를 주고받기 위한 **객체 구조를 정의**하는 패키지

- 5. `*.domain` : **Entity의 구조를 정의**하는 패키지.

- 6. `*.exception` : **예외 처리** 상황 발생 시, 이를 담당하여 처리하는 패키지
