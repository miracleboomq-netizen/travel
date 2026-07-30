package com.travel.smarttravel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.smarttravel.common.Result;
import com.travel.smarttravel.entity.User;
import com.travel.smarttravel.mapper.UserMapper;
import com.travel.smarttravel.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User loginUser) {
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();

        System.out.println("====== 收到登录请求 ======");
        System.out.println(">>> 账号: " + username);
        System.out.println(">>> 原始输入密码: " + password);

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        if (user != null) {
            String dbPwd = user.getPassword();
            String inputMd5 = Md5Util.encode(password);

            System.out.println(">>> 数据库密码: " + dbPwd);
            System.out.println(">>> 输入加密后: " + inputMd5);

            // 🌟 核心改进：双重验证
            if (dbPwd.equals(inputMd5) || dbPwd.equals(password)) {
                System.out.println(">>> [成功] 验证通过！用户角色: " + user.getRole());
                return Result.success(user);
            } else {
                System.out.println(">>> [失败] 密码比对没对上");
            }
        } else {
            System.out.println(">>> [失败] 用户名不存在");
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User newUser) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, newUser.getUsername());
        if (userMapper.selectOne(wrapper) != null) return Result.error("用户名已存在");

        newUser.setPassword(Md5Util.encode(newUser.getPassword()));
        if (newUser.getRole() == null) newUser.setRole("user");
        userMapper.insert(newUser);
        return Result.success("注册成功");
    }
}