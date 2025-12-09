package com.ssaverytime.server.domain.model;

import com.ssaverytime.server.domain.enums.user.UserRole;
import com.ssaverytime.server.domain.enums.user.UserValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class User {
    private Long userId;
    private String bojId;
    private String password;
    private String name;
    private UserRole role;        // DB에는 'USER' / 'ADMIN' 문자열로 들어감
    private Integer season;
    private String baekjoon;      // 가입 단계에서는 null
    private UserValid valid;      // 논리 상태 (VALID/INVALID)
    private LocalDateTime createdAt;
}
