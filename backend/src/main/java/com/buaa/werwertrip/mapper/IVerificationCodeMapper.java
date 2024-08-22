package com.buaa.werwertrip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buaa.werwertrip.entity.VerificationCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IVerificationCodeMapper extends BaseMapper<VerificationCode> {
    VerificationCode getVerificationCode(String email);

    void addVerificationCode(VerificationCode code);
}
