package com.ssaverytime.server.service;

import com.ssaverytime.server.domain.dto.social.SocialUserResponseDto;
import com.ssaverytime.server.domain.enums.user.UserValid;
import com.ssaverytime.server.domain.model.User;
import com.ssaverytime.server.mapper.FollowMapper;
import com.ssaverytime.server.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final UserMapper userMapper;
    private final FollowMapper followMapper;

    // bojId로 사용자 조회
    public SocialUserResponseDto findByBojId(String bojId) {
        User user= userMapper.findByBojId(bojId);
        if(user==null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "해당 BOJ ID의 사용자가 없습니다.");
        }

        boolean withdrawn= (user.getValid()==UserValid.INVALID);

        return new SocialUserResponseDto(
                user.getBaekjoon(),
                user.getBojId(),
                user.getName(),
                user.getSeason(),
                withdrawn
        );
    }

    // 2. name으로 사용자 목록 조회
    public List<SocialUserResponseDto> findByName(String name) {
        List<User> users= userMapper.findByName(name);
        if(users==null || users.isEmpty()){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "해당 이름의 사용자가 없습니다.");
        }

        return users.stream()
                .map(user -> new SocialUserResponseDto(
                        user.getBaekjoon(),
                        user.getBojId(),
                        user.getName(),
                        user.getSeason(),
                        user.getValid() == UserValid.INVALID
                ))
                .collect(Collectors.toList());
    }

    // 팔로우 (양방향 튜플 두 개 insert)
    public void follow(String myBojId, String targetBojId) {
        if(myBojId.equals(targetBojId)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "자기 자신은 팔로우할 수 없습니다.");
        }

        // 존재하는 유저인지 간단히 확인 (필요 없으면 생략 가능)
        User target= userMapper.findByBojId(targetBojId);
        if(target==null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "팔로우 대상 사용자를 찾을 수 없습니다.");
        }

        // 이미 팔로우 관계인지 확인
        int already= followMapper.countFollow(myBojId, targetBojId);
        if(already>0){
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "이미 팔로우 중인 사용자입니다.");
        }

        // 두 개 방향으로 튜플 삽입
        followMapper.insertFollow(myBojId, targetBojId);
        // followMapper.insertFollow(targetBojId, myBojId);
    }

    // 4. 언팔로우 (양방향 튜플 두 개 delete)
    public void unfollow(String myBojId, String targetBojId) {
        if(myBojId.equals(targetBojId)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "자기 자신은 언팔로우할 수 없습니다.");
        }

        followMapper.deleteFollow(myBojId, targetBojId);
        // followMapper.deleteFollow(targetBojId, myBojId);
    }

    // 내가 팔로우한 사람들
    public List<SocialUserResponseDto> getFollowingList(String myBojId) {

        List<User> users = followMapper.findFollowing(myBojId);

        return users.stream()
                .map(u -> new SocialUserResponseDto(
                        u.getBaekjoon(),
                        u.getBojId(),
                        u.getName(),
                        u.getSeason(),
                        u.getValid() == UserValid.INVALID
                ))
                .toList();
    }

    // 나를 팔로우한 사람들
    public List<SocialUserResponseDto> getFollowerList(String myBojId) {

        List<User> users = followMapper.findFollowers(myBojId);

        return users.stream()
                .map(u -> new SocialUserResponseDto(
                        u.getBaekjoon(),
                        u.getBojId(),
                        u.getName(),
                        u.getSeason(),
                        u.getValid() == UserValid.INVALID
                ))
                .toList();
    }

}
