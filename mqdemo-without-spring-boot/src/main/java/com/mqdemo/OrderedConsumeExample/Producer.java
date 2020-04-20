package com.mqdemo.OrderedConsumeExample;


import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

public class Producer {
    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("self-test-topic-producer");
        producer.setNamesrvAddr("10.19.1.65:9876");
        //Launch the instance.
        producer.start();
        String[] steps = new String[]{"StepA", "StepB", "StepC", "StepD", "StepE"};
        for (int orderId = 0; orderId < 10; ++orderId) {
            for (int stepId = 0; stepId < 5; ++stepId) {
                //Create a message instance, specifying topic, tag and message body.
                Message msg = new Message("self-test-topic", steps[stepId % steps.length], "KEY" + (orderId * 10 + stepId),
                        ("OrderId " + orderId + " , stepId " + stepId).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer id = (Integer) arg;
                        int index = id % mqs.size();
                        return mqs.get(index);
                    }
                }, orderId);

                System.out.printf("%s%n", sendResult);
            }
        }
        //server shutdown
        producer.shutdown();
    }
}
