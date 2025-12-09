package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.auth.BojValidateResponseDto;
import com.ssaverytime.server.domain.dto.mypage.MyPageResponseDto;
import com.ssaverytime.server.domain.dto.mypage.MyPageUpdateRequestDto;
import com.ssaverytime.server.domain.enums.user.UserValid;
import com.ssaverytime.server.domain.model.User;
import com.ssaverytime.server.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 1. 마이페이지 조회
    public MyPageResponseDto getMyPage(String loginBojId) {
        User user= userMapper.findByBojId(loginBojId);
        if(user==null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        return new MyPageResponseDto(
                user.getBojId(),
                user.getName(),
                user.getSeason(),
                user.getBaekjoon(),
                user.getCreatedAt()
        );
    }

    // 2. 마이페이지 수정 (password, name, season)
    public void updateMyPage(String loginBojId, MyPageUpdateRequestDto request) {
        User user= userMapper.findByBojId(loginBojId);
        if(user==null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        if(request.getPassword()!=null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if(request.getName()!=null) {
            user.setName(request.getName());
        }
        if(request.getSeason()!=null) {
            user.setSeason(request.getSeason());
        }

        userMapper.updateMyPage(user);
    }

    // 3. 회원 탈퇴 (VALID -> INVALID)
    public void withdraw(String loginBojId) {
        userMapper.updateValid(loginBojId, UserValid.INVALID.name());
    }
}
