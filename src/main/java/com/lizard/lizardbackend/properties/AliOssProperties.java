package com.lizard.lizardbackend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "ali-oss")
public class AliOssProperties {
    /**
     * 地域节点
     */
    private String endpoint;

    /**
     * 存储空间名称
     */
    private String bucketName;

    /**
     * 访问密钥id，用于标识用户
     */
    private String accessKeyId;

    /**
     * 访问密钥secret，用于验证用户的密钥
     */
    private String accessKeySecret;
}
