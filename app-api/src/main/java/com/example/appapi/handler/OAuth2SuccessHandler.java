package com.example.appapi.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OAuth2SuccessHandler {
    public static void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, String kakaoId)
            throws IOException {

        // 리다이렉트
        res.sendRedirect("http://localhost:5173/login/kakao/"+kakaoId);
    }
}
