package com.ssaverytime.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface DietCalorieMapper {

    // STAR + MENU 조인해서 식당쪽 칼로리 합산
    Integer sumRestaurantCalorie(
            @Param("userId") int userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
