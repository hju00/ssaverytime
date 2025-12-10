package com.ssaverytime.server.domain.dto.diet;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MenuCreateRequestDto {

    private Integer restaurantId;
    private String menu;

    // "2025-11-07 12:00:00" 형식
    @JsonFormat(pattern= "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
}
