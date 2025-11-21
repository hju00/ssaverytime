# ssaveryTime Database

## ERD (Entity-Relationship Diagram)

![ERD](img/erd_diagram.png)

## Database Schema.

### `USER`

사용자 정보를 저장하는 테이블

| Column | Type | Description |
|---|---|---|
| `USER_ID` | INT | 사용자 고유 ID (PK) |
| `USERNAME` | VARCHAR(255) | 사용자 아이디 (UNIQUE) |
| `PASSWORD` | VARCHAR(255) | 사용자 비밀번호 |
| `NAME` | VARCHAR(100) | 사용자 이름 |
| `ROLE` | ENUM('USER', 'ADMIN') | 사용자 역할 (기본값: 'USER') |
| `SEASON` | INT | 사용자 기수 |
| `BAEKJOON` | VARCHAR(100) | 백준 아이디 |
| `VALID` | ENUM('1', '0') | 계정 활성화 여부 |
| `CREATED_AT` | DATETIME | 계정 생성일 |

### `BOARD`

게시글 정보를 저장하는 테이블

| Column | Type | Description |
|---|---|---|
| `BOARD_ID` | INT | 게시글 고유 ID (PK) |
| `USER_ID` | INT | 작성자 ID (FK) |
| `TITLE` | VARCHAR(255) | 게시글 제목 |
| `BODY` | TEXT | 게시글 내용 |
| `SUMMARY` | TEXT | 게시글 AI 요약 내용 |
| `VISIBLE` | ENUM('1', '0') | 게시글 공개 여부 (기본값: '1') |
| `WARNING_CNT` | INT | 경고 횟수 (기본값: 0) |
| `CREATED_AT` | DATETIME | 게시글 작성일 |

### `COMMENT`

댓글 정보를 저장하는 테이블

| Column | Type | Description |
|---|---|---|
| `COMMENT_ID` | INT | 댓글 고유 ID (PK) |
| `BOARD_ID` | INT | 게시글 ID (FK) |
| `USER_ID` | INT | 작성자 ID (FK) |
| `BODY` | VARCHAR(500) | 댓글 내용 |
| `VISIBLE` | ENUM('1', '0') | 댓글 공개 여부 (기본값: '1') |
| `WARNING_CNT` | INT | 경고 횟수 (기본값: 0) |
| `CREATED_AT` | DATETIME | 댓글 작성일 |

### `LIKES`

게시글 좋아요 정보를 저장하는 테이블

| Column | Type | Description |
|---|---|---|
| `LIKES_ID` | INT | 좋아요 고유 ID (PK) |
| `BOARD_ID` | INT | 게시글 ID (FK) |
| `USER_ID` | INT | 사용자 ID (FK) |

### `SCRAP`

게시글 스크랩 정보를 저장하는 테이블

| Column | Type | Description |
|---|---|---|
| `SCRAP_ID` | INT | 스크랩 고유 ID (PK) |
| `USER_ID` | INT | 사용자 ID (FK) |
| `BOARD_ID` | INT | 게시글 ID (FK) |

### `AI_TOKEN`

AI 토큰 정보를 저장하는 테이블

| Column | Type | Description |
|---|---|---|
| `TOKEN_ID` | INT | 토큰 고유 ID (PK) |
| `REST` | INT | 남은 토큰 수 (기본값: 0) |
| `LAST_UPDATE` | DATETIME | 마지막 업데이트 시간 |

### `RESTAURANT`

식당 정보를 저장하는 테이블

| Column | Type | Description |
|---|---|---|
| `RESTAURANT_ID` | INT | 식당 고유 ID (PK) |
| `NAME` | VARCHAR(255) | 식당 이름 |

### `MENU`

메뉴 정보를 저장하는 테이블

| Column | Type | Description |
|---|---|---|
| `MENU_ID` | INT | 메뉴 고유 ID (PK) |
| `RESTAURANT_ID` | INT | 식당 ID (FK) |
| `MENU` | VARCHAR(255) | 메뉴 이름 |
| `DATE` | DATETIME | 날짜 |

### `STAR`

별점 정보를 저장하는 테이블

| Column | Type | Description |
|---|---|---|
| `STAR_ID` | INT | 별점 고유 ID (PK) |
| `USER_ID` | INT | 사용자 ID (FK) |
| `RESTAURANT_ID` | INT | 식당 ID (FK) |
| `CATEGORY` | ENUM('TASTE', 'AMOUNT') | 별점 카테고리 |
| `SCORE` | INT | 점수 |
| `DATE` | DATETIME | 별점 등록일 |

## Dummy Data

### `USER`

| USERNAME | PASSWORD | NAME | ROLE | SEASON | BAEKJOON | VALID |
|---|---|---|---|---|---|---|
| kinguser | hashed_pw_01 | 유저왕 | USER | 14 | GOLD V | 1 |
| admin_guy | hashed_pw_admin | 최고관리자 | ADMIN | NULL | NULL | 1 |
| season_2_user | hashed_pw_03 | 계절이 | USER | 13 | SILVER III | 1 |
| temp_disabled | hashed_pw_04 | 잠시휴식 | USER | 15 | PLATINUM V | 0 |
| active_member | hashed_pw_05 | 활동회원 | USER | 15 | BRONZE I | 1 |

### `RESTAURANT`

| NAME |
|---|
| 육수고집 |
| 소담상 |
| 더고메 |
| 차이나호 |
| 속이찬새참 |

### `BOARD`

| USER_ID | TITLE | BODY | VISIBLE | WARNING_CNT |
|---|---|---|---|---|
| 1 | 백반집 점심 후기 | 오늘 백반집 가봤는데 가성비 최고예요. | 1 | 0 |
| 5 | 이번 시즌 알고리즘 질문 | 다들 백준 문제 푸시나요? | 1 | 0 |
| 3 | 솔직히 실버들은 발언 허락 받고 해야한다 생각합니다. | 광주캠퍼스 노트북 쓰기 좋은 카페 아시는 분? | 1 | 0 |
| 2 | 운영 공지: 서비스 업데이트 안내 | 새로운 기능이 추가될 예정입니다. | 1 | 0 |
| 1 | 숨김 테스트 게시글 | 이 글은 관리자가 숨길 수 있습니다. | 0 | 0 |

### `COMMENT`

| BOARD_ID | USER_ID | BODY | VISIBLE | WARNING_CNT |
|---|---|---|---|---|
| 1 | 3 | 메뉴가 궁금해요! | 1 | 0 |
| 2 | 5 | 저는 요즘 DP 문제 풀고 있습니다. | 1 | 0 |
| 3 | 1 | 프리미엄 로스터리 추천합니다. | 1 | 0 |
| 4 | 5 | 업데이트 기대하겠습니다! | 1 | 0 |
| 1 | 4 | 혼밥하기 괜찮은가요? | 1 | 0 |

### `LIKES`

| BOARD_ID | USER_ID |
|---|---|
| 1 | 5 |
| 2 | 1 |
| 2 | 3 |
| 3 | 5 |
| 4 | 1 |

### `SCRAP`

| USER_ID | BOARD_ID |
|---|---|
| 5 | 1 |
| 1 | 3 |
| 3 | 4 |
| 5 | 4 |
| 2 | 2 |

### `AI_TOKEN`

| REST | LAST_UPDATE |
|---|---|
| 10 | DATE_SUB(NOW(), INTERVAL 3 HOUR) |

### `MENU`

| RESTAURANT_ID | MENU | DATE |
|---|---|---|
| 1 | 제육볶음 정식 | 2025-11-07 12:00:00 |
| 2 | 클래식 치즈 버거 | 2025-11-07 12:00:00 |
| 3 | 탄탄멘 | 2025-11-07 12:00:00 |
| 4 | 에티오피아 예가체프 | 2025-11-07 12:00:00 |
| 5 | 슈퍼 디럭스 피자 | 2025-11-06 18:30:00 |

### `STAR`

| USER_ID | RESTAURANT_ID | CATEGORY | SCORE |
|---|---|---|---|
| 1 | 1 | TASTE | 5 |
| 5 | 1 | AMOUNT | 4 |
| 3 | 2 | TASTE | 3 |
| 1 | 3 | TASTE | 5 |
| 5 | 5 | AMOUNT | 4 |
