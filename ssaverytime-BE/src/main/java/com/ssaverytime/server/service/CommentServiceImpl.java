package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.board.BoardResponseDto;
import com.ssaverytime.server.domain.dto.comment.CommentRequestDto;
import com.ssaverytime.server.domain.dto.comment.CommentResponseDto;
import com.ssaverytime.server.mapper.AnonymousBoardMapper;
import com.ssaverytime.server.mapper.AnonymousCommentMapper;
import com.ssaverytime.server.mapper.BoardMapper;
import com.ssaverytime.server.mapper.CommentMapper;
import com.ssaverytime.server.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final AnonymousCommentMapper anonymousCommentMapper;
    private final BoardMapper boardMapper;
    private final AnonymousBoardMapper anonymousBoardMapper;

    @Override
    public List<CommentResponseDto> getCommentList(int boardId, Integer userSeq) {
        List<CommentResponseDto> list = commentMapper.selectCommentList(boardId, userSeq);
        BoardResponseDto board = boardMapper.selectBoardDetail(boardId, null);
        String boardAuthorHash = null;

        if (board != null && "0".equals(board.getVisible())) {
            boardAuthorHash = anonymousBoardMapper.getAuthorHash(boardId);
        }

        Map<String, Integer> anonymousMap = new HashMap<>();
        int anonymousCounter = 1;

        for (CommentResponseDto comment : list) {
            // 1. 내 댓글인지 확인 (기존 로직 + 해시 체크)
            if (userSeq != null) {
                if ("0".equals(comment.getVisible())) {
                    // 해시는 이제 boardId 기반
                    String myHash = SecurityUtil.generateAuthorHash(boardId, userSeq);
                    // DB에 저장된 해시와 내가 계산한 해시가 같은지 확인 (작성자 본인 확인)
                    // 주의: comment.authorHash는 DB에서 가져온 값. myHash는 내 정보로 만든 값.
                    if (myHash.equals(comment.getAuthorHash())) {
                        comment.setAuthor(true);
                    }
                }
            }

            // 2. 익명 닉네임 처리 ("익명1", "작성자")
            if ("0".equals(comment.getVisible()) && comment.getAuthorHash() != null) {
                String hash = comment.getAuthorHash();
                
                // 작성자 확인
                boolean isPostAuthor = false;
                if (board != null) {
                    if ("0".equals(board.getVisible())) {
                        // 익명 게시글: 해시 비교
                        if (hash.equals(boardAuthorHash)) isPostAuthor = true;
                    } else {
                        // 실명 게시글: userSeq 비교 (익명 댓글이라 userSeq는 null이지만 해시로 추적 불가)
                        // 실명 게시글에 익명 댓글을 달면 -> 작성자인지 알 수 있나?
                        // 작성자라면 boardId + userSeq 해시가 생성됨.
                        // 실명 게시글 작성자의 해시를 만들어보면 됨.
                        String realBoardAuthorHash = SecurityUtil.generateAuthorHash(boardId, board.getUserSeq());
                        if (hash.equals(realBoardAuthorHash)) isPostAuthor = true;
                    }
                }

                if (isPostAuthor) {
                    comment.setUserName("작성자");
                    comment.setUserTier("Unrated"); // 작성자 표시는 티어 안 보여주는 게 일반적 (또는 본인 티어?)
                } else {
                    if (!anonymousMap.containsKey(hash)) {
                        anonymousMap.put(hash, anonymousCounter++);
                    }
                    comment.setUserName("익명" + anonymousMap.get(hash));
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
                // 소유권 해시 저장 (boardId 기반으로 변경)
                String hash = SecurityUtil.generateAuthorHash(commentRequestDto.getBoardId(), realUserSeq);
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
            // 익명 댓글 권한 체크 (해시: boardId 기반)
            // commentRequestDto에는 boardId가 없을 수도 있음 -> DB에서 가져온 comment의 boardId 사용
            String hash = SecurityUtil.generateAuthorHash(comment.getBoardId(), userSeq);
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
            // 익명 댓글 권한 체크 (해시: boardId 기반)
            String hash = SecurityUtil.generateAuthorHash(comment.getBoardId(), userSeq);
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
