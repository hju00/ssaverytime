package com.ssaverytime.server.service;

import com.ssaverytime.server.mapper.BoardMapper;
import com.ssaverytime.server.domain.dto.board.BoardRequestDto;
import com.ssaverytime.server.domain.dto.board.BoardResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private com.ssaverytime.server.mapper.AnonymousBoardMapper anonymousBoardMapper;
    
    @Autowired
    private com.ssaverytime.server.mapper.UserMapper userMapper;

    @Autowired
    private AiService aiService;

    @Override
    public List<BoardResponseDto> getBoardList(String keyword, String sort, int page, int size, Integer userSeq) {
        int offset = (page - 1) * size;
        return boardMapper.selectBoardList(keyword, sort, size, offset, userSeq);
    }

    @Override
    public BoardResponseDto getBoardDetail(int boardId, Integer userSeq) {
        BoardResponseDto board = boardMapper.selectBoardDetail(boardId, userSeq);
        
        if (board != null && userSeq != null) {
            if ("0".equals(board.getVisible())) {
                // 익명 게시글: 해시값 비교
                String currentHash = com.ssaverytime.server.util.SecurityUtil.generateAuthorHash(boardId, userSeq);
                int count = anonymousBoardMapper.checkAnonymousAuthorship(boardId, currentHash);
                board.setAuthor(count > 0);
            } else {
                // 일반 게시글: UserSeq 직접 비교
                board.setAuthor(board.getUserSeq() != null && board.getUserSeq().equals(userSeq));
            }
        }
        
        return board;
    }

    @Override
    @Transactional
    public int writeBoard(BoardRequestDto boardRequestDto) {
        // Summary 자동 생성 로직 제거
        boardRequestDto.setSummary(null);
        
        Integer realUserSeq = boardRequestDto.getUserSeq();
        
        if ("0".equals(boardRequestDto.getVisible())) {
            // 익명 게시글 처리 (스냅샷 저장)
            String userTier = userMapper.getUserTier(realUserSeq);
            if (userTier == null) userTier = "Unrated";
            
            boardRequestDto.setAuthorTier(userTier);
            boardRequestDto.setUserSeq(null); // DB에 NULL 저장
            
            int result = boardMapper.insertBoard(boardRequestDto);
            
            if (result > 0) {
                // 소유권 해시 저장 (게시글 ID + 실제 UserID)
                String hash = com.ssaverytime.server.util.SecurityUtil.generateAuthorHash(boardRequestDto.getBoardId(), realUserSeq);
                anonymousBoardMapper.insertAnonymousAuthorship(boardRequestDto.getBoardId(), hash);
            }
            return result;
        } else {
            // 일반 게시글 처리
            return boardMapper.insertBoard(boardRequestDto);
        }
    }

    @Override
    public int modifyBoard(BoardRequestDto boardRequestDto) {
        // Summary 자동 업데이트 로직 제거
        boardRequestDto.setSummary(null);
        return boardMapper.updateBoard(boardRequestDto);
    }

    @Override
    public int removeBoard(int boardId, int userSeq) {
        return boardMapper.deleteBoard(boardId);
    }

    @Transactional
    @Override
    public boolean toggleLike(int boardId, int userSeq) {
        int count = boardMapper.checkLike(boardId, userSeq);
        if (count > 0) {
            boardMapper.deleteLike(boardId, userSeq);
            return false; // 좋아요 취소됨
        } else {
            boardMapper.insertLike(boardId, userSeq);
            return true; // 좋아요 됨
        }
    }

    @Transactional
    @Override
    public boolean toggleScrap(int boardId, int userSeq) {
        int count = boardMapper.checkScrap(boardId, userSeq);
        if (count > 0) {
            boardMapper.deleteScrap(boardId, userSeq);
            return false; // 스크랩 취소됨
        } else {
            boardMapper.insertScrap(boardId, userSeq);
            return true; // 스크랩 됨
        }
    }

    @Override
    @Transactional
    public String getBoardSummary(int boardId) {
        BoardResponseDto board = boardMapper.selectBoardDetail(boardId, null);
        if (board == null) throw new IllegalArgumentException("게시글이 존재하지 않습니다.");

        if (board.getSummary() != null && !board.getSummary().isEmpty()) {
             return board.getSummary();
        }
        
        if (board.getBody() == null || board.getBody().length() < 200) {
            return "본문이 너무 짧아 요약하지 않습니다.";
        }

        // FastAPI 호출
        String summary = aiService.getSummary(board.getBody());

        // DB 저장
        boardMapper.updateSummary(boardId, summary);
        
        return summary;
    }

    @Override
    public List<BoardResponseDto> getScrapBoardList(int userSeq, int page, int size) {
        int offset = (page - 1) * size;
        return boardMapper.selectScrapBoardList(userSeq, size, offset);
    }
}