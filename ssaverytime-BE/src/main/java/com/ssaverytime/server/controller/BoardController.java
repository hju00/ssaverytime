package com.ssaverytime.server.controller;

import com.ssaverytime.server.domain.dto.board.BoardRequestDto;
import com.ssaverytime.server.domain.dto.board.BoardResponseDto;
import com.ssaverytime.server.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@CrossOrigin(origins = "*") // 프론트엔드 개발 편의를 위해 CORS 허용
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 목록 조회
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getBoardList(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // TODO: SecurityContextHolder에서 현재 로그인한 사용자 ID 가져오기
        Integer currentUserSeq = 1; // 테스트용 (User ID 1번)

        List<BoardResponseDto> list = boardService.getBoardList(keyword, sort, page, size, currentUserSeq);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardDetail(@PathVariable int boardId) {
        // TODO: 실제 사용자 ID
        Integer currentUserSeq = 1;
        
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
        // TODO: 로그인한 사용자 정보 세팅
        boardRequestDto.setUserSeq(1);

        int result = boardService.writeBoard(boardRequestDto);
        if (result > 0) {
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 글 수정
    @PutMapping
    public ResponseEntity<String> modifyBoard(@RequestBody BoardRequestDto boardRequestDto) {
        // 본인 확인 로직 필요
        boardRequestDto.setUserSeq(1); // 테스트용

        int result = boardService.modifyBoard(boardRequestDto);
        if (result > 0) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable int boardId) {
        int userSeq = 1; // 테스트용
        int result = boardService.removeBoard(boardId, userSeq);
        if (result > 0) {
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 좋아요 토글
    @PostMapping("/{boardId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable int boardId) {
        int userSeq = 1; // 테스트용
        boolean isLiked = boardService.toggleLike(boardId, userSeq);
        
        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLiked);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 스크랩 토글
    @PostMapping("/{boardId}/scrap")
    public ResponseEntity<Map<String, Object>> toggleScrap(@PathVariable int boardId) {
        int userSeq = 1; // 테스트용
        boolean isScrapped = boardService.toggleScrap(boardId, userSeq);

        Map<String, Object> response = new HashMap<>();
        response.put("scrapped", isScrapped);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
