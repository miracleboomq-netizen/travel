package com.travel.smarttravel.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.smarttravel.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from t_user where username = #{username} and password = #{password} limit 1")
    User login(String username, String password);
}