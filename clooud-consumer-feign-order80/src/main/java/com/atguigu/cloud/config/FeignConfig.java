package com.atguigu.cloud.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Retryer myRetry(){
       /* return Retryer.NEVER_RETRY;//默认openFeign不使用重试策略*/
        return new Retryer.Default(100,1,3);
    }

    @Bean
    Logger.Level feignLoggingLevel(){
        return Logger.Level.FULL;
    }
}
