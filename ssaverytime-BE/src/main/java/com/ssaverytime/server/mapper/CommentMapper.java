package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.dto.comment.CommentRequestDto;
import com.ssaverytime.server.domain.dto.comment.CommentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    
    // 댓글 목록 조회
    List<CommentResponseDto> selectCommentList(@Param("boardId") int boardId, @Param("currentUserSeq") Integer currentUserSeq);
    
    // 댓글 상세 조회 (권한 체크용)
    CommentResponseDto selectCommentById(int commentId);

    // 댓글 작성
    int insertComment(CommentRequestDto commentRequestDto);

    // 댓글 수정
    int updateComment(CommentRequestDto commentRequestDto);

    // 댓글 삭제 (Soft Delete)
    int deleteComment(int commentId);

    // 신고 누적
    void increaseWarningCnt(int commentId);
}
