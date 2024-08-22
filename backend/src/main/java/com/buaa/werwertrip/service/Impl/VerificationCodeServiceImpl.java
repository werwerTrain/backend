package com.buaa.werwertrip.service.Impl;

import com.buaa.werwertrip.entity.VerificationCode;
import com.buaa.werwertrip.mapper.IVerificationCodeMapper;
import com.buaa.werwertrip.service.IVerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("verificationCodeService")
public class VerificationCodeServiceImpl implements IVerificationCodeService {

    @Autowired
    private IVerificationCodeMapper verificationCodeMapper;

    @Override
    public VerificationCode getVerificationCode(String email) {
        return verificationCodeMapper.getVerificationCode(email);
    }

    @Override
    public void addVerificationCode(VerificationCode code) {
        verificationCodeMapper.addVerificationCode(code);
    }
}
