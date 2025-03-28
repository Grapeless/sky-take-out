package com.lim.mapper;

import com.lim.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid} ")
    User selectByOpenId(String openid);

    void save(User user);

    @Select("select * from user where id = #{userId} ")
    User getById(Long userId);
}
