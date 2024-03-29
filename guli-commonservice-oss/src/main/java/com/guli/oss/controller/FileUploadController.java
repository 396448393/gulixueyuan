package com.guli.oss.controller;

import com.guli.common.vo.R;
import com.guli.oss.service.FileService;
import com.guli.oss.util.ConstantPropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "minio文件管理")
@CrossOrigin//跨域
@RestController
@RequestMapping("/admin/oss/file")
public class FileUploadController {
    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("upload")
    public R upload(
            @ApiParam(name = "file",value="文件",required = true)
            @RequestParam("file")MultipartFile file,
            @ApiParam(name = "host",value = "文件上传路径",required = false)
            @RequestParam(value = "host",required = false) String host){
        if(!StringUtils.isEmpty(host)){
            ConstantPropertiesUtil.FILE_HOST=host;
        }
        String uploadUrl = fileService.upload(file);
        //返回R对象
        return R.ok().message("文件上传成功").data("url",uploadUrl);
    }
}
