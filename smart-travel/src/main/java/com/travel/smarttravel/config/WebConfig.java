package com.travel.smarttravel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 确定存放图片的物理路径（项目根目录下的 uploads 文件夹）
        String path = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); // 自动创建文件夹
        }

        // 2. 核心映射：
        // 浏览器访问 http://localhost:8080/uploads/xxx.jpg
        // 对应 硬盘路径 项目根目录/uploads/xxx.jpg
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path);
    }
}