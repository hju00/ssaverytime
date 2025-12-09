package com.ssaverytime.server.mapper;

import com.ssaverytime.server.domain.model.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MenuMapper {

    // 메뉴 INSERT
    void insertMenu(Menu menu);

    // 날짜와 식당으로 메뉴 리스트 조회
    List<Menu> findByDateAndRestaurant(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("restaurantId") Integer restaurantId
    );
}
