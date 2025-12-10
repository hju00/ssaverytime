package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.enums.report.ReportTargetType;

public interface ReportService {
    void report(Integer userId, ReportTargetType targetType, int targetId, String reason);
}
