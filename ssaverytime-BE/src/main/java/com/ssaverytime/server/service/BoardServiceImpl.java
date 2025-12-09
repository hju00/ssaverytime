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
        // Summary 생성 로직
        if (boardRequestDto.getBody() != null && boardRequestDto.getBody().length() > 100) {
            boardRequestDto.setSummary(boardRequestDto.getBody().substring(0, 100) + "...");
        } else {
            boardRequestDto.setSummary(boardRequestDto.getBody());
        }
        
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
        if (boardRequestDto.getBody() != null && boardRequestDto.getBody().length() > 100) {
            boardRequestDto.setSummary(boardRequestDto.getBody().substring(0, 100) + "...");
        } else {
            boardRequestDto.setSummary(boardRequestDto.getBody());
        }
        return boardMapper.updateBoard(boardRequestDto);
    }

    @Override
    public int removeBoard(int boardId, int userSeq) {
        // 실제 삭제가 아닌 숨김 처리로 구현 (데이터 보존) -> Soft Delete (VALID='0')
        // 본인 확인 로직은 Controller나 Security 레벨에서 수행된다고 가정
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
}
