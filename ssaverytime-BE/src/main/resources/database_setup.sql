-- 클라이언트 문자셋 설정
SET NAMES 'utf8mb4';

-- 데이터베이스 생성 및 선택 (Docker Compose에서 이미 생성됨)
-- CREATE DATABASE IF NOT EXISTS ssaverytime
--     DEFAULT CHARACTER SET utf8mb4
--     COLLATE utf8mb4_general_ci;
-- USE ssaverytime;

-- 외래키 체크 비활성화 (테이블 삭제/생성 시 순서 문제 방지)
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS COMMENT;
DROP TABLE IF EXISTS LIKES;
DROP TABLE IF EXISTS SCRAP;
DROP TABLE IF EXISTS AI_TOKEN;
DROP TABLE IF EXISTS MENU;
DROP TABLE IF EXISTS STAR;
DROP TABLE IF EXISTS ANONYMOUS_BOARD_AUTHORSHIP;
DROP TABLE IF EXISTS ANONYMOUS_COMMENT_AUTHORSHIP;
DROP TABLE IF EXISTS BOARD;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS RESTAURANT;
DROP TABLE IF EXISTS FOLLOW;
DROP TABLE IF EXISTS REPORT;
DROP TABLE IF EXISTS PERSONAL_DIET;

-- 1. 테이블 생성
CREATE TABLE USER (
                      USER_ID    INT    NOT NULL AUTO_INCREMENT,
                      BOJ_ID    VARCHAR(255)    NOT NULL UNIQUE,
                      PASSWORD    VARCHAR(255)    NOT NULL,
                      NAME    VARCHAR(100)    NOT NULL,
                      ROLE    ENUM('USER', 'ADMIN')    NOT NULL DEFAULT 'USER',
                      SEASON    INT    NULL,
                      CAMPUS    VARCHAR(50) NULL,
                      BAEKJOON    VARCHAR(100) NULL,
                      VALID ENUM('VALID', 'INVALID') NOT NULL,
                      CREATED_AT    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      PRIMARY KEY (USER_ID)
);

CREATE TABLE RESTAURANT (
                            RESTAURANT_ID    INT    NOT NULL AUTO_INCREMENT,
                            NAME    VARCHAR(255)    NOT NULL,
                            PRIMARY KEY (RESTAURANT_ID)
);

CREATE TABLE BOARD (
                       BOARD_ID    INT    NOT NULL AUTO_INCREMENT,
                       USER_ID    INT    NULL,
                       TITLE    VARCHAR(255)    NOT NULL,
                       BODY    TEXT    NULL,
                       SUMMARY    TEXT    NULL DEFAULT NULL,
                       AUTHOR_TIER VARCHAR(50) NULL,
                       VISIBLE    ENUM('1', '0')    NOT NULL DEFAULT '1',
                       WARNING_CNT    INT    NOT NULL DEFAULT 0,
                       CREATED_AT    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       UPDATED_AT    DATETIME    NULL,
                       VALID    ENUM('1', '0')    NOT NULL DEFAULT '1',
                       PRIMARY KEY (BOARD_ID),
                       CONSTRAINT FK_USER_TO_BOARD FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE
);

CREATE TABLE ANONYMOUS_BOARD_AUTHORSHIP (
                                            AA_ID    INT    NOT NULL AUTO_INCREMENT,
                                            BOARD_ID    INT    NOT NULL,
                                            AUTHOR_HASH    VARCHAR(256)    NOT NULL,
                                            CREATED_AT    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            PRIMARY KEY (AA_ID),
                                            CONSTRAINT FK_BOARD_TO_AA FOREIGN KEY (BOARD_ID) REFERENCES BOARD (BOARD_ID) ON DELETE CASCADE
);

CREATE TABLE COMMENT (
                         COMMENT_ID    INT    NOT NULL AUTO_INCREMENT,
                         BOARD_ID    INT    NOT NULL,
                         USER_ID    INT    NULL,
                         BODY    VARCHAR(500)    NOT NULL,
                         AUTHOR_TIER VARCHAR(50) NULL,
                         VISIBLE    ENUM('1', '0')    NOT NULL DEFAULT '1',
                         WARNING_CNT    INT    NOT NULL DEFAULT 0,
                         CREATED_AT    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         VALID    ENUM('1', '0')    NOT NULL DEFAULT '1',
                         PRIMARY KEY (COMMENT_ID),
                         CONSTRAINT FK_BOARD_TO_COMMENT FOREIGN KEY (BOARD_ID) REFERENCES BOARD (BOARD_ID) ON DELETE CASCADE,
                         CONSTRAINT FK_USER_TO_COMMENT FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE
);

CREATE TABLE ANONYMOUS_COMMENT_AUTHORSHIP (
                                              ACA_ID    INT    NOT NULL AUTO_INCREMENT,
                                              COMMENT_ID    INT    NOT NULL,
                                              AUTHOR_HASH    VARCHAR(256)    NOT NULL,
                                              CREATED_AT    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                              PRIMARY KEY (ACA_ID),
                                              CONSTRAINT FK_COMMENT_TO_ACA FOREIGN KEY (COMMENT_ID) REFERENCES COMMENT (COMMENT_ID) ON DELETE CASCADE
);

CREATE TABLE LIKES (
                       LIKES_ID    INT    NOT NULL AUTO_INCREMENT,
                       BOARD_ID    INT    NOT NULL,
                       USER_ID    INT    NOT NULL,
                       PRIMARY KEY (LIKES_ID),
                       UNIQUE KEY UK_LIKES_BOARD_USER (BOARD_ID, USER_ID),
                       CONSTRAINT FK_BOARD_TO_LIKES FOREIGN KEY (BOARD_ID) REFERENCES BOARD (BOARD_ID) ON DELETE CASCADE,
                       CONSTRAINT FK_USER_TO_LIKES FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE
);

CREATE TABLE SCRAP (
                       SCRAP_ID    INT    NOT NULL AUTO_INCREMENT,
                       USER_ID    INT    NOT NULL,
                       BOARD_ID    INT    NOT NULL,
                       PRIMARY KEY (SCRAP_ID),
                       UNIQUE KEY UK_SCRAP_USER_BOARD (USER_ID, BOARD_ID),
                       CONSTRAINT FK_USER_TO_SCRAP FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE,
                       CONSTRAINT FK_BOARD_TO_SCRAP FOREIGN KEY (BOARD_ID) REFERENCES BOARD (BOARD_ID) ON DELETE CASCADE
);

CREATE TABLE AI_TOKEN (
                          TOKEN_ID    INT    NOT NULL AUTO_INCREMENT,
                          REST    INT    NOT NULL DEFAULT 0,
                          LAST_UPDATE    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (TOKEN_ID)
);

CREATE TABLE MENU (
                      MENU_ID    INT    NOT NULL AUTO_INCREMENT,
                      RESTAURANT_ID    INT    NOT NULL,
                      MENU    VARCHAR(255)    NOT NULL,
                      CALORIE INT NULL,
                      DATE    DATETIME    NULL,
                      PRIMARY KEY (MENU_ID),
                      CONSTRAINT FK_RESTAURANT_TO_MENU FOREIGN KEY (RESTAURANT_ID) REFERENCES RESTAURANT (RESTAURANT_ID) ON DELETE CASCADE
);

CREATE TABLE STAR (
                      STAR_ID    INT    NOT NULL AUTO_INCREMENT,
                      USER_ID    INT    NOT NULL,
                      RESTAURANT_ID    INT    NOT NULL,
                      CATEGORY    ENUM('TASTE', 'AMOUNT')    NOT NULL,
                      SCORE     INT     NOT NULL,
                      DATE    DATETIME    NULL,
                      PRIMARY KEY (STAR_ID),
                      CONSTRAINT FK_USER_TO_STAR FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE,
                      CONSTRAINT FK_RESTAURANT_TO_STAR FOREIGN KEY (RESTAURANT_ID) REFERENCES RESTAURANT (RESTAURANT_ID) ON DELETE CASCADE
);

-- 생성 안 되어있어서 추가함
CREATE TABLE FOLLOW (
                        FOLLOW_ID    INT    NOT NULL AUTO_INCREMENT,
                        BOJ_ID1    VARCHAR(255)    NOT NULL,
                        BOJ_ID2    VARCHAR(255)    NOT NULL,
                        DATE    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (FOLLOW_ID)
);

-- 새롭게 추가된 테이블
CREATE TABLE PERSONAL_DIET (
                               DIET_ID    INT NOT NULL AUTO_INCREMENT,
                               USER_ID    INT NOT NULL,
                               MENU       VARCHAR(255) NOT NULL,
                               CALORIE    INT NOT NULL,
                               DATE       DATETIME NOT NULL,
                               PRIMARY KEY (DIET_ID),
                               CONSTRAINT FK_USER_TO_PERSONAL_DIET
                                   FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE
);


CREATE TABLE REPORT (
                        REPORT_ID INT NOT NULL AUTO_INCREMENT,
                        USER_ID INT NOT NULL,
                        TARGET_TYPE ENUM('BOARD', 'COMMENT') NOT NULL,
                        TARGET_ID INT NOT NULL,
                        REASON VARCHAR(255) NULL,
                        CREATED_AT DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (REPORT_ID),
                        UNIQUE KEY UK_REPORT_USER_TARGET (USER_ID, TARGET_TYPE, TARGET_ID)
);

-- 외래키 체크 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;


-- 3. 더미 데이터 삽입

-- 1) USER
INSERT INTO USER (USER_ID, BOJ_ID, PASSWORD, NAME, ROLE, SEASON, BAEKJOON, VALID) VALUES
(1, 'user1', '$2b$12$eSZpjmFn39nQ0D4JOBm9LeSTVdVt622kblRURpSl7g4.erPx29sAK', '사용자1', 'USER', 14, 'GOLD V', 'VALID'),
(2, 'admin', '$2b$12$3dNqdLQq79pLGPRFIVZYU.TfG5ckq87FDhkPM3i57KRgn30mq2QLO', '관리자', 'ADMIN', NULL, NULL, 'VALID'),
(3, 'user2', '$2b$12$APevtl5nAKpcECKMDQCXwuXZNv8sPPNg0CHkavZntFdY8IwksD8j6', '사용자2', 'USER', 13, 'SILVER III', 'VALID'),
(4, 'user3', '$2b$12$IRwkimGsBKz2GrLbtDMcteXRxdOyzl2ttCCT6voqvJIpiFc.br/vS', '사용자3', 'USER', 15, 'PLATINUM V', 'VALID'),
(5, 'user4', '$2b$12$IJi0SslqBYqkgl7liP7ZsOb4YXGd0NhlPOfjCGZ7y1Y0vmtQ/xZ.G', '사용자4', 'USER', 15, 'BRONZE I', 'VALID'),
(6, 'user5', '$2b$12$vNvNSlarxuBX/dBIdaHtzOWDx6Ag9WAu8ephfAty7wzI0Pu6Ud462', '사용자5', 'USER', 15, 'GOLD I', 'VALID');

-- 2) RESTAURANT
INSERT INTO RESTAURANT (RESTAURANT_ID, NAME) VALUES
                                                 (1, '육수고집'),
                                                 (2, '소담상'),
                                                 (3, '더고메'),
                                                 (4, '차이나호'),
                                                 (5, '속이찬새참');

-- 3) BOARD (수정됨: 컬럼 7개, 값 7개 맞춤)
INSERT INTO BOARD (BOARD_ID, USER_ID, TITLE, BODY, VISIBLE, WARNING_CNT, AUTHOR_TIER) VALUES
(1, 1, '백반집 점심 후기', '오늘 백반집 가봤는데 가성비 최고예요.', '1', 0, NULL),
(2, 5, '이번 시즌 알고리즘 질문', '다들 백준 문제 푸시나요?', '1', 0, NULL),
(3, 3, '솔직히 실버들은 발언 허락 받고 해야한다 생각합니다.', '광주캠퍼스 노트북 쓰기 좋은 카페 아시는 분?', '1', 0, NULL),
(4, 2, '운영 공지: 서비스 업데이트 안내', '새로운 기능이 추가될 예정입니다.', '1', 0, NULL),
(5, NULL, '숨김 테스트 게시글', '이 글은 관리자가 숨길 수 있습니다.',  '0', 0, 'GOLD V');

-- 4) COMMENT
INSERT INTO COMMENT (COMMENT_ID, BOARD_ID, USER_ID, BODY, VISIBLE, WARNING_CNT) VALUES
(1, 1, 3, '메뉴가 궁금해요!', '1', 0),
(2, 2, 5, '저는 요즘 DP 문제 풀고 있습니다.', '1', 0),
(3, 3, 1, '프리미엄 로스터리 추천합니다.', '1', 0),
(4, 4, 5, '업데이트 기대하겠습니다!', '1', 0),
(5, 1, 4, '혼밥하기 괜찮은가요?', '1', 0);

-- 5) LIKES
INSERT INTO LIKES (LIKES_ID, BOARD_ID, USER_ID) VALUES
                                                    (1, 1, 5),
                                                    (2, 2, 1),
                                                    (3, 2, 3),
                                                    (4, 3, 5),
                                                    (5, 4, 1);

-- 6) SCRAP
INSERT INTO SCRAP (SCRAP_ID, USER_ID, BOARD_ID) VALUES
                                                    (1, 5, 1),
                                                    (2, 1, 3),
                                                    (3, 3, 4),
                                                    (4, 5, 4),
                                                    (5, 2, 2);

-- 7) AI_TOKEN
INSERT INTO AI_TOKEN (TOKEN_ID, REST, LAST_UPDATE) VALUES
    (1, 10, DATE_SUB(NOW(), INTERVAL 3 HOUR));

-- 8) MENU
INSERT INTO MENU (MENU_ID, RESTAURANT_ID, MENU, CALORIE, DATE) VALUES
                                                                   (1, 1, '제육볶음 정식', 100, '2025-11-07 12:00:00'),
                                                                   (2, 2, '클래식 치즈 버거', 200, '2025-11-07 12:00:00'),
                                                                   (3, 3, '탄탄멘', 300, '2025-11-07 12:00:00'),
                                                                   (4, 4, '에티오피아 예가체프', 400, '2025-11-07 12:00:00'),
                                                                   (5, 5, '슈퍼 디럭스 피자', 500, '2025-11-06 18:30:00');

-- 9) STAR
INSERT INTO STAR (STAR_ID, USER_ID, RESTAURANT_ID, CATEGORY, SCORE) VALUES
                                                                        (1, 1, 1, 'TASTE', 5),
                                                                        (2, 5, 1, 'AMOUNT', 4),
                                                                        (3, 3, 2, 'TASTE', 3),
                                                                        (4, 1, 3, 'TASTE', 5),
                                                                        (5, 5, 5, 'AMOUNT', 4);