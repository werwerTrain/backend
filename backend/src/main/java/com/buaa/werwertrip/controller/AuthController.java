package com.buaa.werwertrip.controller;

import com.buaa.werwertrip.entity.VerificationCode;
import com.buaa.werwertrip.service.IVerificationCodeService;
import com.buaa.werwertrip.service.Impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//import static org.springframework.web.bind.annotation.RequestMethod.GET;
//import static org.springframework.web.bind.annotation.RequestMethod.POST;
//
//@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "*",methods = {POST,GET})
@RestController
public class AuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private IVerificationCodeService verificationCodeService;


    @PostMapping("/idCode/{email}")
    public Map<String, Object> register(@PathVariable String email) {
        VerificationCode code = new VerificationCode(email);
        verificationCodeService.addVerificationCode(code);

        emailService.sendSimpleMail(email, "Verification Code",
                "【WerwerTrip】你的验证码是：" + code.getCode() + "，2分钟内有效");

        if (verificationCodeService.getVerificationCode(email) != null) {
            return new HashMap<>() {{
                put("result", true);
            }};
        } else {
            return new HashMap<>() {{
                put("result", false);
            }};
        }
    }

    @PostMapping("/idCode/{idCode}/{email}")
    public Map<String, Object> verify(@PathVariable String email, @PathVariable String idCode) {
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

