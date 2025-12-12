package com.ssaverytime.server.controller;

import com.ssaverytime.server.domain.dto.admin.ReportedBoardResponseDto;
import com.ssaverytime.server.domain.dto.admin.ReportedCommentResponseDto;
import com.ssaverytime.server.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 신고 게시글 조회
    @GetMapping("/board")
    public ResponseEntity<List<ReportedBoardResponseDto>> getReportedBoards() {
        List<ReportedBoardResponseDto> list = adminService.getReportedBoards();
        return ResponseEntity.ok(list);
    }

    // 신고 댓글 조회
    @GetMapping("/comment")
    public ResponseEntity<List<ReportedCommentResponseDto>> getReportedComments() {
        List<ReportedCommentResponseDto> list = adminService.getReportedComments();
        return ResponseEntity.ok(list);
    }

    // 특정 사용자 비활성화
    @PutMapping("/user/{userId}")
    public ResponseEntity<String> deactivateUser(@PathVariable int userId) {
        try {
            adminService.deactivateUser(userId);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }
    }

    // 게시글 비활성화
    @PutMapping("/board/{boardId}")
    public ResponseEntity<String> deactivateBoard(@PathVariable int boardId) {
        try {
            adminService.deactivateBoard(boardId);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }
    }

    // 댓글 비활성화
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<String> deactivateComment(@PathVariable int commentId) {
        try {
            adminService.deactivateComment(commentId);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }
    }
}
