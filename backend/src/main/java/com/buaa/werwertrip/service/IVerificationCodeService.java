package com.buaa.werwertrip.service;

import com.buaa.werwertrip.entity.VerificationCode;
import org.springframework.stereotype.Component;

@Component
public interface IVerificationCodeService {
    public VerificationCode getVerificationCode(String email);

    public void addVerificationCode(VerificationCode code);
}
