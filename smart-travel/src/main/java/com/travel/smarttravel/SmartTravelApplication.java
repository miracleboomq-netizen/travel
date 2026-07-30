package com.travel.smarttravel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 🚨 这一行是灵魂！一定要指向你的 mapper 文件夹路径
@MapperScan("com.travel.smarttravel.mapper")
public class SmartTravelApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmartTravelApplication.class, args);
	}
}