package com.example.food_project.controller;

import com.example.food_project.payload.request.SignInRequest;
import com.example.food_project.payload.response.DataResponse;
import com.example.food_project.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin //Cho phep nhung domain khac voi domain cua api truy cap vao
@RequestMapping("/signin")
public class LoginController {
    @Autowired
    LoginService loginService;

    @PostMapping("")
    public ResponseEntity<?> signin(@RequestBody SignInRequest request) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setSuccess(loginService.checkLogin(request.getUsername(), request.getPassword()));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
