package com.yfaan.xjob.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {
    /**
     * OSS endpoint, e.g. https://oss-cn-hangzhou.aliyuncs.com
     */
    private String endpoint;
    /**
     * Access key id for OSS
     */
    private String accessKeyId;
    /**
     * Access key secret for OSS
     */
    private String accessKeySecret;
    /**
     * Target bucket name
     */
    private String bucketName;
    /**
     * Public domain used to access uploaded objects (e.g. https://bucket.oss-cn-hangzhou.aliyuncs.com)
     */
    private String publicDomain;
}
