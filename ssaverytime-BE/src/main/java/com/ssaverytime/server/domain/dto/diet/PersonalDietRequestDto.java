package com.ssaverytime.server.domain.dto.diet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalDietRequestDto {
    private String menu;
    private Integer calorie;
}
