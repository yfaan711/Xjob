package com.yfaan.xjob.config;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        // 1.创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:63790").setPassword("123456").setDatabase(10);
        return Redisson.create(config);
    }
}
