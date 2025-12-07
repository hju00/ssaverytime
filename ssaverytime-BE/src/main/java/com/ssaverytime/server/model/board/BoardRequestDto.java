package com.ssaverytime.server.model.board;

import lombok.Data;

@Data
public class BoardRequestDto {
    private int boardId;      // 수정 시 필요, 작성 시 자동 생성(무시됨)
    private Integer userSeq;      // 컨트롤러에서 주입 (익명일 경우 NULL)
    
    private String title;
    private String body;
    private String summary;
    private String visible;   // '1' (공개) or '0' (익명)
    
    // 익명 게시글용 스냅샷 정보
    private String authorName;
    private String authorTier;
}