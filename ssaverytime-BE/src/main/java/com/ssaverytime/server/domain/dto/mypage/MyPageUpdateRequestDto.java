package com.ssaverytime.server.domain.dto.mypage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageUpdateRequestDto {
    private String password;
    private String name;
    private Integer season;
}
