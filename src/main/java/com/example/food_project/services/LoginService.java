package com.example.food_project.services;


import com.example.food_project.entity.UserEntity;

public interface LoginService {
    boolean checkLogin(String email, String password);

    UserEntity checkLogin(String email);
}
