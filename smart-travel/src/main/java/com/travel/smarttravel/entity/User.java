package com.travel.smarttravel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data // 🌟 这个注解会自动帮你生成 getRole(), setRole() 等所有方法
@TableName("t_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String phone;

    // 🌟 之前报错就是因为少了下面这几个核心字段
    private String role;     // 角色：admin 或 user
    private String realName; // 真实姓名
    private String idCard;   // 身份证号
    private String gender;   // 性别
    private String avatar;   // 头像地址
}