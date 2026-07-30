package com.travel.smarttravel.controller;

import com.travel.smarttravel.common.Result;
import com.travel.smarttravel.entity.User;
import com.travel.smarttravel.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 更新用户实名资料与基础信息
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody User user) {
        // 调用 MyBatis-Plus 提供的 updateById 方法
        int rows = userMapper.updateById(user);

        if (rows > 0) {
            return Result.success("资料更新成功");
        } else {
            // 🌟 修复点：这里只传一个字符串参数即可！
            return Result.error("资料更新失败，请重试");
        }
    }
}