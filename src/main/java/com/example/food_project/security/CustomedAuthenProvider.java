package com.example.food_project.security;

import com.example.food_project.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomedAuthenProvider implements AuthenticationProvider {
    @Autowired
    LoginService loginService;

    //* Xử lí logic code quyết định đăng nhập thành công hay thất bại
    //
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Query database kiem tra username and password co ton tai trong database hay ko
        boolean isSuccess = loginService.checkLogin(username, password);
        System.out.println("Kiem tra dang nhap " + isSuccess);

        if (isSuccess) {
            return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
