package com.ssaverytime.server.controller;

import com.ssaverytime.server.domain.dto.diet.DietResponseDto;
import com.ssaverytime.server.domain.dto.diet.MenuCreateRequestDto;
import com.ssaverytime.server.domain.dto.diet.StarCategoryRequestDto;
import com.ssaverytime.server.domain.dto.diet.StarRequestDto;
import com.ssaverytime.server.service.DietService;
import com.ssaverytime.server.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/diet")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    /**
     * POST /api/v1/dining/menu
     * 메뉴 등록
     * @param request
     * @return
     */
    @PostMapping("/menu")
    public ResponseEntity<?> createMenu(@RequestBody MenuCreateRequestDto request) {
        try{
            dietService.createMenu(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    /**
     * GET /api/v1/diet/{DATE}/{RESTAURANT_ID}
     * 특징 날짜, 특정 식당의 메뉴 전체 가져오기
     * @param date
     * @param restaurantId
     * @return
     */
    @GetMapping("/{date}/{restaurantId}")
    public ResponseEntity<?> getDiet(
            @PathVariable("date") String date,
            @PathVariable("restaurantId") Integer restaurantId) {

        try{
            List<DietResponseDto> list= dietService.getDiet(date, restaurantId);
            return ResponseEntity.ok(list);
        }catch(HttpClientErrorException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    /**
     * POST /api/v1/diet/{DATE}/star/{RESTAURANT_ID}
     * 별점 부여
     * @param date
     * @param restaurantId
     * @param request
     * @return
     */
    @PostMapping("/{date}/star/{restaurantId}")
    public ResponseEntity<?> rateDiet(
            @PathVariable("date") String date,
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestBody StarRequestDto request) {

        String loginBojId= AuthUtil.getLoginUserId();
        if(loginBojId==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try{
            dietService.rateDiet(loginBojId, date, restaurantId, request);
            // 새로 넣었든, 기존 걸 갱신했든 성공 의미만 전달
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    /**
     * GET /api/v1/diet/{DATE}/star/{RESTAURANT_ID}
     * 별점 평균 구하기
     * @param date
     * @param restaurantId
     * @param request
     * @return
     */
    @GetMapping("/{date}/star/{restaurantId}")
    public ResponseEntity<?> getAverageScore(
            @PathVariable("date") String date,
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestBody StarCategoryRequestDto request) {

        try{
            double avg= dietService.getAverageScore(date, restaurantId, request.getCategory());
            return ResponseEntity.ok(
                    Map.of("averageScore", avg)
            );
        }catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}
