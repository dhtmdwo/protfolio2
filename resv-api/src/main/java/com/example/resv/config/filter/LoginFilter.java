package com.example.resv.config.filter;

import com.example.appapi.users.model.Users;
import com.example.appapi.users.model.UsersDto;
import com.example.appapi.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("LoginFilter 실행됐다.");
        UsernamePasswordAuthenticationToken authToken;

        try {
            UsersDto.SignupRequest UsersDto  = new ObjectMapper().readValue(request.getInputStream(), UsersDto.SignupRequest.class);

            authToken =
                    new UsernamePasswordAuthenticationToken(UsersDto.getUserId(), UsersDto.getPassword(), null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Users users = (Users) authResult.getPrincipal();
        String jwtToken = JwtUtil.generateToken(users);

        ResponseCookie cookie = ResponseCookie
                .from("ATOKEN", jwtToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofHours(1L))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print("{");
        out.print("\"userId\": \"" + users.getUserId() + "\",");
        out.print("\"userType\": \"" + users.getUserType().name() + "\"");
        out.print("}");
        out.flush();
    }
}
