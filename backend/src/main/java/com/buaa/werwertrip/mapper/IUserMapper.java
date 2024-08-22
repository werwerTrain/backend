package com.buaa.werwertrip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buaa.werwertrip.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper extends BaseMapper<User> {
    // 用户注册
    //@Insert("insert into users(id,name, password, email) values(#{id},#{name},#{password},#{email})")
    void registerUser(String id, String name, String password, String email, String salt);

    // 是否已注册
    //@Select("select count(1) from users where id=#{id}")
    int findUserById(String id);

    // 查看用户信息
    //@Select("select * from users where id=#{id}")
    User findById(String id);

    //@Select("select * from users where id=#{id} and password=#{password}")
    User login(String id, String password);

    String getUserSalt(String id);

    Integer updatePassword(String userId, String newpassword);

    String getEmail(String userId);

}
