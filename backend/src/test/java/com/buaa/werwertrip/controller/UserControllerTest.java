package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.entity.User;
import com.buaa.werwertrip.entity.VerificationCode;
import com.buaa.werwertrip.service.IPassengerService;
import com.buaa.werwertrip.service.IUserService;
import com.buaa.werwertrip.service.IVerificationCodeService;
import com.buaa.werwertrip.service.Impl.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private IUserService userService;

    @Mock
    private User user;

    @Mock
    private IPassengerService passengerService;

    @Mock
    private EmailService emailService;

    @Mock
    private IVerificationCodeService verificationCodeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // userRegister正面用例
    @Test
    public void testUserRegister_Successful() {
        Map<String, Object> registerMap = new HashMap<>();
        registerMap.put("id", "34567820021010123X");
        registerMap.put("name", "John");
        registerMap.put("password", "Password123");
        registerMap.put("email", "john@example.com");
        registerMap.put("time", 2);


        // 模拟了一个场景，即数据库中没有找到与该身份证号匹配的用户，意味着该用户尚未注册
        when(userService.findUserById("34567820021010123X")).thenReturn(0);

        Map<String, Object> result = userController.userRegister(registerMap);

        // System.out.println(result.get("reason"));
        assertEquals(true, (Boolean) result.get("result"));
        assertEquals("注册成功", result.get("reason"));
        // 使用verify方法来验证在userRegister方法被调用时，userService对象的registerUser方法是否被调用了一次
        // 并且传入的参数是否是"34567820021010123X", "John", "Password123"和"john@example.com"。
        verify(userService, times(1)).registerUser("34567820021010123X", "John", "Password123", "john@example.com");
        verify(passengerService, times(1)).addPassenger("John", "34567820021010123X", "john@example.com", "34567820021010123X");
    }

    // userRegister反面用例
    @Test
    public void testUserRegister_Failure_InvalidId() {
        Map<String, Object> registerMap = new HashMap<>();
        registerMap.put("id", "invalid_id");
        registerMap.put("name", "John");
        registerMap.put("password", "Password123");
        registerMap.put("email", "john@example.com");
        registerMap.put("time", 2);

        Map<String, Object> result = userController.userRegister(registerMap);

        assertEquals(false, result.get("result"));
        assertEquals("身份证号格式错误", result.get("reason"));
        verify(userService, never()).registerUser(anyString(), anyString(), anyString(), anyString());
    }

    // showUserInfo正面用例
    @Test
    public void testShowUserInfo() {
        User user = new User();
        user.setId("34567820021010123X");
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("Password123");

        when(userService.findById("34567820021010123X")).thenReturn(user);

        Map<String, Object> result = userController.showUserInfo("34567820021010123X");

        assertEquals("34567820021010123X", result.get("id"));
        assertEquals("John", result.get("name"));
        assertEquals("john@example.com", result.get("email"));
        assertEquals("Password123", result.get("password"));
    }

    // userLogin正面用例
    @Test
    public void testUserLogin_Success() {
        User user = new User();
        user.setId("34567820021010123X");
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("Password123");

        when(userService.findUserById("34567820021010123X")).thenReturn(1);
        when(userService.login("34567820021010123X", "Password123")).thenReturn(user);

        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("id", "34567820021010123X");
        loginMap.put("password", "Password123");

        Map<String, Object> result = userController.userLogin(loginMap);

        assertEquals(true, result.get("result"));
        assertEquals("登录成功", result.get("message"));
        assertEquals("john@example.com", result.get("email"));
        assertEquals("John", result.get("name"));
    }

    // userLogin反面用例
    @Test
    public void testUserLogin_WrongPassword() {
        User user = new User();
        user.setId("34567820021010123X");
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("123123hhh");

        when(userService.findUserById("34567820021010123X")).thenReturn(1);
        when(userService.login("34567820021010123X", "123123hhh")).thenReturn(null);

        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("id", "34567820021010123X");
        loginMap.put("password", "Password123");

        Map<String, Object> result = userController.userLogin(loginMap);

        assertEquals(false, result.get("result"));
        assertEquals("用户id或密码错误", result.get("message"));
        Assertions.assertNull(result.get("email"));
        Assertions.assertNull(result.get("name"));
    }


    // forgetPassword正面用例
    @Test
    public void testForgetPassword_Success() {
        when(userService.getEmail("34567820021010123X")).thenReturn("john@example.com");

        VerificationCode code = new VerificationCode("john@example.com");
        when(verificationCodeService.getVerificationCode("john@example.com")).thenReturn(code);

        Map<String, Object> result = userController.forgetPassword("34567820021010123X");

        assertEquals(true, result.get("result"));
        assertEquals("成功发送验证码", result.get("message"));
        //verify(emailService, times(1)).sendSimpleMail(eq("john@example.com"), eq("Verification Code"), contains(code.getCode()));
    }

    // forgetPassword反面用例
    @Test
    public void testForgetPassword_InvalidId() {
        when(userService.getEmail("123456789012345678")).thenReturn("john@example.com");

        VerificationCode code = new VerificationCode("john@example.com");
        when(verificationCodeService.getVerificationCode("john@example.com")).thenReturn(code);

        Map<String, Object> result = userController.forgetPassword("123456789012345678");

        assertEquals(false, result.get("result"));
        assertEquals("身份证号格式错误", result.get("message"));
        //verify(emailService, times(1)).sendSimpleMail(eq("john@example.com"), eq("Verification Code"), contains(code.getCode()));
    }


    @Test
    public void testIdCodeByEmail_WhenIdCodeIsValid_ShouldReturnTrue() {
        String mockId = "test", mockEmail = "test@buaa.edu.cn", mockCode = "114514";
        VerificationCode mockVerificationCode = new VerificationCode(mockEmail);
        mockVerificationCode.setCode(mockCode);
        mockVerificationCode.setGeneratedAt(LocalDateTime.now().minusMinutes(1));
        mockVerificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(1));

        when(userService.getEmail(mockId)).thenReturn(mockEmail);
        when(verificationCodeService.getVerificationCode(mockEmail)).thenReturn(mockVerificationCode);

        // Act
        Map<String, Object> result = userController.idCodeByEmail(mockCode, mockId);

        // Asserts
        Assertions.assertTrue((Boolean)result.get("result"));

        verify(userService, times(1)).getEmail(anyString());
        verify(verificationCodeService, times(1)).getVerificationCode(anyString());
    }

    @Test
    public void testIdCodeByEmail_WhenIdCodeIsWrong_ShouldReturnFalse() {
        String mockId = "test", mockEmail = "test@buaa.edu.cn";
        String rightCode = "114514", wrongCode = "1919810";
        VerificationCode mockVerificationCode = new VerificationCode(mockEmail);
        mockVerificationCode.setCode(rightCode);
        mockVerificationCode.setGeneratedAt(LocalDateTime.now().minusMinutes(1));
        mockVerificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(1));

        when(userService.getEmail(mockId)).thenReturn(mockEmail);
        when(verificationCodeService.getVerificationCode(mockEmail)).thenReturn(mockVerificationCode);

        // Act
        Map<String, Object> result = userController.idCodeByEmail(wrongCode, mockId);

        // Asserts
        Assertions.assertFalse((Boolean)result.get("result"));

        verify(userService, times(1)).getEmail(anyString());
        verify(verificationCodeService, times(1)).getVerificationCode(anyString());
    }

    @Test
    public void testIdCodeByEmail_WhenIdCodeIsExpired_ShouldReturnTrue() {
        String mockId = "test", mockEmail = "test@buaa.edu.cn", mockCode = "114514";
        VerificationCode mockVerificationCode = new VerificationCode(mockEmail);
        mockVerificationCode.setCode(mockCode);
        mockVerificationCode.setGeneratedAt(LocalDateTime.now().minusMinutes(3));
        mockVerificationCode.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(userService.getEmail(mockId)).thenReturn(mockEmail);
        when(verificationCodeService.getVerificationCode(mockEmail)).thenReturn(mockVerificationCode);

        // Act
        Map<String, Object> result = userController.idCodeByEmail(mockCode, mockId);

        // Asserts
        Assertions.assertFalse((Boolean)result.get("result"));

        verify(userService, times(1)).getEmail(anyString());
        verify(verificationCodeService, times(1)).getVerificationCode(anyString());
    }
}