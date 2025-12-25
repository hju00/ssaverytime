<div align="center">
  <img src="img/logo.png" alt="SsaveryTime Logo" width="150" height="150" />
  
  # SsaveryTime (싸브리타임)
  
  **"싸피인들의 소중한 시간을 아껴드립니다."**
  <br/>
  삼성 청년 SW 아카데미(SSAFY) 교육생들을 위한 통합 정보 공유 및 커뮤니티 플랫폼
  <br/>
</div>

---

## Tech Stack

### Frontend
![Vue.js](https://img.shields.io/badge/Vue.js-3.5.13-4FC08D?logo=vue.js&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-6.0.5-646CFF?logo=vite&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.4.17-06B6D4?logo=tailwindcss&logoColor=white)
![Shadcn Vue](https://img.shields.io/badge/Shadcn_Vue-Latest-000000?logo=shadcnui&logoColor=white)

### Backend
![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.8-6DB33F?logo=springboot&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-3.0.4-C4002B?logo=mybatis&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)

### AI & Data
![Python](https://img.shields.io/badge/Python-3.11-3776AB?logo=python&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-0.109.0-009688?logo=fastapi&logoColor=white)
![LangChain](https://img.shields.io/badge/LangChain-0.1.0-1C3C3C?logo=langchain&logoColor=white)
![OpenAI GPT-4o](https://img.shields.io/badge/GPT--4o-mini-412991?logo=openai&logoColor=white)

### DevOps & Monitoring
![Docker](https://img.shields.io/badge/Docker-27.4.0-2496ED?logo=docker&logoColor=white)
![AWS EC2](https://img.shields.io/badge/AWS_EC2-Orange?logo=amazon-aws&logoColor=white)
![AWS RDS](https://img.shields.io/badge/AWS_RDS-527FFF?logo=amazon-rds&logoColor=white)
![Prometheus](https://img.shields.io/badge/Prometheus-2.55.1-E6522C?logo=prometheus&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-11.3.0-F46800?logo=grafana&logoColor=white)

---

## Architecture

<div align="center">
  <img src="img/architecture/system_architecture.png" alt="System Architecture" width="800" />
</div>

*   **Frontend:** Vue 3 + Vite 기반의 SPA. EC2 내에서 Docker 컨테이너로 실행됩니다.
*   **Backend:** Spring Boot API 서버. RESTful API를 제공하며 MySQL RDS와 통신합니다.
*   **AI Service:** Python FastAPI로 구축된 마이크로서비스. 게시글 요약 기능을 담당합니다.
*   **Infra:** AWS EC2 인스턴스 하나에 Docker Compose를 이용하여 모든 서비스(FE, BE, AI, Monitoring)를 오케스트레이션합니다.
*   **Monitoring:** Prometheus가 Spring Boot Actuator 메트릭을 수집하고, Grafana를 통해 시각화합니다.

---

## Key Features

1.  **게시판 & 커뮤니티:** 익명/실명 게시글 작성, 댓글, 좋아요, 스크랩 기능.
2.  **알고리즘 랭크 연동:** Solved.ac와 연동하여 사용자의 백준 티어 정보를 실시간으로 프로필과 게시글에 표시합니다.
3.  **AI 게시글 요약:** 긴 게시글 내용을 AI(GPT-4o mini)가 3줄로 요약해줍니다.
4.  **식단 정보:** SSAFY 캠퍼스별 식단 정보를 제공합니다.
5.  **반응형 UI:** 데스크탑과 모바일 환경 모두에 최적화된 사용자 경험을 제공합니다.

---

## Team

| 이름 | 학번 | 역할 |
| :---: | :---: | :--- |
| **박형주** | 1412288 | **Backend / DevOps**<br>- Spring Boot API 개발<br>- AWS EC2 & RDS 배포<br>- Docker & Monitoring 구축<br>- Solved.ac 연동 |
| **심동근** | 1415822 | **Frontend / UI**<br>- Vue 3 화면 설계 및 구현<br>- Shadcn UI 디자인 시스템 적용<br>- 모바일 반응형 구현<br>- API 연동 |  |

---

## License

This project is licensed under the MIT License.
