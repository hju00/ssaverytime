package com.ssaverytime.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnonymousCommentMapper {
    void insertAnonymousAuthorship(@Param("commentId") int commentId, @Param("authorHash") String authorHash);
    int checkAnonymousAuthorship(@Param("commentId") int commentId, @Param("authorHash") String authorHash);
}
