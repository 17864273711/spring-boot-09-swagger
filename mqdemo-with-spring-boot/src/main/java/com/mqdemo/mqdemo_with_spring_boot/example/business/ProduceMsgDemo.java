package com.mqdemo.mqdemo_with_spring_boot.example.business;

import com.mqdemo.mqdemo_with_spring_boot.component.MDWProducer;
import com.mqdemo.mqdemo_with_spring_boot.config.MQCommonConfig;
import lombok.Data;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProduceMsgDemo extends MDWProducer {
    @Autowired
    private MQCommonConfig m_MQCommonConfig;
    private int msgCount = 0;

    @Override
    public void produceMsg()
    {
        try {
            for (int i = 0; i < msgCount; ++ i)
            {
                /*
                 * Create a message instance, specifying topic, tag and message body.
                 */
                Message msg = new Message(m_MQCommonConfig.getTopic() /* Topic */,
                        "TagA" /* Tag */,
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );
                getProducer().send(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        return "produced " + msgCount + " messages!";
    }
}
