package com.ssaverytime.server.domain.dto.diet;

import com.ssaverytime.server.domain.enums.star.StarCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StarCategoryRequestDto {
    private StarCategory category;  // TASTE or AMOUNT
}
