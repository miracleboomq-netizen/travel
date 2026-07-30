package com.travel.smarttravel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travel.smarttravel.common.Result;
import com.travel.smarttravel.entity.Comment;
import com.travel.smarttravel.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    // 获取某个景区的评论列表
    @GetMapping("/list/{spotId}")
    public Result<List<Comment>> getBySpot(@PathVariable Integer spotId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        // 🌟 优化：按 ID 倒序排列，保证刚刚发出的最新评论显示在最上方！
        wrapper.eq(Comment::getSpotId, spotId)
                .orderByDesc(Comment::getId);
        return Result.success(commentMapper.selectList(wrapper));
    }

    // 提交评论或回复
    @PostMapping("/add")
    public Result<?> add(@RequestBody Comment comment) {
        commentMapper.insert(comment);
        return Result.success("发表成功");
    }
}