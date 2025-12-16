package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.dto.admin.ReportedBoardResponseDto;
import com.ssaverytime.server.domain.dto.admin.ReportedCommentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    // 신고된 게시글 조회
    List<ReportedBoardResponseDto> selectReportedBoards();

    // 신고된 댓글 조회
    List<ReportedCommentResponseDto> selectReportedComments();

    // 사용자 비활성화 (VALID -> INVALID)
    int deactivateUser(@Param("userId") int userId);

    // 게시글 비활성화 (VISIBLE -> '0')
    int deactivateBoard(@Param("boardId") int boardId);

    // 댓글 비활성화 (VISIBLE -> '0')
    int deactivateComment(@Param("commentId") int commentId);
}
