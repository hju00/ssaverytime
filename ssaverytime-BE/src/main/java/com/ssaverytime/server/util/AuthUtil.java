package com.ssaverytime.server.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    /**
     * 현재 로그인한 사용자의 bojId 반환
     */
    public static String getLoginUserId() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(authentication==null || authentication.getPrincipal()==null) {
            return null;
        }

        return authentication.getPrincipal().toString();
    }
}
