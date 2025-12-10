package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.dto.board.BoardRequestDto;
import com.ssaverytime.server.domain.dto.board.BoardResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    
    // 게시글 목록 조회 (검색, 정렬, 페이징 포함)
    List<BoardResponseDto> selectBoardList(@Param("keyword") String keyword,
                                           @Param("sort") String sort,
                                           @Param("limit") int limit,
                                           @Param("offset") int offset,
                                           @Param("currentUserSeq") Integer currentUserSeq); // 로그인한 경우 좋아요/스크랩 여부 확인용

    // 게시글 상세 조회
    BoardResponseDto selectBoardDetail(@Param("boardId") int boardId, @Param("currentUserSeq") Integer currentUserSeq);

    // 게시글 작성
    int insertBoard(BoardRequestDto boardRequestDto);

    // 게시글 수정
    int updateBoard(BoardRequestDto boardRequestDto);

    // 게시글 삭제 (실제 삭제 대신 숨김 처리 시 update 사용 가능, 여기선 삭제로 가정하거나 숨김으로 구현)
    int deleteBoard(int boardId);
    
    // 게시글 숨김 처리 (Soft Delete)
    int updateBoardVisibility(@Param("boardId") int boardId, @Param("visible") String visible);

    // 좋아요 관련
    int insertLike(@Param("boardId") int boardId, @Param("userSeq") int userSeq);
    int deleteLike(@Param("boardId") int boardId, @Param("userSeq") int userSeq);
    int checkLike(@Param("boardId") int boardId, @Param("userSeq") int userSeq);

    // 스크랩 관련
    int insertScrap(@Param("boardId") int boardId, @Param("userSeq") int userSeq);
    int deleteScrap(@Param("boardId") int boardId, @Param("userSeq") int userSeq);
    int checkScrap(@Param("boardId") int boardId, @Param("userSeq") int userSeq);

    // 신고 누적
    void increaseWarningCnt(int boardId);
}
