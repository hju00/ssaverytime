package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.auth.LoginRequestDto;
import com.ssaverytime.server.domain.dto.auth.LoginResponseDto;
import com.ssaverytime.server.domain.dto.auth.RegistRequestDto;
import com.ssaverytime.server.domain.enums.user.UserRole;
import com.ssaverytime.server.domain.enums.user.UserValid;
import com.ssaverytime.server.domain.model.User;
import com.ssaverytime.server.mapper.UserMapper;
import com.ssaverytime.server.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void register(RegistRequestDto dto) {

        // 아이디 중복 확인
        int cnt= userMapper.countByBojId(dto.getBojId());
        if(cnt>0) {
            throw new RuntimeException();
        }

        // User 생성
        User user= new User();
        user.setBojId(dto.getBojId());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setSeason(dto.getSeason());
        user.setCampus(dto.getCampus());
        user.setRole(UserRole.USER); // 기본 설정
        user.setBaekjoon(dto.getBaekjoon());
        user.setValid(UserValid.VALID);

        // 저장
        userMapper.insertUser(user);
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        User user= userMapper.findByBojId(dto.getBojId());
        if(user==null){
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        if(user.getValid().equals(UserValid.INVALID)){
            throw new RuntimeException("탈퇴된 계정입니다.");
        }

        String accessToken= jwtUtil.generateAccessToken(user.getBojId(), user.getRole());

        return new LoginResponseDto(accessToken);
    }
}
