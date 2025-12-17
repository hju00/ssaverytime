package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.dto.diet.DailyDietItemResponseDto;
import com.ssaverytime.server.domain.model.PersonalDiet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PersonalDietMapper {

    void insertPersonalDiet(PersonalDiet personalDiet);

    // 특정 유저가 특정 날짜 구간에 먹은 개인 섭취 칼로리 합
    Integer sumPersonalCalorie(@Param("userId") int userId,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    List<DailyDietItemResponseDto> findPersonalDietList(
            @Param("userId") Integer userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
