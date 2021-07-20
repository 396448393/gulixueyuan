package com.guli.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 文件上传至minio
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
