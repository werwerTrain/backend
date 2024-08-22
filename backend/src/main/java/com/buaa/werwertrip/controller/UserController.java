package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.service.Impl.EmailService;
import com.buaa.werwertrip.entity.User;
import com.buaa.werwertrip.entity.VerificationCode;
import com.buaa.werwertrip.service.IPassengerService;
import com.buaa.werwertrip.service.IUserService;
import com.buaa.werwertrip.service.IVerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

//@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "*",methods = {POST,GET})
@RestController
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private User user;
    @Autowired
    private IPassengerService passengerService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IVerificationCodeService verificationCodeService;

    @PostMapping(value = "/register")
    public Map<String, Object> userRegister(@RequestBody Map<String, Object> registerMap) {
        String id = registerMap.get("id").toString();
        String name = registerMap.get("name").toString();
        String password = registerMap.get("password").toString();
        String email = registerMap.get("email").toString();
        int time = Integer.parseInt(registerMap.get("time").toString());
        Map<String, Object> map = new HashMap<>();

        // 校验身份证号
        if (!id.matches("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")) {
            map.put("result", false);
            map.put("reason", "身份证号格式错误");
            return map;
        }

        // email格式校验(支持中文)
        if (!email.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            map.put("result", false);
            map.put("reason", "email格式错误");
            return map;
        }

        // 校验密码格式（密码要求：至少8个字符，包含数字和字母）
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$")) {
            map.put("result", false);
            map.put("reason", "密码格式错误(密码要求：至少8个字符，包含数字和字母)");
            return map;
        }
        if (userService.findUserById(id) > 0) {
            map.put("result", false);
            map.put("reason", "用户已注册");
        } else {
            if (time == 2) {
                userService.registerUser(id, name, password, email);
                //name, identification, phone, userId
                String identification = id;
                String userId = id;
                String phone = email;
                passengerService.addPassenger(name,identification,phone,userId);
            }
            map.put("result", true);
            map.put("reason", "注册成功");
        }
        return map;
    }

    @GetMapping("/users/{id}")
    public Map<String, Object> showUserInfo(@PathVariable String id) {
        User showuser = userService.findById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("id", showuser.getId());
        map.put("name", showuser.getName());
        map.put("email", showuser.getEmail());
        map.put("password", showuser.getPassword());
        return map;
    }

    @PostMapping("/login")
    public Map<String, Object> userLogin(@RequestBody Map<String, Object> loginMap) {
        String id = loginMap.get("id").toString();
        String password = loginMap.get("password").toString();
        Map<String, Object> map = new HashMap<>();
        if (userService.findUserById(id) == 0) {
            map.put("message", "用户未注册");
            map.put("result", false);
            map.put("email", null);
            map.put("name", null);
        } else {
            User loginuser = userService.login(id, password);

            if (loginuser != null) {
                //map.put("id", loginuser.getId());
                map.put("message", "登录成功");
                map.put("result", true);
                map.put("email", loginuser.getEmail());
                map.put("name", loginuser.getName());
            } else {
                map.put("message", "用户id或密码错误");
                map.put("result", false);
                map.put("email", null);
                map.put("name", null);
            }
        }
        return map;
    }

    @PostMapping("/updatepassword")
    public Map<String, Object> updatePassword(@RequestBody Map<String, Object> map) {
        String userId = map.get("id").toString();
        String newpassword = map.get("newpassword").toString();
        Integer res = userService.updatePassword(userId, newpassword);
        if (res == 0) {
            return new HashMap<>() {{
                put("result", false);
            }};
        } else {
            return new HashMap<>() {{
                put("result", true);
            }};
        }
    }

    @GetMapping("/getbackPassword/{id}")
    public Map<String, Object> forgetPassword(@PathVariable String id){
        if (!id.matches("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")) {
            return new HashMap<>() {{
                put("result", false);
                put("message","身份证号格式错误");
            }};

        }
        String email = userService.getEmail(id);

        if(email==null){
            return new HashMap<>() {{
                put("result", false);
                put("message","未注册");
            }};
        }
        VerificationCode code = new VerificationCode(email);
        verificationCodeService.addVerificationCode(code);

        emailService.sendSimpleMail(email, "Verification Code",
                "【WerwerTrip】Your verification code is " + code.getCode());

        if (verificationCodeService.getVerificationCode(email) != null) {
            return new HashMap<>() {{
                put("result", true);
                put("message","成功发送验证码");
            }};
        } else {
            return new HashMap<>() {{
                put("result", false);
                put("message","验证码发送失败");
            }};
        }
    }

    @PostMapping("id/idCode/{idCode}/{id}")
    public Map<String, Object> idCodeByEmail(@PathVariable String idCode,
                                             @PathVariable String id){
        String email = userService.getEmail(id);
        VerificationCode code = verificationCodeService.getVerificationCode(email);

        if (code != null) {
            if (code.getCode().equals(idCode)) {
                LocalDateTime now = LocalDateTime.now();
                if (now.isAfter(code.getGeneratedAt()) && now.isBefore(code.getExpiresAt())) {
                    return new HashMap<>() {{
                        put("result", true);
                    }};
                }
            }
        }
        return new HashMap<>() {{
            put("result", false);
        }};
    }
}
