<br/>

[![Solved.ac 프로필](http://mazassumnida.wtf/api/v2/generate_badge?boj=tkdgnsdl37)](https://solved.ac/tkdgnsdl37)

안녕하세요!<br/>
이상훈 의 Github 개인 프로젝트 ReservationShop 입니다!

**sanghoon23@naver.com**

<br/><br/>
## 사용 기술

<br/>

![](https://img.shields.io/badge/HTML-239120?style=for-the-badge&logo=html5&logoColor=white)
![](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)
![](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
<br/><br/><br/>

## 개발 환경

<br/>

- IDE: IntelliJ IDEA
- Java: 17
- Framework: Spring Boot 3.3.3
- ORM: JPA
- DB: Oracle, MySQL
- Build Tool: Gradle
- Template Engine: Thymeleaf
- Version Control: Git


<br/><br/><br/>
## 목차

<br/>

- [프로젝트 소개](#프로젝트-소개)
- [구현 기능](#구현-기능)
- [엔티티 구조](#엔티티-구조)


<br/><br/>
## 프로젝트 소개

<img width="30%" src="https://github.com/user-attachments/assets/c6c6acb1-7136-4339-b8cf-c03d8e7f09d9"/>
<br/><br/>
네이버 예약 시스템을 바탕으로 개발한 개인 프로젝트입니다.

<br/><br/>
## 구현 기능

<img width="30%" src="https://github.com/user-attachments/assets/e2b4277c-3264-4621-8769-4e02ae43729a"/>
<img width="30%" src="https://github.com/user-attachments/assets/9b456263-b0b0-4592-95ac-b58376db2371"/>
<img width="30%" src="https://github.com/user-attachments/assets/0e3b0091-8acb-4454-b2ec-cad68a8e4caf"/>
<br/>
<img width="30%" src="https://github.com/user-attachments/assets/bb352535-17cb-497c-8a45-8d9c2965df23"/>
<img width="30%" src="https://github.com/user-attachments/assets/21085477-b268-45e9-a796-0b995016ff46"/>
<img width="30%" src="https://github.com/user-attachments/assets/d3e56442-918e-4e59-bb07-0b78d4467500"/>
<br/>
<img width="30%" src="https://github.com/user-attachments/assets/18715e61-4559-4efd-bdf4-488cc6051519"/>
<img width="30%" src="https://github.com/user-attachments/assets/be4bcecd-6181-48a7-ad53-923d25b21ef2"/>
<img width="30%" src="https://github.com/user-attachments/assets/b4904f37-5517-4fcc-bcbc-5381d154499e"/>




<br/><br/>
1. SpringSecurity 기반 OAuth2 로그인 인증 방식 구현 (Google, Naver, Kakao) 및 회원가입<br/>
2. 주소 API, 카카오 맵 API 활용  <br/>
3. 댓글 기능<br/>
4. 장소 등록 및 상품 등록<br/>
5. 예약하기<br/>
6. 마이페이지, 관리자 페이지<br/>

<br/><br/>
## 엔티티 구조

```mermaid
classDiagram
  class Member {
    +Long id
    +String name
    +String pw
    +String email
    +String phoneNumber
    +Address address
    +List~Reservation~ reservations
    +Member()
    +Member(MemberForm form)
    +void UpdateFromMemberForm(MemberForm form)
  }

  class Address {
    +String postcodes
    +String mainAddress
    +String detailAddress
  }

  class Reservation {
    +Long id
    +Member member
    +Place place
    +List~UserItem~ userItemList
    +String reservDate
    +String reservTime
    +String requiredContent
    +Reservation()
  }

  class Place {
    +Long id
    +String placeName
    +Address address
    +String description
    +String uploadImageFileName
    +String category
    +List~Comment~ comments
    +List~Item~ items
    +List~Reservation~ reservations
    +Place()
    +Place(PlaceForm form)
    +void updateFromPlaceForm(PlaceForm form)
  }

  class Comment {
    +Long id
    +Long userId
    +String userName
    +String content
    +String createdAt
    +LocalDateTime updateAt
    +Comment(...)
  }

  class Item {
    +Long id
    +String itemName
  }

  class UserItem {
    +Long id
    +String itemName
    +Long price
    +String uploadImageFileName
    +UserItem()
    +UserItem(...)
  }

  Member "1" --> "1..*" Reservation
  Reservation "1" --> "1..*" UserItem
  Member "1" --> "1" Address
  Place "1" --> "1..*" Comment
  Place "1" --> "1..*" Item
  Place "1" --> "1..*" Reservation
  Comment "1" --> "1" Place
```

<br/><br/>

