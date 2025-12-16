package com.ssaverytime.server.domain.dto.comment;

import lombok.Data;

@Data
public class CommentRequestDto {
    private int commentId; // 수정 시 사용
    private int boardId;   // 작성 시 사용 (URL에서 받을 수도 있지만 DTO에도 포함 가능)
    
    private Integer userSeq;   // 작성자 PK (Service에서 주입)
    private String authorTier; // 작성 시점 티어 (Service에서 주입)
    private String body;
    private String visible; // '1' or '0' (익명 선택)
}
