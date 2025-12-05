package com.ssaverytime.server.service;

import com.ssaverytime.server.model.board.BoardRequestDto;
import com.ssaverytime.server.model.board.BoardResponseDto;
import java.util.List;

public interface BoardService {
    // 게시글 목록
    List<BoardResponseDto> getBoardList(String keyword, String sort, int page, int size, Integer userSeq);
    
    // 상세 조회
    BoardResponseDto getBoardDetail(int boardId, Integer userSeq);
    
    // 글 작성
    int writeBoard(BoardRequestDto boardRequestDto);
    
    // 글 수정
    int modifyBoard(BoardRequestDto boardRequestDto);
    
    // 글 삭제 (숨김 처리)
    int removeBoard(int boardId, int userSeq);
    
    // 좋아요 토글
    boolean toggleLike(int boardId, int userSeq);
    
    // 스크랩 토글
    boolean toggleScrap(int boardId, int userSeq);
}
