package com.guli.oss.service.impl;


import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.oss.service.FileService;
import com.guli.oss.util.ConstantPropertiesUtil;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file) {
        String endPoint= ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String fileHost = ConstantPropertiesUtil.FILE_HOST;

        String url = null;
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(endPoint, accessKeyId, accessKeySecret);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if(isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);
            }
            InputStream inputStream = file.getInputStream();
            //构建日期路径：avatar/2019/02/26/文件名
            String filePath = new DateTime().toString("yyyy/MM/dd");
            //文件名：uuid.扩展名
            String original = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            String fileType = original.substring(original.lastIndexOf("."));
            String newName = fileName + fileType;
            String fileUrl = fileHost + "/" + filePath + "/" + newName;

            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject(bucketName,fileUrl,inputStream,new PutObjectOptions(file.getSize(),-1L));
            url = minioClient.getObjectUrl(bucketName,fileUrl);
        } catch(Exception e) {
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
        return url;
    }
}
