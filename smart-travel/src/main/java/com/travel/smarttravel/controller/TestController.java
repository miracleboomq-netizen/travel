package com.travel.smarttravel.controller;

import com.travel.smarttravel.entity.Spot;
import com.travel.smarttravel.mapper.SpotMapper; // 👈 这一行是核心，必须手动导入！
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private SpotMapper spotMapper; // 👈 只要上面的 import 对了，这里就不再报红

    @GetMapping("/spots")
    public List<Spot> testGetSpots() {
        // 测试 MyBatis-Plus 是否能从数据库查到景区数据
        return spotMapper.selectList(null);
    }

    @GetMapping("/hello")
    public String hello() {
        return "后端服务运行正常！";
    }
}