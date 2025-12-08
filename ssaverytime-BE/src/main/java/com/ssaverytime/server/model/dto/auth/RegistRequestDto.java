package com.ssaverytime.server.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegistRequestDto {
    private String boj_id;
    private String password;
    private String name;
    private int season;
}
