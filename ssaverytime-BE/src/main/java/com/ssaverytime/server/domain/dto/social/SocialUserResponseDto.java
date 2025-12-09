package com.ssaverytime.server.domain.dto.social;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserResponseDto {
    private String baekjoon;  // 티어 이미지 URL
    private String bojId;
    private String name;
    private Integer season;
    private boolean withdrawn;  // true면 탈퇴한 사용자
}
