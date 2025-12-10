package com.ssaverytime.server.controller;

import com.ssaverytime.server.domain.dto.auth.BojValidateResponseDto;
import com.ssaverytime.server.domain.dto.mypage.MyPageResponseDto;
import com.ssaverytime.server.domain.dto.mypage.MyPageUpdateRequestDto;
import com.ssaverytime.server.service.MyPageService;
import com.ssaverytime.server.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    /**
     *  GET /api/v1/mypage
     *  현재 로그인한 사용자의 회원정보 불러오기
     * @return
     */
    @GetMapping
    public ResponseEntity<MyPageResponseDto> getMyPage() {
        String loginBojId= AuthUtil.getLoginUserId();
        System.out.println(loginBojId);
        MyPageResponseDto dto= myPageService.getMyPage(loginBojId);
        return ResponseEntity.ok(dto);
    }

    /**
     * PUT /api/v1/mypage
     * 현재 로그인한 계정의 회원정보 수정 (password, name, season)
     */
    @PutMapping
    public ResponseEntity<Void> updateMyPage(@RequestBody MyPageUpdateRequestDto request) {
        String loginBojId= AuthUtil.getLoginUserId();
        myPageService.updateMyPage(loginBojId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * POST /api/v1/mypage
     * 회원 탈퇴 (VALID -> INVALID 변경)
     */
    @PostMapping
    public ResponseEntity<Void> withdraw() {
        String loginBojId= AuthUtil.getLoginUserId();
        myPageService.withdraw(loginBojId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * PUT /api/v1/mypage/boj
     * 현재 로그인한 계정의 백준 랭크(티어 이미지)를 solved.ac에서 새로 가져와 DB에 반영
     */
    @PutMapping("/boj")
    public ResponseEntity<?> refreshMyBojRank() {
        String bojId= AuthUtil.getLoginUserId();

        if(bojId==null){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        try{
            BojValidateResponseDto dto = myPageService.refreshBojRank(bojId);
            return ResponseEntity.ok(dto); // svgUrl 담아서 반환
        }catch(IllegalArgumentException e){
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }catch(RuntimeException e){
            // solved.ac 404 or 네트워크 문제 등
            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(e.getMessage());
        }
    }

}
