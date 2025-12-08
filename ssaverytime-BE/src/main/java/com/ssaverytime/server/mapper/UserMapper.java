package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT BAEKJOON FROM USER WHERE USER_ID = #{userSeq}")
    String getUserTier(int userSeq);

    int countByBojId(String bojId);

    void insertUser(User user);

    User findByBojId(@Param("bojId")  String bojId);
}
