package com.ssaverytime.server.service;

import com.ssaverytime.server.mapper.BoardMapper;
import com.ssaverytime.server.model.board.BoardResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<BoardResponseDto> getBoardList(String keyword, String sort, int page, int size, Integer userSeq) {
        int offset = (page - 1) * size;
        return boardMapper.selectBoardList(keyword, sort, size, offset, userSeq);
    }

    @Override
    public BoardResponseDto getBoardDetail(int boardId, Integer userSeq) {
        return boardMapper.selectBoardDetail(boardId, userSeq);
    }

    @Override
    public int writeBoard(BoardResponseDto boardResponseDto) {
        // Summary 생성 로직 (본문 앞부분 잘라서 저장)
        if (boardResponseDto.getBody() != null && boardResponseDto.getBody().length() > 100) {
            boardResponseDto.setSummary(boardResponseDto.getBody().substring(0, 100) + "...");
        } else {
            boardResponseDto.setSummary(boardResponseDto.getBody());
        }
        return boardMapper.insertBoard(boardResponseDto);
    }

    @Override
    public int modifyBoard(BoardResponseDto boardResponseDto) {
        if (boardResponseDto.getBody() != null && boardResponseDto.getBody().length() > 100) {
            boardResponseDto.setSummary(boardResponseDto.getBody().substring(0, 100) + "...");
        } else {
            boardResponseDto.setSummary(boardResponseDto.getBody());
        }
        return boardMapper.updateBoard(boardResponseDto);
    }

    @Override
    public int removeBoard(int boardId, int userSeq) {
        // 실제 삭제가 아닌 숨김 처리로 구현 (데이터 보존)
        // 본인 확인 로직은 Controller나 Security 레벨에서 수행된다고 가정
        return boardMapper.updateBoardVisibility(boardId, "0");
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
