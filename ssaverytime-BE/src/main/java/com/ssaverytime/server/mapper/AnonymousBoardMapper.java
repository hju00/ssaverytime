package com.ssaverytime.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnonymousBoardMapper {
    
    // 익명 소유권 등록
    void insertAnonymousAuthorship(@Param("boardId") int boardId, @Param("authorHash") String authorHash);

    // 익명 소유권 확인 (해시값 존재 여부 반환)
    int checkAnonymousAuthorship(@Param("boardId") int boardId, @Param("authorHash") String authorHash);
}
