package com.example.food_project.jwt;

import com.google.gson.Gson;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenHelper {
    private Gson gson = new Gson();
    private String strKey = "RGF5IGxhIGNodW9pIGJhbyBtYXQgZGF5IGR1IDI1NiBiaXQ="; // Chuỗi Base 64

    public String generateToken(String data, String type, long expiredDate) {
        SecretKey SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKey));

        Date now = new Date();
        Date expired = new Date(now.getTime() + expiredDate);

        Map<String, Object> subjectData = new HashMap<>();
        subjectData.put("username", data);
        subjectData.put("type", type);

        String json = gson.toJson(subjectData);

        return Jwts.builder()
                .setSubject(json) // Lưu trữ dữ liệu vào trong token kiểu String
                .setIssuedAt(now) // Thời gian tạo token
                .setExpiration(expired) // Thời gian hết hạn của token
                .signWith(SecretKey, SignatureAlgorithm.HS256)
                .compact(); // Trả ra token đã được mã hoá
    }

    public String decodeToken(String token) {
        SecretKey SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKey));

        return Jwts.parserBuilder()
                .setSigningKey(SecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Lấy ra giá trị lưu trữ
    }

    public boolean validateToken(String token) {
        SecretKey SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKey));
        boolean isSuccess = false;
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            isSuccess = true;
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return isSuccess;
    }
}
