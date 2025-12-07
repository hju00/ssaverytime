package com.ssaverytime.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT BAEKJOON FROM USER WHERE USER_ID = #{userSeq}")
    String getUserTier(int userSeq);
}
