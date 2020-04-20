package com.mqdemo.mqdemo_with_spring_boot.component;

import com.mqdemo.mqdemo_with_spring_boot.config.MQCommonConfig;
import lombok.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Data
@Component
@ConfigurationProperties(prefix = "rocketmq-producer")
public abstract class MDWProducer {

    private String producerGroup;

    @Autowired
    private MQCommonConfig m_MQCommonConfig;
    private DefaultMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        /*
         * Instantiate with a producer group name.
         */
         producer = new DefaultMQProducer(producerGroup);

        /*
         * Specify name server addresses.
         * <p/>
         *
         * Alternatively, you may specify name server addresses via exporting environmental variable: NAMESRV_ADDR
         * <pre>
         * {@code
         * producer.setNamesrvAddr("name-server1-ip:9876;name-server2-ip:9876");
         * }
         * </pre>
         */
        producer.setNamesrvAddr(m_MQCommonConfig.getNamesrvAddrs());

        /*
         * Launch the instance.
         */
        producer.start();
    }

    @PreDestroy
    public void uninit()
    {
        /*
         * Shut down once the producer instance is not longer in use.
         */
        producer.shutdown();
    }

    public abstract void produceMsg() throws Exception;
}
