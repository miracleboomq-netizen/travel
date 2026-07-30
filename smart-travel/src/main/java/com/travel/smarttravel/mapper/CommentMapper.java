package com.travel.smarttravel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.smarttravel.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}