package com.buaa.werwertrip.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Random;

@TableName("verificationCodes")
public class VerificationCode {

    private String email;
    private String code;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;

    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成6位随机验证码
        return String.valueOf(code);
    }

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public VerificationCode(String email) {
        this.email = email;
        this.code = VerificationCode.generateVerificationCode();
        this.generatedAt = LocalDateTime.now();
        this.expiresAt = generatedAt.plusMinutes(2);
    }
}

