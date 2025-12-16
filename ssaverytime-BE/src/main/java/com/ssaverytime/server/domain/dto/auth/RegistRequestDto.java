package com.ssaverytime.server.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegistRequestDto {
    private String bojId;
    private String password;
    private String name;
    private Integer season;
    private String campus;
    private String baekjoon;
}
