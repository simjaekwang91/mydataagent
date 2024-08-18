# **금융 마이데이터 에이전트**
### 마이데이터 기술 가이드라인 및 API 스펙문서 조회 기능 제공
- 금융 마이데이터 기술 가이드 라인에 대해 궁금한 내용을 pdf를 찾아보지 않고 조회하는 에이전트 입니다.
- 사용자는 전체 대화 내역을 조회할 수 있습니다.
- 사용자는 질의 내용을 보내고 답변을 받아볼 수 있습니다.

# 기술스택
Spring Boot 3.3.2 로 구성하였으며 Kotlin 으로 작성되었습니다.
PDF 데이터를 올린 VectorDB 는 Weaviate로 구성하였습니다.
전체 대화 내용을 저장하기 위해 Mongo(embed)를 통해 구성했고
최근 10개 대화를 캐싱하기 위해 Redis(Embed)를 통해 구성하였습니다.
Spring AI 프레임워크를 이용해 OpenAI, Weaviate 를 연동하였습니다.
- Spring Boot 3.3.2
- Spring AI 1.0.0-SNAPSHOT
- JAVA 17 Version
- Weaviate
- JUnit 5
- Mongo(Embed)
- Redis(Embed)
- springdoc 2.2.0

# Usage
### 전체 대화 내용 조회
endpoint : /v1/openai/request-question

response : 

{
"errorCode": "Success",
"data": "정보 전송 요구 연장은 고객이 지정한 개인신용정보 전송요구 기간 만료 1개월 전부터 요청할 수 있습니다. 따라서 개인신용정보 전송요구 기간이 만료되기 전에 마이데이터사업자를 통해 연장 요청을 할 수 있습니다.",
"message": null
}

### 질의 응답
endpoint : /v1/openai/conversation/all/user1

response :

{
"errorCode": "Success",
"data": [
{
"id": "66c16c1162b4e730364f26bd",
"userId": "user1",
"roomId": "1",
"conversationList": [
{
"question": "API 스펙 중 aNS는 어떤 것을 뜻하나요?",
"response": "\"aNS\"는 \"aNS (1000)\"로 표시된 부분에서 타입과 길이가 함께 명시된 것으로 보아, 길이가 1000인 문자열 데이터 형식을 나타내는 것으로 해석됩니다. 따라서, \"aNS\"는 1000 길이의 문자열 데이터 형식을 나타내며, \"a\"는 문자열 데이터를 의미하는 것으로 추정됩니다.",
"createTime": "2024-08-18T03:35:45.083Z"
}
]
},
"message": null
}

# 상세 내용
### 패키지 구조
Hexagonal architecture 에 따라 Adaptor Application Domain 로 패키지 구조를 만들었습니다.

- adapter
  - in
    - restapi
      - controller
      - exception
      - mapper
  - out
    - aiapi
      - open ai api
    - cache
      - redis
    - database
      - monogo
    - vectordb
      - weaviate
- application
  - port
    - in
      - use case
    - out
      - ai port
      - cache port
      - conversation port
      - rag port
  - service
- domain

위 구조로 구성되어 있습니다.

### 기능 설명
호출 받는 프로토콜이 달라질 수도 있고 캐싱 혹은 전체 대화를 저장할 DB, VectorDB 가 달라질 수 있기 때문에 계층간 분리가 명확한 Hexagonal을 선택하였습니다.

PDF 정보를 weaviate에 업로드 한 방식은 먼저 PDF를 MarkDown 형식으로 데이터를 추출하고 '##'이나 '###' 같은

섹션 단위로 잘라서 데이터를 넣었습니다. 추후 ai api로 입력할때 토큰 길이를 고려하여 2차로 4000토큰을 초과하지 않게

잘라서 입력하였습니다(처음에는 문장 단위로 잘라봤는데 너무 잘게 잘려 제대로된 검색이 되지 않을것 같아 Markdown 으로 변경하여 섹션 단위로 구성하였습니다.)

멀티턴 구현을 하기 위해 Redis를 통해 최근 10개의 대화를 같이 보낼 수 있도록 하였습니다.(10개 제한을 둔 이유는 토큰 제한 고려)

전체 대화 내역 보관은 MongoDB를 통해 구현하였습니다.

만약 검색된 데이터 + 이전 대화 내용이 토큰 max 갯수를 초과한다면 오래된 대화 내용들 부터 제거해서 open ai 에 요청하도록 구성하였습니다.

추가로 전체 대화 내용이 조회가 가능하도록 Endpoint를 추가해두었고 

대화 내용은 userid & roomid 로 구성하여 같은 유저라도 채팅방이 다르면 새로운 채팅으로 인식하도록 구성 하였습니다.


### Feature 별 Endpoint
- 사용자는 전체 대화 내역 조회가 가능합니다.(v1/openai/conversation/all/{userid)
- 사용자는 질의 내용을 작성하고 답변을 받을 수 있습니다.(v1/openai/request-question)


### 기술적인 요구사항
- RAG
  - PDF를 weaviate 에 Markdown 형식으로 섹션 단위로 분리하여 구성하였습니다.
- OpenAI(LLM 모델)
  - OPEN AI LLM 모델을 선택하여 에이전트를 구성 하였습니다.
- 멀티턴 Agent
  - 이전 대화 내용을 같이 요청하여 멀티턴이 가능하도록 구성하였습니다.
- 헥사고날 패키지 구조 적용
  - 추후 확장 및 수정에 용이하도록 헥사고날 패키지 구조를 적용하였습니다.
- 토큰 제한
  - weaviate 에 저장할때 최대 토큰의 절반 수준으로 제한해서 잘라서 저장 하였습니다.
  - application level 에서 최대 토큰 넘지 않는지 확인 하여 이전 대화는 제외하고 요청 하였습니다.
