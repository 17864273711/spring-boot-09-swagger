package com.mqdemo.mqdemo_with_spring_boot.component;

import com.mqdemo.mqdemo_with_spring_boot.config.MQCommonConfig;
import lombok.Data;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "rocketmq-consumer")
public abstract class MDWConsumer {

    private String comsumerGroup;

    @Autowired
    private MQCommonConfig m_MQCommonConfig;
    private DefaultMQPushConsumer m_consumer;

    @PostConstruct
    private void init() throws MQClientException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        /*
         * Instantiate with specified consumer group name.
         */
         m_consumer = new DefaultMQPushConsumer(comsumerGroup);

        /*
         * Specify name server addresses.
         * <p/>
         *
         * Alternatively, you may specify name server addresses via exporting environmental variable: NAMESRV_ADDR
         * <pre>
         * {@code
         * consumer.setNamesrvAddr("name-server1-ip:9876;name-server2-ip:9876");
         * }
         * </pre>
         */

        m_consumer.setNamesrvAddr(m_MQCommonConfig.getNamesrvAddrs());

        /*
         * Specify where to start in case the specified consumer group is a brand new one.
         */
        m_consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        /*
         * Subscribe one more more topics to consume.
         */
        m_consumer.subscribe(m_MQCommonConfig.getTopic(), "*");

        /*
         *  Register callback to execute on arrival of messages fetched from brokers.
         */
        m_consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                return processMsg(msgs, context);
            }
        });

        /*
         *  Launch the consumer instance.
         */
        m_consumer.start();

        System.out.printf("Consumer Started.%n");
    }

    public abstract ConsumeConcurrentlyStatus processMsg(List<MessageExt> msgs, ConsumeConcurrentlyContext context);

    @PreDestroy
    private void uninit()
    {
        m_consumer.shutdown();
    }
}
