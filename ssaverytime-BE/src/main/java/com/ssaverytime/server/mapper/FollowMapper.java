package com.ssaverytime.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowMapper {

    // 팔로우 추가 (한 방향)
    void insertFollow(@Param("bojId1") String bojId1,
                      @Param("bojId2") String bojId2);

    // 팔로우 삭제 (한 방향)
    void deleteFollow(@Param("bojId1") String bojId1,
                      @Param("bojId2") String bojId2);

    // 필로우 관계 확인
    int countFollow(@Param("bojId1") String bojId1,
                    @Param("bojId2") String bojId2);
}
