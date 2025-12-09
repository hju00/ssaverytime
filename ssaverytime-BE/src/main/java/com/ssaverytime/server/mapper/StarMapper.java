package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.model.Star;
import com.ssaverytime.server.domain.enums.star.StarCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface StarMapper {

    // 새 별점 INSERT
    void insertStar(Star star);

    // 기존 별점 UPDATE (같은 USER, RESTAURANT, CATEGORY, DATE)
    int updateStar(@Param("userId") int userId,
                   @Param("restaurantId") int restaurantId,
                   @Param("category") StarCategory category,
                   @Param("date") java.time.LocalDateTime date,
                   @Param("score") int score);

    // 평균 점수 조회
    Double findAverageScore(@Param("restaurantId") int restaurantId,
                            @Param("category") StarCategory category,
                            @Param("start") LocalDateTime start,
                            @Param("end") LocalDateTime end);
}
