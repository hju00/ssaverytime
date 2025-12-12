package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.admin.ReportedBoardResponseDto;
import com.ssaverytime.server.domain.dto.admin.ReportedCommentResponseDto;
import com.ssaverytime.server.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public List<ReportedBoardResponseDto> getReportedBoards() {
        return adminMapper.selectReportedBoards();
    }

    @Override
    public List<ReportedCommentResponseDto> getReportedComments() {
        return adminMapper.selectReportedComments();
    }

    @Override
    @Transactional
    public void deactivateUser(int userId) {
        adminMapper.deactivateUser(userId);
    }

    @Override
    @Transactional
    public void deactivateBoard(int boardId) {
        adminMapper.deactivateBoard(boardId);
    }

    @Override
    @Transactional
    public void deactivateComment(int commentId) {
        adminMapper.deactivateComment(commentId);
    }
}
