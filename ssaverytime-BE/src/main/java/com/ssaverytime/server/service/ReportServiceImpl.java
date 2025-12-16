package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.board.BoardResponseDto;
import com.ssaverytime.server.domain.dto.comment.CommentResponseDto;
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

        // 2. 본인 신고 방지 확인 및 대상 테이블의 신고 횟수 증가
        if (targetType == ReportTargetType.BOARD) {
            BoardResponseDto board = boardMapper.selectBoardDetail(targetId, null);
            if (board == null) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }
            if (board.getUserSeq() != null && board.getUserSeq().equals(userId)) {
                throw new IllegalArgumentException("본인의 게시글은 신고할 수 없습니다.");
            }
            boardMapper.increaseWarningCnt(targetId);
            
        } else if (targetType == ReportTargetType.COMMENT) {
            CommentResponseDto comment = commentMapper.selectCommentById(targetId);
            if (comment == null) {
                throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
            }
            if (comment.getUserSeq() != null && comment.getUserSeq().equals(userId)) {
                throw new IllegalArgumentException("본인의 댓글은 신고할 수 없습니다.");
            }
            commentMapper.increaseWarningCnt(targetId);
        }

        // 3. 신고 이력 저장
        Report report = Report.builder()
                .userId(userId)
                .targetType(targetType)
                .targetId(targetId)
                .reason(reason)
                .build();
        
        reportMapper.insertReport(report);
    }
}
