package com.mjc.controller;

import com.mjc.Result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Tag(name = "上传文件相关接口")
@RestController
public class FileUploadController {

    // 读取配置文件中的存储路径: D:/Code/Hometown/images/
    @Value("${hometown.file.upload-path}")
    private String baseUploadPath;

    // 读取访问前缀: /images/
    @Value("${hometown.file.access-path}")
    private String accessPath;

    //@PostMapping("/upload")
    @Operation(summary = "上传文件(本地存储)")
    public Result<String> upload(MultipartFile file, HttpServletRequest request) {
        log.info("文件上传开始：{}", file.getOriginalFilename());

        if (file.isEmpty()) {
            return Result.error("上传失败，文件为空");
        }

        try {
            // 1. 获取原始文件名和后缀
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 2. 生成新文件名 (UUID防止重名)
            String newFileName = UUID.randomUUID().toString().replace("-", "") + extension;

            // 3. 生成日期目录 (例如: 2023/12/19)
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            // 4. 拼接最终的硬盘存储路径
            // 结果类似: D:/Code/Hometown/images/2023/12/19/
            File uploadDir = new File(baseUploadPath + datePath);

            // 如果目录不存在，自动创建
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 5. 保存文件到硬盘
            // 最终文件路径: D:/Code/Hometown/images/2023/12/19/xxx-xxx.jpg
            file.transferTo(new File(uploadDir, newFileName));

            // 6. 拼接返回给前端的访问URL
            // 获取当前服务器的 IP 和 端口 (例如 http://localhost:8080)
            String scheme = request.getScheme(); // http
            String serverName = request.getServerName(); // localhost
            int serverPort = request.getServerPort(); // 8080

            // 拼接基础URL: http://localhost:8080/images/
            String baseUrl = scheme + "://" + serverName + ":" + serverPort + accessPath;

            // 最终URL: http://localhost:8080/images/2023/12/19/xxx-xxx.jpg
            String fileUrl = baseUrl + datePath + "/" + newFileName;

            log.info("文件上传成功，访问路径：{}", fileUrl);
            return Result.success(fileUrl);

        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传发生错误");
        }
    }
}