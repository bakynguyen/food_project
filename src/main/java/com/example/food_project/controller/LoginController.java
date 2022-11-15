package com.example.food_project.controller;

import com.example.food_project.payload.request.SignInRequest;
import com.example.food_project.payload.response.DataResponse;
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
    @Autowired
    LoginService loginService;

    @Autowired
    AuthenticationManager authenticationManager;

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

        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
//        dataResponse.setSuccess(loginService.checkLogin(request.getUsername(), request.getPassword()));

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
