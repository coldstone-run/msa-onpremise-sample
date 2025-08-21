# msa-onpremise-sample

## 1. MSA 개념 및 필요성

### 1.1 Monolithic 아키텍쳐 개요
- **구조**: 하나의 코드베이스, 하나의 프로세스로 구성
- **장점**
  - 개발 초기 환경 단순하여 구조 이해가 빠름
  - 배포 절차가 단순
- **단점**
  - 유지보수성
    > 배포 혹은 장애 발생 시 전체 시스템 중단 위험
  - 확장성
  - 기술 종속성
    > 단일 기술 스택의 고착화

---


### 1.2 MSA 개요
- **구조**: 비즈니스 기능 중심의 작고 독립적인 서비스의 집합으로 구성
- **특징**
  - 유지보수성
    > 서비스별 독립 개발 및 배포 가능
  - 확장성
    > 필요한 서비스만 수평 확장 가능
  - 장애 격리
    > 특정 서비스 장애가 전체 시스템으로 확산되지 않음
- **단점**
  - 운영 복잡성 증가
    > 서비스별 설정파일, 빌드, 배포 작업이 서비스 수 만큼 필요
    .. 대안 설명할 것(docker-compose, cicd) ..
    
---


### 1.3 MSA 필수 구성요소
  - **Service Registry & Discovery**
    - 분산되어 있는 여러 서비스들의 위치를 자동으로 등록하고 찾는 기능 제공
    - 예시: Netflix Eureka, Spring Cloud Service Discovery 등
  - **API Gateway**
    - 외부 요청이 들어오는 단일 진입점(클라이언트 요청 라우팅, 인증/인가)
    - 예시: Spring Cloud Gateway, Nginx 등
  - **Load Balancer**
    - 동일 서비스의 여러 인스턴스에 대한 트래픽 균등 분산
    - 예시: HAProxy, Nginx 등
  - **Configuration Management**
    - 서비스마다 환경설정 파일이 존재 → 중앙 집중식 관리 필요
    - 예시: Spring Cloud Config, HashiCorp Vault

---


## 2. 실습 MSA 구조

### 2.1 프로젝트 구성
- **common**: 공통 모듈 (공통 DTO, 유틸리티 등)
- **discovery-server**: Eureka 기반 서비스 등록/발견 모듈
- **api-gateway**: Spring Cloud Gateway 기반 단일 진입점
- **config-server**: Spring Cloud Config 기반 각 서비스 설정 중앙 관리 (환경별 yml 관리)
- **user-service**: 사용자 관련 비즈니스 서비스
- **product-service**: 상품 관련 비즈니스 서비스
- **order-service**: 주문 관련 비즈니스 서비스

---

### 2.2 서비스 기동 및 요청/응답 흐름

#### 2.2.1. 서비스 기동 순서 및 준비
1. **Config Server 기동**
   - 각 서비스들의 `application.yml`과 같은 환경 설정 파일들을 중앙에서 관리
   - 예시: DB 연결 정보, 서비스 이름, 포트 등
2. **Discovery Server(Eureka) 기동**
   - 서비스들이 등록할 레지스트리 역할
   - Eureka 서버 실행 시 서비스들의 IP/Port와 이름을 기록
3. **각 서비스 기동**
   - `user-service`, `product-service`, `order-service`
   - 서비스 시작 시:
     - Config Server에서 환경 설정 가져오기
     - Eureka Discovery Server에 서버 정보 등록
4. **API Gateway 기동**
   - 클라이언트 요청 단일 진입점

#### 2.2.2. 사용자 요청 흐름
1. **사용자가 클라이언트(브라우저, 앱)에서 요청**
   - 예시: “사용자 조회” 요청
2. **API Gateway로 요청 도달**
   - 인증/인가 확인
   - 어떤 서비스로 보낼지 결정
3. **API Gateway → Discovery Server 사용**
   - Eureka를 조회하여 살아있는 `user-service` 인스턴스 IP/Port 확인
4. **API Gateway → 선택된 서비스 인스턴스로 요청 전달**
   - 복수의 인스턴스 존재시 트래픽 분산을 위한 로드밸런싱 적용

#### 2.2.3. 서비스 내부 처리
1. `user-service` 요청 수신
   - 비즈니스 로직 처리(공통 모듈 및 DB 등 사용하여 데이터 조회/저장)
   - 필요 시 `product-service`나 `order-service` 호출 (Eureka를 통해 동적 호출)

#### 2.2.4. 응답 반환
1. 서비스 처리 결과 → API Gateway로 전달
2. API Gateway에서 필요 시 응답 변환 → 클라이언트 전달
3. 클라이언트 최종 응답 수신
  - 예시: 사용자 목록 JSON

---

```mermaid
flowchart LR
    Client -->|요청| APIGateway[API Gateway]
    
    APIGateway -->|라우팅| UserService[user-service]
    APIGateway -->|라우팅| ProductService[product-service]
    APIGateway -->|라우팅| OrderService[order-service]
    
    UserService -->|조회/저장| Discovery[Discovery Server]
    ProductService -->|조회/저장| Discovery
    OrderService -->|조회/저장| Discovery

    UserService -->|환경설정 요청| Config[Config Server]
    ProductService -->|환경설정 요청| Config
    OrderService -->|환경설정 요청| Config

    Common[common 모듈] --- UserService
    Common --- ProductService
    Common --- OrderService
