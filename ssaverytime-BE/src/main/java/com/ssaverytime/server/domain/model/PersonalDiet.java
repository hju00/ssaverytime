package com.ssaverytime.server.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PersonalDiet {
    private Integer dietId;
    private Integer userId;
    private String menu;
    private Integer calorie;
    private LocalDateTime date;
}
