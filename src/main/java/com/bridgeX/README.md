# - Package Info.

```
- 최초 작성일 : 25.11.20
- 마지막 수정일 : 25.12.05
```

> 본 문서는 src/main/java 내의 패키지 구조에 대해 설명하는 문서이며, 추후 필요에 의해 수정될 수 있다.


## - 상위 Package 구조 관련

### 1. **user** Package (**com.bridgeX.user** Package)

> 로그인 및 회원가입을 포함한 사용자(User) 관련 작업을 처리하는 패키지

### 2. **review** Package (**com.bridgeX.review** Package)

> 사용자 커뮤니티(Review)와 관련된 작업을 처리하는 패키지

### 3. **image** Package (**com.bridgeX.image** Package)

> 이미지를 **CloudFlare에 업로드** 및 Url을 반환하는 패키지

### 4. **facilities** Package (**com.bridgeX.facilities** Package)

> 별도의 DB(데이터베이스)에 저장된 **운동 시설(Exercise Facilities)을 담당**하는 패키지

## - 하위 Package 구조

- 1. `*.controller` : 프론트엔드에서 들어온 **API 요청을 구분하여 알맞게 호출**을 불러오는 패키지

- 2. `*.service` : **비즈니스 로직을 처리**하는 계층(Service Layer)

- 3. `*.repository` : **데이터베이스(DB)접근 및 DB CRUD를 담당**하는 역할을 담당하는 패키지

- 4. `*.dto` : **D**ate **T**ransfer **O**bject. 데이터를 주고받기 위한 **요청(Request) 및 응답(Response)시 데이터 전송 객체의  구조를 정의**하는 패키지

- 5. `*.domain` : **Entity의 구조를 정의**하는 패키지.

- 6. `*.exception` : **예외 처리** 상황 발생 시, 이를 담당하여 처리하는 패키지

## - 전체 구조
```
src/main/java/com/bridgeX
 ├── Application, config, etc..
 ├── facilities
 │    └── ...
 ├── image
 │    └── ...
 ├── user
 │    ├── controller
 │    ├── service
 │    ├── repository
 │    ├── dto
 │    ├── exception
 │    └── domain
 └── review
      ├── controller
      ├── service
      ├── repository
      ├── dto
      └── domain
```
