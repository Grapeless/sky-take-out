package com.lim.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.lim.properties.AliOssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.UUID;

@Component
public class AliOssUtil {

        @Autowired
        private AliOssProperties aliOSSProperties;

        /**
         * 实现上传图片到OSS
         */
        public String upload(MultipartFile file) throws Exception {
            // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
            EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
            // 获取上传的文件的输入流
            InputStream inputStream = file.getInputStream();
            // 避免文件覆盖
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

            //获取配置参数
            String endpoint = aliOSSProperties.getEndpoint();
            String region = aliOSSProperties.getRegion();
            String bucketName = aliOSSProperties.getBucketName();

            //上传文件到 OSS
            ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
            clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
            OSS ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(clientBuilderConfiguration)
                    .region(region)
                    .build();

            ossClient.putObject(bucketName, fileName, inputStream);

            //文件访问路径
            String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
            // 关闭ossClient
            ossClient.shutdown();
            return url;// 把上传到oss的路径返回
        }
}
