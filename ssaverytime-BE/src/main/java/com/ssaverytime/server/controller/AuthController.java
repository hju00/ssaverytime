package com.ssaverytime.server.controller;

import com.ssaverytime.server.domain.dto.auth.LoginRequestDto;
import com.ssaverytime.server.domain.dto.auth.LoginResponseDto;
import com.ssaverytime.server.domain.dto.auth.RegistRequestDto;
import com.ssaverytime.server.service.AuthService;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService= authService;
    }

    @PostMapping("/regist")
    public ResponseEntity<?> regist(@RequestBody RegistRequestDto request) {
        try{
            authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            LoginResponseDto response= authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
