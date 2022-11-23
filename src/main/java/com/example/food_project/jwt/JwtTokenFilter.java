package com.example.food_project.jwt;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private Gson gson = new Gson();
    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Cắt header và lấy token
        String token = getTokenFromHeader(request);

        if (token != null) {
            //{"type":"authen","username":"nguyenvana@gmail.com"}

            // Kiểm tra token có phải do hệ thống của mình sinh ra hay ko
            if (jwtTokenHelper.validateToken(token)) {
                String json = jwtTokenHelper.decodeToken(token);

                Map<String, Object> mapJson = gson.fromJson(json, Map.class);

                System.out.println("Kiem tra " + json + " type " + mapJson.get("type").toString());

                if (StringUtils.hasText(mapJson.get("type").toString()) && !mapJson.get("type").toString().equals("refresh")) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("",
                            "", new ArrayList<>());
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        // Lấy giá trị token ở Header có key là Authorization
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String finalToken = token.substring(7);
            return finalToken;
        }
        return null;
    }
}
