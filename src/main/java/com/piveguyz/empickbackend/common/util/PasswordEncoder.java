package com.piveguyz.empickbackend.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password5678@@";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("BCrypt 암호화된 비밀번호: " + encodedPassword);
    }
}
