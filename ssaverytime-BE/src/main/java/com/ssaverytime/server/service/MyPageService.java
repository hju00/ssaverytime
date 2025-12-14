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

    // 마이페이지 조회
    public MyPageResponseDto getMyPage(String loginBojId) {
        User user= userMapper.findByBojId(loginBojId);
        if(user==null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        return new MyPageResponseDto(
                user.getBojId(),
                user.getName(),
                user.getSeason(),
                user.getBaekjoon(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }

    // 마이페이지 수정 (password, name, season)
    public void updateMyPage(String loginBojId, MyPageUpdateRequestDto request) {
        User user= userMapper.findByBojId(loginBojId);
        if(user==null){
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

    // 회원 탈퇴 (VALID -> INVALID)
    public void withdraw(String loginBojId) {
        userMapper.updateValid(loginBojId, UserValid.INVALID.name());
    }

    // 백준 티어 갱신
    public BojValidateResponseDto refreshBojRank(String bojId) {

        if(bojId==null || bojId.isBlank()) {
            throw new IllegalArgumentException("bojId는 필수입니다.");
        }

        try{
            String encodedId= URLEncoder.encode(bojId, StandardCharsets.UTF_8);
            String url= "https://solved.ac/profile/" + encodedId;

            RestTemplate restTemplate= new RestTemplate();
            String html= restTemplate.getForObject(url, String.class);

            // solved.ac HTML 안에서 tier_small svg 주소만 뽑기
            // 예: https://static.solved.ac/tier_small/11.svg
            Pattern pattern = Pattern.compile("https://static\\.solved\\.ac/tier_small/\\d+\\.svg");
            Matcher matcher = pattern.matcher(html);

            if(!matcher.find()) {
                // 프로필은 있는데 tier 이미지 못 찾은 케이스 or 존재하지 않는 아이디일 가능성
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "존재하지 않는 BOJ 아이디입니다.");
            }

            String svgUrl= matcher.group();

            userMapper.updateBaekjoon(bojId, svgUrl);

            return new BojValidateResponseDto(svgUrl);

        } catch (HttpClientErrorException e) {
            // solved.ac 에서 404 등
            throw new RuntimeException(e.getStatusText());
        } catch (RestClientException e) {
            // 네트워크 오류 등
            throw new RuntimeException("solved.ac 호출 중 오류가 발생했습니다.");
        }
    }
}
