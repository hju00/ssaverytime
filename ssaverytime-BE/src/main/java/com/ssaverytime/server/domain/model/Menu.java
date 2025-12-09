package com.ssaverytime.server.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Menu {

    private Integer menuId;        // AUTO_INCREMENT
    private Integer restaurantId;
    private String menu;
    private LocalDateTime date;
}
