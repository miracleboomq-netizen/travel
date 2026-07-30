package com.travel.smarttravel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_spot") // 🌟 关键：必须指向数据库里的 t_spot 表
public class Spot {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    // 🌟 解决 desc 关键字问题，映射到数据库的 description 字段
    @TableField("description")
    private String description;

    // 🌟 映射到数据库的 image_url 字段
    @TableField("image_url")
    private String imageUrl;
    private String strategy;
}