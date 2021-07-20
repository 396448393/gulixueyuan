package com.guli.oss.controller;

import com.guli.common.vo.R;
import com.guli.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "minio文件管理")
@CrossOrigin//跨域
@RestController
@RequestMapping("/admin/oss/file")
public class FileUploadController {
    @Autowired
    private FileService fileService;

    public R upload(
            @ApiParam(name = "file",value="文件",required = true)
            @RequestParam("file")MultipartFile file){
        String uploadUrl = fileService.upload(file);
        return R.ok().message("文件上传成功").data("url",uploadUrl);
    }
}
