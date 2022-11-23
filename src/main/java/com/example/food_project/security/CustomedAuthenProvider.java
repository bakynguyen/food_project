package com.example.food_project.security;

import com.example.food_project.entity.UserEntity;
import com.example.food_project.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomedAuthenProvider implements AuthenticationProvider {
    @Autowired
    LoginService loginService;


    //** @Qualifier("Abc") : Lấy tên Bean được chỉ đình nếu có nhiểu hơn 2 Bean có tên giống nhau
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //* Xử lí logic code quyết định đăng nhập thành công hay thất bại
    //
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Query database kiem tra username and password co ton tai trong database hay ko
        UserEntity userEntity = loginService.checkLogin(username);

        if (userEntity != null) {
            boolean isMatchedPassword = passwordEncoder.matches(password, userEntity.getPassword());
            if (isMatchedPassword) {
                System.out.println("Kiem tra dang nhap " + userEntity.getEmail() + " - " + userEntity.getPassword());
                return new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
