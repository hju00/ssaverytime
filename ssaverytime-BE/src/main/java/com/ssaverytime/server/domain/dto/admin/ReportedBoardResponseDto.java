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
    private String visible;  // 현재 상태 ('1' or '0')
    private LocalDateTime createdAt;
}
