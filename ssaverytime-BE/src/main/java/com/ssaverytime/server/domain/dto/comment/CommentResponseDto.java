package com.ssaverytime.server.domain.dto.comment;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private int commentId;
    private int boardId;
    
    // 작성자 정보
    private Integer userSeq;     // USER table PK (익명일 경우 NULL 가능)
    private String bojId;    // USER table BOJ_ID
    private String userName; // USER table NAME
    private String userTier; // 티어 정보
    
    private String body;
    private String visible; // '1' or '0' (익명 여부)
    private LocalDateTime createdAt;
    
    private boolean isAuthor; // 본인 댓글 여부
}
