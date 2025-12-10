package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.enums.report.ReportTargetType;
import com.ssaverytime.server.domain.model.Report;
import com.ssaverytime.server.mapper.BoardMapper;
import com.ssaverytime.server.mapper.CommentMapper;
import com.ssaverytime.server.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public void report(Integer userId, ReportTargetType targetType, int targetId, String reason) {
        // 1. 중복 신고 확인
        int count = reportMapper.checkReportExists(userId, targetType, targetId);
        if (count > 0) {
            throw new IllegalArgumentException("이미 신고한 대상입니다.");
        }

        // 2. 신고 이력 저장
        Report report = Report.builder()
                .userId(userId)
                .targetType(targetType)
                .targetId(targetId)
                .reason(reason)
                .build();
        
        reportMapper.insertReport(report);

        // 3. 대상 테이블의 신고 횟수 증가
        if (targetType == ReportTargetType.BOARD) {
            boardMapper.increaseWarningCnt(targetId);
        } else if (targetType == ReportTargetType.COMMENT) {
            commentMapper.increaseWarningCnt(targetId);
        }
    }
}
