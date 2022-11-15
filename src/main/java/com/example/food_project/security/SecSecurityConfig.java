package com.example.food_project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig {
    /* Dung de khoi tao danh sach users cứng và danh sách user  sẽ được lưu trữ ở RAM
     * */
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user1 = User.withUsername("cybersoft")
//                .password(passwordEncoder().encode("123"))
//                .roles("USERS")
//                .build();
//        UserDetails user2 = User.withUsername("admin")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

    @Autowired
    CustomedAuthenProvider customedAuthenProvider;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customedAuthenProvider);

        return authenticationManagerBuilder.build();
    }

    /* Kiểu mã hoá dữ liệu
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* Quy đinh các rule liên quan tới bảo mật và quyền truy cập
     * antMatchers : định nghĩa những link cần xác thực
     * authenticated : bắt buộc phải đăng nhâp (chứng thực) thì mới đc gọi
     * permitAll : cho phép truy câp full quyền vào link chỉ ở antMatchers
     * anyRequest : toàn bộ request gọi vào API
     * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/signin").permitAll()
                .antMatchers("/signin/test").authenticated()
                .anyRequest()
                .authenticated();

        return http.build();
    }
}
