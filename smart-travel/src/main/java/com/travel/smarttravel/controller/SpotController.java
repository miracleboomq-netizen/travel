package com.travel.smarttravel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.smarttravel.common.Result;
import com.travel.smarttravel.entity.Spot;
import com.travel.smarttravel.mapper.SpotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spot")
@CrossOrigin
public class SpotController {

    @Autowired
    private SpotMapper spotMapper;

    // 🌟 首页列表：支持搜索和分页
    @GetMapping("/list")
    public Result<Page<Spot>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "8") Integer pageSize,
            @RequestParam(required = false) String name) {

        Page<Spot> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Spot> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Spot::getName, name);
        }
        wrapper.orderByDesc(Spot::getId);
        return Result.success(spotMapper.selectPage(page, wrapper));
    }

    // 🌟 详情页接口
    @GetMapping("/{id}")
    public Result<Spot> getById(@PathVariable Integer id) {
        return Result.success(spotMapper.selectById(id));
    }
}