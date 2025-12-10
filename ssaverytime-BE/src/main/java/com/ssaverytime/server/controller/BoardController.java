package com.ssaverytime.server.controller;

import com.ssaverytime.server.domain.dto.board.BoardRequestDto;
import com.ssaverytime.server.domain.dto.board.BoardResponseDto;
import com.ssaverytime.server.domain.model.User;
import com.ssaverytime.server.mapper.UserMapper;
import com.ssaverytime.server.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
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

    // 목록 조회
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getBoardList(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Integer currentUserSeq = getCurrentUserSeq();
        List<BoardResponseDto> list = boardService.getBoardList(keyword, sort, page, size, currentUserSeq);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardDetail(@PathVariable int boardId) {
        Integer currentUserSeq = getCurrentUserSeq();
        BoardResponseDto board = boardService.getBoardDetail(boardId, currentUserSeq);
        if (board != null) {
            return new ResponseEntity<>(board, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 글 작성
    @PostMapping
    public ResponseEntity<String> writeBoard(@RequestBody BoardRequestDto boardRequestDto) {
        Integer userSeq = getCurrentUserSeq();
        if (userSeq == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        boardRequestDto.setUserSeq(userSeq);

        int result = boardService.writeBoard(boardRequestDto);
        if (result > 0) {
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 글 수정
    @PutMapping
    public ResponseEntity<String> modifyBoard(@RequestBody BoardRequestDto boardRequestDto) {
        Integer userSeq = getCurrentUserSeq();
        if (userSeq == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        boardRequestDto.setUserSeq(userSeq);

        int result = boardService.modifyBoard(boardRequestDto);
        if (result > 0) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable int boardId) {
        Integer userSeq = getCurrentUserSeq();
        if (userSeq == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        int result = boardService.removeBoard(boardId, userSeq);
        if (result > 0) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 좋아요 토글
    @PostMapping("/{boardId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable int boardId) {
        Integer userSeq = getCurrentUserSeq();
        if (userSeq == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        boolean isLiked = boardService.toggleLike(boardId, userSeq);
        
        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLiked);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 스크랩 토글
    @PostMapping("/{boardId}/scrap")
    public ResponseEntity<Map<String, Object>> toggleScrap(@PathVariable int boardId) {
        Integer userSeq = getCurrentUserSeq();
        if (userSeq == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        boolean isScrapped = boardService.toggleScrap(boardId, userSeq);

        Map<String, Object> response = new HashMap<>();
        response.put("scrapped", isScrapped);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}