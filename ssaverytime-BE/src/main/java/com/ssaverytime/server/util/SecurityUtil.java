package com.ssaverytime.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class SecurityUtil {

    /**
     * boardId와 userId를 조합하여 SHA-256 해시를 생성합니다.
     * @param boardId 게시글 ID
     * @param userSeq 사용자 ID
     * @return 생성된 해시 문자열 (Hex format)
     */
    public static String generateAuthorHash(int boardId, int userSeq) {
        try {
            // 해시 입력값: boardId + ":" + userSeq
            String input = boardId + ":" + userSeq;
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
