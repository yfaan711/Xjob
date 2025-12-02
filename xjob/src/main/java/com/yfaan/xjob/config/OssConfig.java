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
        if (StrUtil.hasBlank(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret())) {
            throw new IllegalStateException("OSS configuration is incomplete. Please check aliyun.oss settings.");
        }
        return new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret()
        );
    }
}
