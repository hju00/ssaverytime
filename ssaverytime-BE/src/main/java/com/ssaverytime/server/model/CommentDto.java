package com.ssaverytime.server.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private int commentId;
    private int boardId;
    
    // 작성자 정보
    private int userSeq;     // USER table PK
    private String bojId;    // USER table BOJ_ID
    private String userName; // USER table NAME
    
    private String body;
    private String visible;
    private int warningCnt;
    private LocalDateTime createdAt;
}