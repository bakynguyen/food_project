package com.example.food_project.controller;

import com.example.food_project.jwt.JwtTokenHelper;
import com.example.food_project.payload.response.DataResponse;
import com.example.food_project.payload.response.DataTokenResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/refresh-token")
public class RefreshTokenController {
    private Gson gson = new Gson();
    private long expiredDate = 8 * 60 * 60 * 1000;
    private long refreshExpiredDate = 80 * 60 * 60 * 1000;
    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @PostMapping("")
    public ResponseEntity<?> index(@RequestParam("token") String token) {
        DataResponse dataResponse = new DataResponse();

        if (jwtTokenHelper.validateToken(token)) {
            String json = jwtTokenHelper.decodeToken(token);
            System.out.println("json " + json);
            Map<String, Object> mapJson = gson.fromJson(json, Map.class);

            if (StringUtils.hasText(mapJson.get("type").toString()) && mapJson.get("type").toString().equals("refresh")) {
                String tokenAuthen = jwtTokenHelper.generateToken(mapJson.get("username").toString(), "authen", expiredDate);
                String refreshToken = jwtTokenHelper.generateToken(mapJson.get("username").toString()
                        + "_refresh", "refresh", refreshExpiredDate);
                String decodeToken = jwtTokenHelper.decodeToken(tokenAuthen);

                DataTokenResponse dataTokenResponse = new DataTokenResponse();
                dataTokenResponse.setToken(tokenAuthen);
                dataTokenResponse.setRefreshToken(refreshToken);

                dataResponse.setStatus(HttpStatus.OK.value());
                dataResponse.setData(dataTokenResponse);
                dataResponse.setDesc(decodeToken);
                dataResponse.setSuccess(true);
            }
        } else {
            dataResponse.setStatus(HttpStatus.OK.value());
            dataResponse.setData("");
            dataResponse.setDesc("");
            dataResponse.setSuccess(true);
        }

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
