package com.ssaverytime.server.domain.model;

import com.ssaverytime.server.domain.enums.star.StarCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Star {

    private Integer starId;
    private Integer userId;
    private Integer restaurantId;
    private StarCategory category;
    private Integer score;
    private LocalDateTime date;
}
