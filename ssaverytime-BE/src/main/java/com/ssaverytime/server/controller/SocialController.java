package com.ssaverytime.server.controller;

import com.ssaverytime.server.domain.dto.social.SocialSearchByBojIdRequestDto;
import com.ssaverytime.server.domain.dto.social.SocialSearchByNameRequestDto;
import com.ssaverytime.server.domain.dto.social.SocialUserResponseDto;
import com.ssaverytime.server.service.SocialService;
import com.ssaverytime.server.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;

    /**
     * GET /api/v1/social/bojId
     * 사용자 검색 (백준 아이디 활용)
     * @param request
     * @return
     */
    @GetMapping("/bojId")
    public ResponseEntity<?> getByBojId(@RequestBody SocialSearchByBojIdRequestDto request) {
        try{
            SocialUserResponseDto dto= socialService.findByBojId(request.getBojId());
            return ResponseEntity.ok(dto);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * GET /api/v1/social/name
     * 사용자 검색 (사용자 이름 활용)
     * @param request
     * @return
     */
    @GetMapping("/name")
    public ResponseEntity<?> getByName(@RequestBody SocialSearchByNameRequestDto request) {
        try{
            List<SocialUserResponseDto> list= socialService.findByName(request.getName());
            return ResponseEntity.ok(list);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * POST /api/v1/social/follow/{USER_ID}
     * USERID를 팔로우
     * @param targetBojId
     * @return
     */
    @PostMapping("/follow/{bojId}")
    public ResponseEntity<?> follow(@PathVariable("bojId") String targetBojId) {
        String myBojId= AuthUtil.getLoginUserId();

        try{
            socialService.follow(myBojId, targetBojId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * DELETE /api/v1/social/follow/{USER_ID}
     * USER_ID를 언팔로우
     * @param targetBojId
     * @return
     */
    @DeleteMapping("/follow/{bojId}")
    public ResponseEntity<?> unfollow(@PathVariable("bojId") String targetBojId) {
        String myBojId= AuthUtil.getLoginUserId();
        if(myBojId==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try{
            socialService.unfollow(myBojId, targetBojId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /**
     * 내가 팔로우한 사람 목록
     */
    @GetMapping("/follow/to")
    public ResponseEntity<?> getFollowingList() {

        String myBojId = AuthUtil.getLoginUserId();

        List<SocialUserResponseDto> list =
                socialService.getFollowingList(myBojId);

        return ResponseEntity.ok(list);
    }

    /**
     * 나를 팔로우한 사람 목록
     */
    @GetMapping("/follow/from")
    public ResponseEntity<?> getFollowerList() {

        String myBojId = AuthUtil.getLoginUserId();

        List<SocialUserResponseDto> list =
                socialService.getFollowerList(myBojId);

        return ResponseEntity.ok(list);
    }
}
