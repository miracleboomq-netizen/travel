package com.travel.smarttravel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("t_order") // 👈 告诉程序，现在去存 t_order 表，别找 orders 了
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String username;
    private String spotName;
    private LocalDate orderDate;
    private Integer ticketCount;
    private String status;
}
