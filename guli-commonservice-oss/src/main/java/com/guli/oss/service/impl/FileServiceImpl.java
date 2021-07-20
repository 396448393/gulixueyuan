package com.guli.oss.service.impl;

import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.oss.service.FileService;
import com.guli.oss.util.ConstantPropertiesUtil;
import io.minio.*;
import io.minio.errors.MinioException;
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

        try {
            // 创建 MinIO 客户端.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(endPoint)
                            .credentials(accessKeyId, accessKeySecret)
                            .build();

            // 如果bucket不存在，则创建  bucketName:bucket名称
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // 创建bucket
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
//                System.out.println("Bucket 'asiatrip' already exists.");
            }

            //获取上传文件流
            InputStream inputStream = file.getInputStream();

            //构建日期路径：avatar/2019/02/26/文件名
            String filePath = new DateTime().toString("yyyy/MM/dd");

            //文件名：uuid.扩展名
            String original = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            String fileType = original.substring(original.lastIndexOf("."));
            String newName = fileName + fileType;
            String fileUrl = fileHost + "/" + filePath + "/" + newName;
            long fileSize = file.getSize();
            // Upload known sized input stream.
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(fileUrl).stream(
                            inputStream, fileSize, -1)
                            .contentType(fileType)
                            .build());

        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
        return null;
    }
}
