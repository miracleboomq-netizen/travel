package com.travel.smarttravel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.smarttravel.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    // 🌟 统计每个景区的预约人数，返回给大屏
    // name 对应景区名，value 对应预约次数
    @Select("SELECT spot_name as name, COUNT(*) as value FROM t_order GROUP BY spot_name")
    List<Map<String, Object>> getSpotStatistics();
}