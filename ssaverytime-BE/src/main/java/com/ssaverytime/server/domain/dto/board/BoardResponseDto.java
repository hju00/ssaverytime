package com.ssaverytime.server.domain.dto.board;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BoardResponseDto {
    private int boardId;
    
    // 작성자 정보
    private int userSeq;     // USER table PK (USER_ID)
    private String bojId;    // USER table BOJ_ID (로그인 ID)
    private String userName; // USER table NAME
    private String userTier; // USER table BAEKJOON (티어 정보)
    
    private String title;
    private String body;
    private String summary;
    private String visible; // '1' or '0'
    private int warningCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 집계 및 상태 정보 (DB 조회 시 계산)
    private int likeCount;
    private int commentCount;
    private boolean isLiked;
    private boolean isScrapped;
    private boolean isAuthor;
}