package com.ssaverytime.server.domain.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String bojId;
    private String password;
}
