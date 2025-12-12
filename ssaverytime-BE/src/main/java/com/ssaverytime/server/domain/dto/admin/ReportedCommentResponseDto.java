package com.ssaverytime.server.domain.dto.admin;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportedCommentResponseDto {
    private int commentId;
    private int boardId;     // 어느 글의 댓글인지
    private String body;
    private String bojId;    // 작성자 ID
    private String userName; // 작성자 이름
    private int warningCnt;  // 신고 횟수
    private String visible;  // 현재 상태
    private LocalDateTime createdAt;
}
