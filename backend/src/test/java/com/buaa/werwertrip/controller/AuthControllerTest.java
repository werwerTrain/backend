package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.entity.VerificationCode;
import com.buaa.werwertrip.service.IVerificationCodeService;
import com.buaa.werwertrip.service.Impl.EmailService;
import com.buaa.werwertrip.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class AuthControllerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private IVerificationCodeService verificationCodeService;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    // 正面测试用例：成功注册验证码
    @Test
    void testRegister_ShouldReturnSuccess() {
        // Arrange
        String email = "test@example.com";
        VerificationCode code = new VerificationCode(email);
        code.setCode("123456");
        code.setGeneratedAt(LocalDateTime.now().minusMinutes(1));
        code.setExpiresAt(LocalDateTime.now().plusMinutes(1));

        when(verificationCodeService.getVerificationCode(email)).thenReturn(code);

        // Act
        Map<String, Object> response = authController.register(email);

        // Assert
        assertTrue((Boolean) response.get("result"));
        verify(verificationCodeService, times(1)).addVerificationCode(any(VerificationCode.class));
        verify(emailService, times(1)).sendSimpleMail(eq(email), anyString(), anyString());
    }

    // 反面测试用例：注册验证码失败
    @Test
    void testRegister_WhenCodeNotSaved_ShouldReturnFailure() {
        // Arrange
        String email = "test@example.com";
        VerificationCode code = new VerificationCode(email);
        code.setCode("123456");
        code.setGeneratedAt(LocalDateTime.now().minusMinutes(1));
        code.setExpiresAt(LocalDateTime.now().plusMinutes(1));

        when(verificationCodeService.getVerificationCode(email)).thenReturn(null);

        // Act
        Map<String, Object> response = authController.register(email);

        // Assert
        assertFalse((Boolean) response.get("result"));
        verify(verificationCodeService, times(1)).addVerificationCode(any(VerificationCode.class));
        verify(emailService, times(1)).sendSimpleMail(eq(email), anyString(), anyString());
    }

    // 正面测试用例：成功验证验证码
    @Test
    void testVerify_ShouldReturnSuccess() {
        // Arrange
        String email = "test@example.com";
        String idCode = "123456";
        LocalDateTime now = LocalDateTime.now();

        VerificationCode code = new VerificationCode(email);
        code.setCode(idCode);
        code.setGeneratedAt(now.minusMinutes(1));
        code.setExpiresAt(now.plusMinutes(1));

        when(verificationCodeService.getVerificationCode(email)).thenReturn(code);

        // Act
        Map<String, Object> response = authController.verify(email, idCode);

        // Assert
        assertTrue((Boolean) response.get("result"));
    }

    // 反面测试用例：验证码验证失败（验证码错误）
    @Test
    void testVerify_WhenCodeIsIncorrect_ShouldReturnFailure() {
        // Arrange
        String email = "test@example.com";
        String idCode = "wrongCode";
        LocalDateTime now = LocalDateTime.now();

        VerificationCode code = new VerificationCode(email);
        code.setCode("123456");
        code.setGeneratedAt(now.minusMinutes(1));
        code.setExpiresAt(now.plusMinutes(1));

        when(verificationCodeService.getVerificationCode(email)).thenReturn(code);

        // Act
        Map<String, Object> response = authController.verify(email, idCode);

        // Assert
        assertFalse((Boolean) response.get("result"));
    }

    // 反面测试用例：验证码验证失败（验证码过期）
    @Test
    void testVerify_WhenCodeIsExpired_ShouldReturnFailure() {
        // Arrange
        String email = "test@example.com";
        String idCode = "123456";
        LocalDateTime now = LocalDateTime.now();

        VerificationCode code = new VerificationCode(email);
        code.setCode(idCode);
        code.setGeneratedAt(now.minusMinutes(5)); // 验证码生成时间早于当前时间
        code.setExpiresAt(now.minusMinutes(1)); // 验证码已经过期

        when(verificationCodeService.getVerificationCode(email)).thenReturn(code);

        // Act
        Map<String, Object> response = authController.verify(email, idCode);

        // Assert
        assertFalse((Boolean) response.get("result"));
    }
}
