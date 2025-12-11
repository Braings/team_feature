### 국민 체육 데이터를 활용한 연령대별 운동 추천 게시판 프로젝트
# BRIDGE - X
<img width="2516" height="1320" alt="image" src="https://github.com/user-attachments/assets/1dca84ca-5884-4c8f-89c4-24bdbfd49040" />
<img src="https://raw.githubusercontent.com/Braings/team_feature/main/BridgeX_Architecture.svg" width="2516" />


### 프로젝트 소개
> 국민 체육 진흥원에서 제공하는 국민 연령대별 운동 빅데이터를 활용한 연령대별 운동을 추천하는 프로젝트입니다.

### 프로젝트 목표
> 개인 맞춤 운동 관리를 위한 운동 마이페이지 운영

> 홈 화면에서 제공하는 동 연령대 대비 운동 수행 능력 표시

> 국가 기관 빅데이터에 의거한 개인 맞춤 운동 추천

> 지도를 통한 전국의 주변 운동 시설 시각화 및 리뷰 가능

> 직접 관련 시설 게시판 사용 가능

### 고찰(느낌 및 향후 발전 방향)

> 이번 Bridge-X 프로젝트는 단순한 게시판 기능이 포함된 웹 서비스를 만들었다기보다,
프론트엔드(Vue 3 기반)부터 백엔드(Spring Boot 3, JPA, 복수 데이터소스 구조(다중 데이터베이스) 등)
꽤나 본격적인 웹 구조를 직접 설계하고 부딪혀본 프로젝트였다.

초반엔 DB 스키마 관리 및 백엔드와 프론트엔드 간 API통신 단계 부터 쉽지 않았고,
대용량 데이터베이스의 데이터 정제 과정도 부딪히는것이 많았다
Spring Security 인증 과정, JWT 쿠키 저장, 프론트엔드 라우팅, 컴포넌트 간 props 전달 오류 등등
수 많은 오류를 해결하는 과정에서 실제 프로젝트는 단순히 기능 개발이 우세가 아닌,
오류들을 고쳐가며 유지보수 및 기능 확장이 큰 쟁점이라는것도 느꼈다.

무엇보다 웹 전체 구조가 어떻게 맞물려 돌아가는지 이번에 확실히 알게 된 것이 가장 인상깊었다.
더 나아가, 소통의 부족으로 오히려 서로 중복되는 기능 개발을 하는 등.
독이 되었던 경우들마저 있었기에 팀플 및 협업에서 개발 능력만큼이나 중요한 것은 소통이라는것도 알게 되었다.

앞으로의 발전 방향은, 이번 프로젝트에 부족했던 부분을 보완하여 학교에서의 프로젝트나 졸업 작품에 국한되지 않고
더 나아가 미래 개발자로써의 활동에서도 계속 배우는 자세를 가져야겠다는 생각도 들었다.


### 기술 체인

**Front-END**
```
node js
Vue js
Figma
```

**Back-END**
```
java jdk 21
java spring-boot
Gleek.io
mariaDB 10.5.29
flask 3.1.0
```

### AI Feature Dependencies
```bash
# pip package
pip install google-genai pandas python-dotenv
```
> GoogleAi-API key required in .env

### License
```
MIT License

Copyright (c) 2025 Braings

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
