package com.ssaverytime.server.domain.model;

import com.ssaverytime.server.domain.enums.report.ReportTargetType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private Integer reportId;
    private Integer userId;
    private ReportTargetType targetType;
    private Integer targetId;
    private String reason;
    private LocalDateTime createdAt;
}
