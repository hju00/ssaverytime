package com.ssaverytime.server.controller;

import com.ssaverytime.server.domain.dto.auth.*;
import com.ssaverytime.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/v1/auth/regist
     * 회원가입
     * @param request
     * @return
     */
    @PostMapping("/regist")
    public ResponseEntity<?> regist(@RequestBody RegistRequestDto request) {
        try{
            authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * GET /api/v1/auth/boj/validate
     * 백준 계정 존재 여부 확인 및 티어 가져오기
     * @return
     */
    @GetMapping("/boj/validate")
    public ResponseEntity<?> validateBoj(@RequestParam("bojId") String bojId) {

        if (bojId == null || bojId.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("bojId는 필수입니다.");
        }

        try{
            String encodedId= URLEncoder.encode(bojId, StandardCharsets.UTF_8);
            String url= "https://solved.ac/profile/" + encodedId;

            RestTemplate restTemplate= new RestTemplate();
            
            // User-Agent 헤더 추가 (봇 차단 방지)
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String html = response.getBody();

            // solved.ac에서 추출
            Pattern pattern= Pattern.compile("https://static\\.solved\\.ac/tier_small/\\d+\\.svg");
            Matcher matcher= pattern.matcher(html);

            if(matcher.find()){
                String svgUrl= matcher.group();
                return ResponseEntity.ok(new BojValidateResponseDto(svgUrl));

            }else{
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("존재하지 않는 BOJ 아이디입니다.");
            }
        } catch (RestClientException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body("solved.ac 호출 중 오류가 발생했습니다.");
        }
    }

    /**
     * POST /api/v1/auth/login
     * 로그인
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try{
            LoginResponseDto response= authService.login(request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
