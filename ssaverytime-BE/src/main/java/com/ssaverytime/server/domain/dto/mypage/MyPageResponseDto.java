package com.ssaverytime.server.domain.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private String bojId;
    private String name;
    private Integer season;
    private String baekjoon;
    private String role;
    private LocalDateTime createdAt;
}
