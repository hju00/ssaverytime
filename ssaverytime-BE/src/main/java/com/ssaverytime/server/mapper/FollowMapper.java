package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    // 내가 팔로우한 사람
    List<User> findFollowing(@Param("myBojId") String myBojId);

    // 나를 팔로우한 사람
    List<User> findFollowers(@Param("myBojId") String myBojId);
}
