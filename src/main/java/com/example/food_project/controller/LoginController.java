package com.example.food_project.controller;

import com.example.food_project.jwt.JwtTokenHelper;
import com.example.food_project.payload.request.SignInRequest;
import com.example.food_project.payload.response.DataResponse;
import com.example.food_project.payload.response.DataTokenResponse;
import com.example.food_project.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin //Cho phep nhung domain khac voi domain cua api truy cap vao
@RequestMapping("/signin")
public class LoginController {
    private long expiredDate = 8 * 60 * 60 * 1000;
    private long refreshExpiredDate = 80 * 60 * 60 * 1000;

    @Autowired
    LoginService loginService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @GetMapping("/test")
    public String test() {
        return "Hello";
    }

    @PostMapping("")
    public ResponseEntity<?> signin(@RequestBody SignInRequest request) {
        UsernamePasswordAuthenticationToken authenRequest =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(authenRequest);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        String token = jwtTokenHelper.generateToken(request.getUsername(), "authen", expiredDate);
        String refreshToken = jwtTokenHelper.generateToken(request.getUsername() + "_refresh", "refresh", refreshExpiredDate);
        String decodeToken = jwtTokenHelper.decodeToken(token);

        // Khi ????ng nh???p th??nh c??ng tr??? th??m Refresh Token (Kh??ng c?? th???i gian expired)
        // Ta??? controller refresh
        // Ki???m tra Refresh Token n??y c?? h???p l??? hay ko, n???u h???p l??? ph???i tr??? ra token m???i

        DataTokenResponse dataTokenResponse = new DataTokenResponse();
        dataTokenResponse.setToken(token);
        dataTokenResponse.setRefreshToken(refreshToken);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setData(dataTokenResponse);
        dataResponse.setDesc(decodeToken);
//        dataResponse.setSuccess(loginService.checkLogin(request.getUsername(), request.getPassword()));

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
