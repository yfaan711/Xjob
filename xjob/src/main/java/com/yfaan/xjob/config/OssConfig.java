package com.yfaan.xjob.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import cn.hutool.core.util.StrUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

    @Bean
    public OSS ossClient(OssProperties ossProperties) {
        // OSS 连接所需配置必须完整，否则无法构建客户端
        if (StrUtil.hasBlank(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret())) {
            throw new IllegalStateException("OSS configuration is incomplete. Please check aliyun.oss settings.");
        }
        // 创建并返回 OSS 客户端实例，供上传头像时复用
        return new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret()
        );
    }
}
