package com.ssaverytime.server.domain.dto.diet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyDietItemResponseDto {
    private String menu;
    private int calorie;
}
