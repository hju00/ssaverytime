package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.comment.CommentRequestDto;
import com.ssaverytime.server.domain.dto.comment.CommentResponseDto;
import com.ssaverytime.server.mapper.AnonymousCommentMapper;
import com.ssaverytime.server.mapper.CommentMapper;
import com.ssaverytime.server.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final AnonymousCommentMapper anonymousCommentMapper;

    @Override
    public List<CommentResponseDto> getCommentList(int boardId, Integer userSeq) {
        List<CommentResponseDto> list = commentMapper.selectCommentList(boardId, userSeq);

        if (userSeq != null) {
            for (CommentResponseDto comment : list) {
                if ("0".equals(comment.getVisible())) { // 익명 댓글
                    String hash = SecurityUtil.generateAuthorHash(comment.getCommentId(), userSeq);
                    int count = anonymousCommentMapper.checkAnonymousAuthorship(comment.getCommentId(), hash);
                    if (count > 0) {
                        comment.setAuthor(true);
                    }
                }
            }
        }
        return list;
    }

    @Override
    @Transactional
    public int writeComment(CommentRequestDto commentRequestDto) {
        Integer realUserSeq = commentRequestDto.getUserSeq();

        if ("0".equals(commentRequestDto.getVisible())) {
            commentRequestDto.setUserSeq(null); // 익명 저장
            int result = commentMapper.insertComment(commentRequestDto);
            if (result > 0) {
                // 소유권 해시 저장
                String hash = SecurityUtil.generateAuthorHash(commentRequestDto.getCommentId(), realUserSeq);
                anonymousCommentMapper.insertAnonymousAuthorship(commentRequestDto.getCommentId(), hash);
            }
            return result;
        } else {
            return commentMapper.insertComment(commentRequestDto);
        }
    }

    @Override
    @Transactional
    public int modifyComment(CommentRequestDto commentRequestDto, Integer userSeq) {
        // 권한 체크
        CommentResponseDto comment = commentMapper.selectCommentById(commentRequestDto.getCommentId());
        if (comment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }

        if ("0".equals(comment.getVisible())) {
            // 익명 댓글 권한 체크
            String hash = SecurityUtil.generateAuthorHash(commentRequestDto.getCommentId(), userSeq);
            int count = anonymousCommentMapper.checkAnonymousAuthorship(commentRequestDto.getCommentId(), hash);
            if (count == 0) {
                throw new SecurityException("수정 권한이 없습니다.");
            }
        } else {
            // 실명 댓글 권한 체크
            if (comment.getUserSeq() == null || !comment.getUserSeq().equals(userSeq)) {
                throw new SecurityException("수정 권한이 없습니다.");
            }
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

        if ("0".equals(comment.getVisible())) {
            // 익명 댓글 권한 체크
            String hash = SecurityUtil.generateAuthorHash(commentId, userSeq);
            int count = anonymousCommentMapper.checkAnonymousAuthorship(commentId, hash);
            if (count == 0) {
                throw new SecurityException("삭제 권한이 없습니다.");
            }
        } else {
            // 실명 댓글 권한 체크
            if (comment.getUserSeq() == null || !comment.getUserSeq().equals(userSeq)) {
                throw new SecurityException("삭제 권한이 없습니다.");
            }
        }
        
        return commentMapper.deleteComment(commentId);
    }
}
