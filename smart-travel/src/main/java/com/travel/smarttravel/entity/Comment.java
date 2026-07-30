package com.travel.smarttravel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("t_comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String content;
    private Integer userId;
    private String username;
    private Integer spotId;
    private Date createTime;
    private Integer score; // 对应数据库新加的字段
    private Integer parentId; // 🌟 必须有这个，用于记录回复谁
}