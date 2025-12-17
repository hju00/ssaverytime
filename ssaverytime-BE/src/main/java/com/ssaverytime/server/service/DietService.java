package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.diet.*;
import com.ssaverytime.server.domain.enums.star.StarCategory;
import com.ssaverytime.server.domain.model.Menu;
import com.ssaverytime.server.domain.model.PersonalDiet;
import com.ssaverytime.server.domain.model.Star;
import com.ssaverytime.server.domain.model.User;
import com.ssaverytime.server.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietService {

    private final MenuMapper menuMapper;
    private final UserMapper userMapper;
    private final StarMapper starMapper;
    private final PersonalDietMapper personalDietMapper;
    private final DietCalorieMapper dietCalorieMapper;
    private final DietListMapper dietListMapper;

    // 메뉴 등록
    public void createMenu(MenuCreateRequestDto dto) {

        // restaurantId 1~5만 허용 (안 맞으면 400)
        if(dto.getRestaurantId()==null
                || dto.getRestaurantId()<1
                || dto.getRestaurantId()>5){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 식당입니다.");
        }

        if(dto.getMenu()==null || dto.getMenu().isBlank()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "메뉴 이름은 필수입니다.");
        }

        Menu menu= new Menu();
        menu.setRestaurantId(dto.getRestaurantId());
        menu.setMenu(dto.getMenu());
        menu.setCalorie(dto.getCalorie());
        menu.setDate(dto.getDate()); // null이면 DB에서 NULL로 들어감

        menuMapper.insertMenu(menu);
    }

    // date + restaurantId
    public List<DietResponseDto> getDiet(String date, Integer restaurantId) {

        if(restaurantId==null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "restaurantId는 필수입니다.");
        }

        // 파싱
        LocalDate localDate;
        try{
            localDate= LocalDate.parse(date);
        }catch(Exception e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "DATE 형식이 올바르지 않습니다. (yyyy-MM-dd)");
        }

        LocalDateTime start= localDate.atStartOfDay();
        LocalDateTime end= localDate.atTime(23, 59, 59);

        List<Menu> menus= menuMapper.findByDateAndRestaurant(start, end, restaurantId);

        return menus.stream()
                .map(m -> new DietResponseDto(m.getMenu(), m.getCalorie()))
                .collect(Collectors.toList());
    }

    // 별점 부여
    public void rateDiet(String loginBojId,
                         String dateStr,
                         Integer restaurantId,
                         StarRequestDto request) {

        if(loginBojId==null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        // 로그인한 사용자 → USER_ID 조회
        User me= userMapper.findByBojId(loginBojId);
        if(me==null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        if(restaurantId==null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "RESTAURANT_ID는 필수입니다.");
        }

        if(request.getCategory()==null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "category는 필수입니다.");
        }

        // 점수 검증 (원하면 1~5 등으로 제한)
        if(request.getScore()<1 || request.getScore()>5) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "score는 1~5 사이여야 합니다.");
        }

        // DATE 파싱 (yyyy-MM-dd)
        LocalDate localDate;
        try{
            localDate= LocalDate.parse(dateStr); // 기본 패턴 yyyy-MM-dd
        }catch(Exception e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "DATE 형식이 올바르지 않습니다. (yyyy-MM-dd)");
        }

        // 해당 날짜의 기준 시간 (00:00:00 로 통일)
        LocalDateTime starDate= localDate.atStartOfDay();

        // 1) 먼저 UPDATE 시도 (이미 같은 날짜에 평점이 있다면 갱신)
        int updated= starMapper.updateStar(
                me.getUserId(),
                restaurantId,
                request.getCategory(),
                starDate,
                request.getScore()
        );

        if(updated==0){
            // 2) 기존 평점이 없으면 INSERT
            Star star= new Star();
            star.setUserId(me.getUserId());
            star.setRestaurantId(restaurantId);
            star.setCategory(request.getCategory());
            star.setScore(request.getScore());
            star.setDate(starDate);

            starMapper.insertStar(star);
        }
    }

    // 평균 계산
    public double getAverageScore(String dateStr,
                                  Integer restaurantId,
                                  StarCategory category) {

        if(restaurantId==null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "RESTAURANT_ID는 필수입니다.");
        }

        if(category==null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "category는 필수입니다.");
        }

        LocalDate localDate;
        try{
            localDate= LocalDate.parse(dateStr); // yyyy-MM-dd
        }catch(Exception e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "DATE 형식이 올바르지 않습니다. (yyyy-MM-dd)");
        }

        LocalDateTime start= localDate.atStartOfDay();
        LocalDateTime end= localDate.atTime(23, 59, 59);

        Double avg= starMapper.findAverageScore(restaurantId, category, start, end);

        // 아직 아무도 별점 안 남겼으면 0으로 처리
        if(avg==null) {
            return 0.0;
        }
        return avg;
    }

    // 개인 섭취 음식 추가
    public void addPersonalDiet(String loginBojId, PersonalDietRequestDto dto) {

        User me= userMapper.findByBojId(loginBojId);
        if(me==null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        if(dto.getMenu()==null || dto.getMenu().isBlank()){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "menu는 필수입니다.");
        }
        if(dto.getCalorie()==null || dto.getCalorie()<0){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "calorie는 0 이상이어야 합니다.");
        }

        PersonalDiet diet= new PersonalDiet();
        diet.setUserId(me.getUserId());
        diet.setMenu(dto.getMenu());
        diet.setCalorie(dto.getCalorie());
        diet.setDate(LocalDateTime.now());

        personalDietMapper.insertPersonalDiet(diet);
    }

    // 특정 날짜의 총 섭취 칼로리 조회 (개인 + 식당)
    public int getTotalDailyCalorie(String loginBojId, String dateStr) {

        User me= userMapper.findByBojId(loginBojId);
        if(me==null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        LocalDate localDate;
        try{
            localDate= LocalDate.parse(dateStr); // yyyy-MM-dd
        }catch(Exception e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "DATE 형식이 올바르지 않습니다. (yyyy-MM-dd)");
        }

        LocalDateTime start= localDate.atStartOfDay();
        LocalDateTime end= localDate.atTime(23, 59, 59);

        Integer personal= personalDietMapper.sumPersonalCalorie(me.getUserId(), start, end);
        Integer restaurant= dietCalorieMapper.sumRestaurantCalorie(me.getUserId(), start, end);

        if(personal==null) personal= 0;
        if(restaurant==null) restaurant= 0;

        return personal+restaurant;
    }

    // 특정 날짜 섭취 음식 전체 목록 조회
    public List<DailyDietItemResponseDto> getDailyDietList(String loginBojId, String dateStr) {

        User me = userMapper.findByBojId(loginBojId);
        if (me == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(dateStr);
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "DATE 형식이 올바르지 않습니다. (yyyy-MM-dd)");
        }

        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end = localDate.atTime(23, 59, 59);

        // 1️⃣ 식당 음식 (별점 기준)
        List<DailyDietItemResponseDto> restaurantList =
                dietListMapper.findRestaurantDietByUserAndDate(
                        me.getUserId(), start, end
                );

        // 2️⃣ 개인 섭취 음식
        List<DailyDietItemResponseDto> personalList =
                personalDietMapper.findPersonalDietList(
                        me.getUserId(), start, end
                );

        restaurantList.addAll(personalList);
        return restaurantList;
    }

}
