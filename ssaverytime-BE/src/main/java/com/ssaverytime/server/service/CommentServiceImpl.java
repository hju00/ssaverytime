package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.comment.CommentRequestDto;
import com.ssaverytime.server.domain.dto.comment.CommentResponseDto;
import com.ssaverytime.server.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public List<CommentResponseDto> getCommentList(int boardId, Integer userSeq) {
        return commentMapper.selectCommentList(boardId, userSeq);
    }

    @Override
    @Transactional
    public int writeComment(CommentRequestDto commentRequestDto) {
        return commentMapper.insertComment(commentRequestDto);
    }

    @Override
    @Transactional
    public int modifyComment(CommentRequestDto commentRequestDto, Integer userSeq) {
        // 권한 체크
        CommentResponseDto comment = commentMapper.selectCommentById(commentRequestDto.getCommentId());
        if (comment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }
        if (comment.getUserSeq() == null || !comment.getUserSeq().equals(userSeq)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }
        
        return commentMapper.updateComment(commentRequestDto);
    }

    @Override
    @Transactional
    public int removeComment(int commentId, Integer userSeq) {
        // 권한 체크
        CommentResponseDto comment = commentMapper.selectCommentById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }
        if (comment.getUserSeq() == null || !comment.getUserSeq().equals(userSeq)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        
        return commentMapper.deleteComment(commentId);
    }
}
