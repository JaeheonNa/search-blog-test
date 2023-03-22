# search-blog-test
## 목차
0. [개요](#0-개요)  
1. [블로그 검색을 위한 Combo 조회용 API](#1-블로그-검색을-위한-Combo-조회용-API)  
2. [블로그 조회용 API](#2-블로그-조회용-API)  
3. [사용 오픈 소스](#3-사용-오픈-소스)  
4. [jar 파일 URL](#4-jar-파일-URL)  

## 0. 개요
    블로그 검색 및 인기 검색어 순위 조회 API입니다.
### 0.1. 동작 순서
'''
    1. client의 요청이 들어오면 우선 검색어를 H2에 저장합니다.
    2. serviceFactory로부터 모듈에 요청을 보낼 객체를 받아옵니다.
    3. kakao API에 요청을 보낼 모듈을 호출합니다.
        3-1. kakaoAPI 요청 모듈이 kakaoAPI로 요청을 보낸 후 응답을 받아 응답합니다.
        3-2. 만약 kakaoAPI 혹은 kakaoAPI 호출 모듈이 정상적인 상태가 아니거나 응답 시간이 느려지면 같은 방식으로 naverAPI 요청 모델을 통해 naverAPI로 요청합니다.
    4. redis에 검색어를 저장합니다.
    5. redis에서 10위까지 검색어 순위를 조회합니다.
        5-1. redis가 비어있을 경우 H2를 조회합니다.
    6. 검색 결과와 검색어 순위 결과를 Map에 담아 응답합니다.
'''

##### 참고 #####
    반드시 아래 프로젝트 중 하나라도 구동 중이어야 동작합니다.
    (https://github.com/JaeheonNa/search-blog-call-api-2)
    (https://github.com/JaeheonNa/search-blog-call-api-2)

## 1. 블로그 검색을 위한 Combo 조회용 API   
### 1.1. 기본 정보
|URL|HTTP 메서드|   
|:--|:--------|   
|localhost:8080/search/blog/combo|GET|   
### 1.2. 요청 데이터
|NAME|TYPE|DESCRIPTION|REQUIRED|   
|:---|:---|:----------|:-------|   

### 1.3. 응답 데이터 
|NAME|TYPE|DESCRIPTION|   
|:---|:---|:----------|   
|SizeCombo|list<number>|한 페이지에 표시할 컨텐츠 수. 현재 10부터 10단위로 50까지 출력|   
|SortCombo|list<string>|정렬 순서. 정확순: accuracy, 최신순: recency|   

### 1.4. Sample
#### 1.4.1. 요청
```
curl -v -X GET "http://localhost:8080/search/blog/combo"
```
#### 1.4.2. 응답
```
{
    "SizeCombo": [10, 20, 30, 40, 50],
    "SortCombo": [ "accuracy", "recency"]
}
```
  
## 2. 블로그 조회용 API  
### 2.1. 기본 정보
|URL|HTTP 메서드|   
|:--|:--------|   
|localhost:8080/search/blog|GET|   
### 2.2. 요청 데이터
|NAME|TYPE|DESCRIPTION|REQUIRED|  
|:---|:---|:----------|:-------|   
|query|string|검색어|true|   
|page|number|결과 페이지 번호, 기본값 1|false|   
|sort|number|결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy|false|   
|size|number|한 페이지에 보여질 문서 수, 10~50까지 10단위로 제공, 기본값 10|false|   
|apiType|string|요청할 API, kakao(카카오API) 또는 naver(네이버API), 기본값 kakao|false|   

### 2.3. 응답 데이터
|NAME|TYPE|DESCRIPTION|   
|:---|:---|:----------|   
|** blogSearchResult(JSON Array) **|||   
|** documents(JSON Array) **|||   
|blogname|string|블로그의 이름|   
|contents|string|블로그 글 요약|   
|datetime|string|블로그 글 작성시간, ISO 8601 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz], 네이버 검색 시 yyyyMMdd|   
|thumbnail|string|검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음, 네이버 검색 시 null|   
|title|string|블로그 글 제목|   
|url|string|블로그 글 URL|   
|** meta(JSON Array) **|||   
|total_count|number|검색된 문서 수|   
|pageable_count|number|total_count 중 노출 가능 문서 수, 네이버 검색 시 null|   
|is_end|boolean|현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음|   
|** requestParam(JSON Array) **|||   
|query|string|검색어 요청값|   
|page|number|결과 페이지 번호 요청값|   
|sort|number|결과 문서 정렬 방식 요청값|   
|size|number|한 페이지에 보여질 문서 수 요청값|   
|apiType|string|요청 API 타입 요청값|   
|** searchRankingResult(JSON Array) **|||   
|keyword|string|인기 검색어, 최근 24시간 기준|   
|searchCount|number|검색량, 최근 24시간 기준|   
|ranking|number|검색 순위, 최근 24시간 기준|   

### 2.4. Sample
#### 2.4.1. 요청
```
curl -v -X GET "http://localhost:8080/search/blog?query=카카오뱅크&size=1&page=1&sort=accuracy"
```
#### 2.4.2. 응답
```
{
    "blogSearchResult": {
        "documents": [
            {
                "blogname": "하운드쉐어",
                "contents": "그랬던 것처럼 보증금 낼 돈이 500만 원이 전부라면 어떻게 해야 할까요. 5분만 아니 3분만 보시면 두 번째 집으로 이사 가실 수 있게 도와드리겠습니다. <b>카카오</b><b>뱅크</b> 전월세보증금 총정리 공인중개사 현직에서 일하다 보면 많은 분들이 <b>카카오</b><b>뱅크</b> 전월세보증금에 대해 문의하십니다. 잠깐 살펴보면 너무 단순하고 간편...",
                "datetime": "2023-03-19T12:43:37.000+09:00",
                "thumbnail": "https://search1.kakaocdn.net/argon/130x130_85_c/1mn5IQryX0g",
                "title": "<b>카카오</b><b>뱅크</b> 전월세보증금 총정리",
                "url": "http://hwshare07.com/48"
            }
        ],
        "meta": {
            "is_end": false,
            "pageable_count": 800,
            "total_count": 484586
        },
        "requestParam": {
            "size": 1,
            "query": "카카오뱅크",
            "sort": "accuracy",
            "page": 1
        }
    },
    "searchRankingResult": [
        {
            "keyword": "카카오뱅크",
            "searchCount": 15,
            "rank": 1
        },
        {
            "keyword": "대출",
            "searchCount": 14,
            "rank": 2
        },
        {
            "keyword": "주택담보대출",
            "searchCount": 13,
            "rank": 3
        },
        {
            "keyword": "카카오톡",
            "searchCount": 11,
            "rank": 4
        },
        {
            "keyword": "집짓기",
            "searchCount": 9,
            "rank": 5
        },
        {
            "keyword": "스쿠버",
            "searchCount": 8,
            "rank": 6
        },
        {
            "keyword": "카카오",
            "searchCount": 5,
            "rank": 7
        },
        {
            "keyword": "너는내운명",
            "searchCount": 3,
            "rank": 8
        },
        {
            "keyword": "상환",
            "searchCount": 2,
            "rank": 9
        },
        {
            "keyword": "모기지",
            "searchCount": 1,
            "rank": 10
        }
    ]
}
```
  
## 3. 사용 오픈 소스
### 3.1. Hystrix
#### 3.1.1 사용 목적
Kakao 블로그 검색 API 서버 장애 발생 시, 자동으로 Naver 블로그 검색 API 호출토록 하기 위해 사용했습니다.

### 3.2. redis
#### 3.2.1 사용 목적
인기 검색어 랭킹을 관리하기 위해 사용했습니다.
    
### 3.3 H2
#### 3.3.1 사용 목적
redis가 비어 있을 경우를 대비해 인기 검색어 랭킹을 관리하기 위해 사용해습니다.

## 4. jar 파일 URL    
    https://drive.google.com/drive/folders/1EDg2K_fZUoOv_zHIVx-hPOjbp1MkmZOQ?usp=sharing
    
    
# 감사합니다.
