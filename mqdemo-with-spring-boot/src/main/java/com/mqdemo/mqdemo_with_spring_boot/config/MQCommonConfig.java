package com.mqdemo.mqdemo_with_spring_boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rocketmq-common")
public class MQCommonConfig {
    private String namesrvAddrs;
    private String topic;
}
