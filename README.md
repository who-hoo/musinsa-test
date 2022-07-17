# 무신사 채용과제 - 온라인 쇼핑몰 상품 카테고리 구현

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fwho-hoo%2Fmusinsa-test&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

[1. 설치 및 빌드 방법 가이드(리눅스 기준)](#1-설치-및-빌드-방법-가이드리눅스-기준)  
[2. API 명세](#2-api-명세)  
[3. API 사용 가이드 및 요청 예시(JSON)](#3-api-사용-가이드-및-요청-예시json)

## 1. 설치 및 빌드 방법 가이드(리눅스 기준)

### 직접 설치 & 빌드

- install

  ```bash
  // jdk 설치(되어있다면 생략 가능)
  sudo apt install openjdk-11-jdk

  git clone https://github.com/who-hoo/musinsa-test.git
  ```

- build
  ```bash
  cd [클론받은 리포지토리 경로]
  chmod +x gradlew
  ./gradlew bootJar
  ```
- execute
  ```bash
  cd [클론받은 리포지토리 경로]/build/libs
  java -jar category-0.0.1-SNAPSHOT.jar
  ```

### docker 사용

- install
  ```bash
  // 도커 설치(되어있다면 생략 가능)
  curl -fsSL https://get.docker.com/ | sudo sh
  docker pull zjunghoo/musinsa-test
  ```
- execute
  ```bash
  docker images // image id 확인
  docker run -it -d -p 8080:8080 [doker images로 확인한 image id]
  ```

## 2. API 명세

| 기능               | API                                             | 비고                                                       |
| ------------------ | ----------------------------------------------- | ---------------------------------------------------------- |
| 카테고리 등록      | POST /categories                                |                                                            |
| 카테고리 수정      | PATCH /categories/{id}                          |                                                            |
| 카테고리 삭제      | DELETE /categories/{id}?withSubCategories=false | withSubCategories : 하위 카테고리 삭제 여부(default:false) |
| 전체 카테고리 조회 | GET /categories                                 |                                                            |
| 특정 카테고리 조회 | GET /categories/{id}                            | 해당 카테고리의 하위의 모든 카테고리 조회 가능             |

## 3. API 요청 및 응답 예시(JSON)

### 카테고리 등록

- POST http://localhost:8080/api/categories
- request body
  ```json
  {
    "kor_name": "반려동물 장난감",
    "parent_category_id": 191
  }
  ```
- response
  ```json
  {
    "category_id": 192,
    "category_name": "반려동물 장난감",
    "category_english_name": null,
    "parent_category_id": 191,
    "sub_categories": []
  }
  ```

### 카테고리 수정

- PATCH http://localhost:8080/api/categories/188
- request body
  ```json
  {
    "kor_name": "티셔츠",
    "eng_name": null,
    "parent_category_id": 2,
    "sub_category_id_list": [23, 25]
  }
  ```
- response
  ```json
  {
    "category_id": 188,
    "category_name": "티셔츠",
    "category_english_name": null,
    "parent_category_id": 2,
    "sub_categories": [
      {
        "category_id": 23,
        "category_name": "반소매 티셔츠",
        "category_english_name": null,
        "parent_category_id": 188,
        "sub_categories": []
      },
      {
        "category_id": 25,
        "category_name": "긴소매 티셔츠",
        "category_english_name": null,
        "parent_category_id": 188,
        "sub_categories": []
      }
    ]
  }
  ```

### 카테고리 삭제

- DELETE http://localhost:8080/api/categories/1?withSubCategories=false : 상위 카테고리만 삭제(하위 카테고리는 루트 카테고리로 변경)
- DELETE http://localhost:8080/api/categories/2?withSubCategories=true : 하위 카테고리 함께 삭제

### 카테고리 조회

- GET http://localhost:8080/api/categories/1
- response
  ```json
  {
    "categories": [
      {
        "category_id": 1,
        "category_name": "상의",
        "category_english_name": "Top",
        "parent_category_id": null,
        "sub_categories": [
          {
            "category_id": 24,
            "category_name": "피케/카라 티셔츠",
            "category_english_name": null,
            "parent_category_id": 1,
            "sub_categories": []
          },
          {
            "category_id": 26,
            "category_name": "맨투맨/스웨트셔츠",
            "category_english_name": null,
            "parent_category_id": 1,
            "sub_categories": []
          },
          {
            "category_id": 27,
            "category_name": "민소매 티셔츠",
            "category_english_name": null,
            "parent_category_id": 1,
            "sub_categories": []
          },
          {
            "category_id": 28,
            "category_name": "후드 티셔츠",
            "category_english_name": null,
            "parent_category_id": 1,
            "sub_categories": []
          },
          {
            "category_id": 29,
            "category_name": "셔츠/블라우스",
            "category_english_name": null,
            "parent_category_id": 1,
            "sub_categories": []
          },
          {
            "category_id": 30,
            "category_name": "니트/스웨터",
            "category_english_name": null,
            "parent_category_id": 1,
            "sub_categories": []
          },
          {
            "category_id": 31,
            "category_name": "기타 상의",
            "category_english_name": null,
            "parent_category_id": 1,
            "sub_categories": []
          }
        ]
      },
      {
        "category_id": 2,
        "category_name": "아우터",
        "category_english_name": "Outer",
        "parent_category_id": null,
        "sub_categories": [
          {
            "category_id": 32,
            "category_name": "후드 집업",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 33,
            "category_name": "블루종/MA-1",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 34,
            "category_name": "레더/라이더스 재킷",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 35,
            "category_name": "무스탕/퍼",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 36,
            "category_name": "트러커 재킷",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 37,
            "category_name": "슈트/블레이저 재킷",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 38,
            "category_name": "카디건",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 39,
            "category_name": "아노락 재킷",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 40,
            "category_name": "플리스/뽀글이",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 41,
            "category_name": "트레이닝 재킷",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 42,
            "category_name": "스타디움 재킷",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 43,
            "category_name": "환절기 코트",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 44,
            "category_name": "겨울 싱글 코트",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 45,
            "category_name": "겨울 더블 코트",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 46,
            "category_name": "겨울 기타 코트",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 47,
            "category_name": "롱패딩/롱헤비 아우터",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 48,
            "category_name": "숏패딩/숏헤비 아우터",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 49,
            "category_name": "패딩 베스트",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 50,
            "category_name": "베스트",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 51,
            "category_name": "사파리/헌팅 재킷",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 52,
            "category_name": "나일론/코치 재킷",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 53,
            "category_name": "기타 아우터",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": []
          },
          {
            "category_id": 188,
            "category_name": "티셔츠",
            "category_english_name": null,
            "parent_category_id": 2,
            "sub_categories": [
              {
                "category_id": 23,
                "category_name": "반소매 티셔츠",
                "category_english_name": null,
                "parent_category_id": 188,
                "sub_categories": []
              },
              {
                "category_id": 25,
                "category_name": "긴소매 티셔츠",
                "category_english_name": null,
                "parent_category_id": 188,
                "sub_categories": []
              }
            ]
          }
        ]
      },
      {
        "category_id": 3,
        "category_name": "바지",
        "category_english_name": "Pants",
        "parent_category_id": null,
        "sub_categories": [
          {
            "category_id": 54,
            "category_name": "데님 팬츠",
            "category_english_name": null,
            "parent_category_id": 3,
            "sub_categories": []
          },
          {
            "category_id": 55,
            "category_name": "코튼 팬츠",
            "category_english_name": null,
            "parent_category_id": 3,
            "sub_categories": []
          },
          {
            "category_id": 56,
            "category_name": "슈트 팬츠/슬랙스",
            "category_english_name": null,
            "parent_category_id": 3,
            "sub_categories": []
          },
          {
            "category_id": 57,
            "category_name": "트레이닝/조거 팬츠",
            "category_english_name": null,
            "parent_category_id": 3,
            "sub_categories": []
          },
          {
            "category_id": 58,
            "category_name": "숏 팬츠",
            "category_english_name": null,
            "parent_category_id": 3,
            "sub_categories": []
          },
          {
            "category_id": 59,
            "category_name": "레깅스",
            "category_english_name": null,
            "parent_category_id": 3,
            "sub_categories": []
          },
          {
            "category_id": 60,
            "category_name": "점프 슈트/오버올",
            "category_english_name": null,
            "parent_category_id": 3,
            "sub_categories": []
          },
          {
            "category_id": 61,
            "category_name": "기타 바지",
            "category_english_name": null,
            "parent_category_id": 3,
            "sub_categories": []
          }
        ]
      },
      {
        "category_id": 4,
        "category_name": "원피스",
        "category_english_name": "Onepiece",
        "parent_category_id": null,
        "sub_categories": [
          {
            "category_id": 62,
            "category_name": "미니 원피스",
            "category_english_name": null,
            "parent_category_id": 4,
            "sub_categories": []
          },
          {
            "category_id": 63,
            "category_name": "미디 원피스",
            "category_english_name": null,
            "parent_category_id": 4,
            "sub_categories": []
          },
          {
            "category_id": 64,
            "category_name": "맥시 원피스",
            "category_english_name": null,
            "parent_category_id": 4,
            "sub_categories": []
          }
        ]
      }
    ]
  }
  ```
