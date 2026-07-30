package com.travel.smarttravel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.travel.smarttravel.common.Result;
import com.travel.smarttravel.entity.Order;
import com.travel.smarttravel.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    // 🌟 核心：大屏统计数据接口 (解决 405 报错)
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> data = new HashMap<>();

        // 1. 累计预约总量
        Long totalCount = orderMapper.selectCount(null);
        data.put("totalCount", totalCount);

        // 2. 今日新增预约
        LambdaQueryWrapper<Order> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.eq(Order::getOrderDate, LocalDate.now());
        Long todayCount = orderMapper.selectCount(todayWrapper);
        data.put("todayCount", todayCount);

        // 3. 入园游客总数 (状态为已核销)
        LambdaQueryWrapper<Order> checkInWrapper = new LambdaQueryWrapper<>();
        checkInWrapper.eq(Order::getStatus, "已核销");
        Long checkInCount = orderMapper.selectCount(checkInWrapper);
        data.put("checkInCount", checkInCount);

        // 4. 景区预约热度排名 (按景区分组统计)
        // 这里使用 QueryWrapper 执行分组查询
        QueryWrapper<Order> groupWrapper = new QueryWrapper<>();
        groupWrapper.select("spot_name as name", "count(*) as value")
                .groupBy("spot_name")
                .orderByDesc("value");
        List<Map<String, Object>> ranking = orderMapper.selectMaps(groupWrapper);
        data.put("ranking", ranking);

        return Result.success(data);
    }

    // --- 以下是之前的订单逻辑，保持不变 ---
    @GetMapping("/user/{userId}")
    public Result<List<Order>> getByUserId(@PathVariable Integer userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId).orderByDesc(Order::getId);
        return Result.success(orderMapper.selectList(wrapper));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Order order) {
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDate.now());
        }
        order.setStatus("已预约");
        orderMapper.insert(order);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        orderMapper.deleteById(id);
        return Result.success(null);
    }
    /**
     * 根据订单 ID 删除已失效的订单
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteOrder(@PathVariable Integer id) {
        // 直接从数据库中物理删除该条订单记录
        boolean success = orderMapper.deleteById(id) > 0;
        if (success) {
            return Result.success("订单删除成功");
        } else {
            return Result.error("删除失败，订单可能不存在");
        }
    }
}