package com.lim.controller;

import com.lim.result.Result;
import com.lim.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        try {
            String filePath = aliOssUtil.upload(file);
            return Result.success(filePath);
        } catch (Exception e) {
            return Result.error("文件上传失败");
        }
    }
}
