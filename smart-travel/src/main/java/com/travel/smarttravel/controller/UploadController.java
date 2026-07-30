package com.travel.smarttravel.controller;

import com.travel.smarttravel.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin // 解决跨域问题
public class UploadController {

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        // 1. 获取项目根路径下的 uploads 文件夹
        String projectPath = System.getProperty("user.dir");
        String uploadDir = projectPath + File.separator + "uploads" + File.separator;

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // 2. 给文件起个唯一的名字，防止同名覆盖
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;

        try {
            // 3. 把文件真正存到硬盘上
            file.transferTo(new File(uploadDir + fileName));

            // 4. 【重要】返回浏览器可以访问的真实地址
            // 这里的 /uploads/ 对应 WebConfig 里的映射路径
            String url = "http://localhost:8080/uploads/" + fileName;

            System.out.println("文件上传成功，访问路径为：" + url);
            return Result.success(url);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败");
        }
    }
}