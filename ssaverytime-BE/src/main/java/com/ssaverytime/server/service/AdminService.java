package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.admin.ReportedBoardResponseDto;
import com.ssaverytime.server.domain.dto.admin.ReportedCommentResponseDto;

import java.util.List;

public interface AdminService {
    List<ReportedBoardResponseDto> getReportedBoards();
    List<ReportedCommentResponseDto> getReportedComments();
    void deactivateUser(int userId);
    void deactivateBoard(int boardId);
    void deactivateComment(int commentId);
}
