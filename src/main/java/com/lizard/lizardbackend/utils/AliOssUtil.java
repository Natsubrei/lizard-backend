package com.lizard.lizardbackend.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.exception.UtilRuntimeException;
import com.lizard.lizardbackend.properties.AliOssProperties;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Random;

/**
 * 阿里云OSS服务工具类
 */
@Slf4j
@AllArgsConstructor
public class AliOssUtil {
    private static String endpoint;
    private static String bucketName;
    private static String accessKeyId;
    private static String accessKeySecret;

    /**
     * 内部类：用于注入配置并初始化静态变量
     */
    @Component
    public static class AliOssUtilInitializer {
        private final AliOssProperties aliOssProperties;

        public AliOssUtilInitializer(AliOssProperties aliOssProperties) {
            this.aliOssProperties = aliOssProperties;
        }

        @PostConstruct
        public void init() {
            AliOssUtil.endpoint = aliOssProperties.getEndpoint();
            AliOssUtil.bucketName = aliOssProperties.getBucketName();
            AliOssUtil.accessKeyId = aliOssProperties.getAccessKeyId();
            AliOssUtil.accessKeySecret = aliOssProperties.getAccessKeySecret();
        }
    }

    /**
     * 文件上传
     * @param bytes 文件的字节数组
     * @param originalName 文件原名称
     * @return 访问文件的url
     */
    public static String upload(byte[] bytes, String originalName, String directory) {
        String objectName = generateUniqueName(originalName);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 文件上传
            ossClient.putObject(bucketName, directory + "/" + objectName, new ByteArrayInputStream(bytes));
            log.info("文件 " + objectName + " 上传成功");
        } catch (OSSException | ClientException oe) {
            throw new UtilRuntimeException(MessageConstant.FILE_UPLOAD_FAILED);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        //文件访问路径规则 https://bucketName.endpoint/prefix/objectName
        String url = "https://" + bucketName + "." + endpoint + "/" + directory + "/" + objectName;
        log.info("文件url为：{}", url);

        return url;
    }

    private static String generateUniqueName(String originalName) {
        if (originalName == null) {
            return null;
        }

        // 获取文件后缀
        int dotIndex = originalName.lastIndexOf('.');
        String fileExtension = originalName.substring(dotIndex);

        // 获取当前时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());

        // 生成一个0到9999之间的随机数
        Random random = new Random();
        int randomNum = random.nextInt(10000);

        // 连接以形成一个唯一的文件名称
        return timestamp + randomNum + fileExtension;
    }
}
