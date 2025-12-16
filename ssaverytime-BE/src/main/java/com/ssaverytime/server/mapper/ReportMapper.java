package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.enums.report.ReportTargetType;
import com.ssaverytime.server.domain.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReportMapper {
    int insertReport(Report report);
    int checkReportExists(@Param("userId") Integer userId, @Param("targetType") ReportTargetType targetType, @Param("targetId") int targetId);
}
