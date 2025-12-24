package com.ssaverytime.server.domain.dto.admin;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportedBoardResponseDto {
    private int boardId;
    private String title;
    private String bojId;    // 작성자 ID
    private String userName; // 작성자 이름
    private int warningCnt;  // 신고 횟수
    private String visible;  // 익명 여부 ('1':실명, '0':익명)
    private String valid;    // 활성 여부 ('1':활성, '0':비활성/삭제)
    private LocalDateTime createdAt;
}
