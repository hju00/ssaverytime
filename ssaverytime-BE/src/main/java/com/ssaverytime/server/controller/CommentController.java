package com.ssaverytime.server.controller;

import com.ssaverytime.server.domain.dto.comment.CommentRequestDto;
import com.ssaverytime.server.domain.dto.comment.CommentResponseDto;
import com.ssaverytime.server.domain.model.User;
import com.ssaverytime.server.mapper.UserMapper;
import com.ssaverytime.server.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserMapper userMapper;

    private Integer getCurrentUserSeq() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        String bojId = (String) auth.getPrincipal();
        User user = userMapper.findByBojId(bojId);
        return user != null ? user.getUserId().intValue() : null;
    }

    // 댓글 목록 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentList(@PathVariable int boardId) {
        Integer currentUserSeq = getCurrentUserSeq();
        List<CommentResponseDto> list = commentService.getCommentList(boardId, currentUserSeq);
        return ResponseEntity.ok(list);
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<String> writeComment(@PathVariable int boardId, @RequestBody CommentRequestDto commentRequestDto) {
        Integer userSeq = getCurrentUserSeq();
        if (userSeq == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        commentRequestDto.setBoardId(boardId);
        commentRequestDto.setUserSeq(userSeq);

        int result = commentService.writeComment(commentRequestDto);
        return result > 0 ? ResponseEntity.status(HttpStatus.CREATED).body("success") : 
                            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<String> modifyComment(@PathVariable int boardId, @PathVariable int commentId, @RequestBody CommentRequestDto commentRequestDto) {
        Integer userSeq = getCurrentUserSeq();
        if (userSeq == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        commentRequestDto.setCommentId(commentId);
        
        try {
            int result = commentService.modifyComment(commentRequestDto, userSeq);
            return result > 0 ? ResponseEntity.ok("success") : 
                                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> removeComment(@PathVariable int boardId, @PathVariable int commentId) {
        Integer userSeq = getCurrentUserSeq();
        if (userSeq == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            int result = commentService.removeComment(commentId, userSeq);
            return result > 0 ? ResponseEntity.ok("success") : 
                                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
