package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.comment.CommentRequestDto;
import com.ssaverytime.server.domain.dto.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    List<CommentResponseDto> getCommentList(int boardId, Integer userSeq);
    int writeComment(CommentRequestDto commentRequestDto);
    int modifyComment(CommentRequestDto commentRequestDto, Integer userSeq);
    int removeComment(int commentId, Integer userSeq);
}
