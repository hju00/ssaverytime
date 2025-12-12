package com.ssaverytime.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final RestTemplate restTemplate = new RestTemplate();
    // Docker Compose 서비스명 사용
    private final String FASTAPI_URL = "http://ai-service:8000/summary";

    @Override
    public String getSummary(String originalText) {
        Map<String, String> request = new HashMap<>();
        request.put("text", originalText);

        try {
            Map response = restTemplate.postForObject(FASTAPI_URL, request, Map.class);
            
            if (response != null && response.containsKey("summary")) {
                return (String) response.get("summary");
            }
            return "요약 실패: 응답이 올바르지 않습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "AI 서비스 호출 중 오류 발생: " + e.getMessage();
        }
    }
}