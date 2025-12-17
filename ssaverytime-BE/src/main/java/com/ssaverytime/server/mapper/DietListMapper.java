package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.dto.diet.DailyDietItemResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DietListMapper {

    List<DailyDietItemResponseDto> findRestaurantDietByUserAndDate(
            @Param("userId") Integer userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
