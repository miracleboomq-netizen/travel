package com.travel.smarttravel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.smarttravel.common.Result;
import com.travel.smarttravel.entity.Comment;
import com.travel.smarttravel.entity.Order;
import com.travel.smarttravel.entity.Spot;
import com.travel.smarttravel.entity.User;
import com.travel.smarttravel.mapper.CommentMapper;
import com.travel.smarttravel.mapper.OrderMapper;
import com.travel.smarttravel.mapper.SpotMapper;
import com.travel.smarttravel.mapper.UserMapper;
import com.travel.smarttravel.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired private SpotMapper spotMapper;
    @Autowired private OrderMapper orderMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private CommentMapper commentMapper;

    // ==================== 1. 景区管理 (CRUD) ====================

    @GetMapping("/spots")
    public Result<List<Spot>> getSpots() {
        return Result.success(spotMapper.selectList(null));
    }

    @PostMapping("/spot/add")
    public Result<?> addSpot(@RequestBody Spot spot) {
        spotMapper.insert(spot);
        return Result.success(null);
    }

    @PutMapping("/spot/update")
    public Result<?> updateSpot(@RequestBody Spot spot) {
        spotMapper.updateById(spot);
        return Result.success(null);
    }

    @DeleteMapping("/spot/delete/{id}")
    public Result<?> deleteSpot(@PathVariable Integer id) {
        spotMapper.deleteById(id);
        return Result.success(null);
    }

    // ==================== 2. 用户管理 (查、改、删) ====================

    @GetMapping("/users")
    public Result<List<User>> getUsers() {
        return Result.success(userMapper.selectList(null));
    }

    @PutMapping("/user/update")
    public Result<?> updateUser(@RequestBody User user) {
        // 如果管理员修改了用户密码，需要重新进行 MD5 加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(Md5Util.encode(user.getPassword()));
        }
        userMapper.updateById(user);
        return Result.success(null);
    }

    @DeleteMapping("/user/delete/{id}")
    public Result<?> deleteUser(@PathVariable Integer id) {
        userMapper.deleteById(id);
        return Result.success(null);
    }

    // ==================== 3. 订单管理 (O2O扫码核销) ====================

    @GetMapping("/orders")
    public Result<List<Order>> getOrders() {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Order::getId); // 按最新订单排序
        return Result.success(orderMapper.selectList(wrapper));
    }

    // 🌟 修正了接口路径，并增加了防重复核销的判断逻辑
    @PutMapping("/order/verify/{id}")
    public Result<?> verifyOrder(@PathVariable Integer id) {
        Order order = orderMapper.selectById(id);

        if (order == null) {
            return Result.error("查无此单，请确认订单号是否正确！");
        }

        // 防重复核销拦截
        if ("已核销".equals(order.getStatus())) {
            return Result.error("警告：该电子票已核销，请勿重复入园！");
        }

        // 更改状态为已核销
        order.setStatus("已核销");
        orderMapper.updateById(order);

        return Result.success("核销成功，准许入园！");
    }

    // ==================== 4. 评论管理 (审核/删除) ====================

    @GetMapping("/comments")
    public Result<List<Comment>> getComments() {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Comment::getCreateTime); // 最新评论排前面方便审核
        return Result.success(commentMapper.selectList(wrapper));
    }

    @DeleteMapping("/comment/delete/{id}")
    public Result<?> deleteComment(@PathVariable Integer id) {
        commentMapper.deleteById(id);
        return Result.success(null);
    }
}