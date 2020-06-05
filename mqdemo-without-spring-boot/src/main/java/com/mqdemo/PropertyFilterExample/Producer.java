package com.mqdemo.PropertyFilterExample;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class Producer {
    public static void main(String[] args) {
        /*
         * Instantiate with a producer group name.
         */

        DefaultMQProducer producer = new DefaultMQProducer("self-test-topic-producer");

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
        producer.setNamesrvAddr("10.19.1.65:9876");

        /*
         * Launch the instance.
         */
        try {
            producer.start();

            try {

                /*
                 * Create a message instance, specifying topic, tag and message body.
                 */
                Message msgWithoutProperty = new Message("self-test-topic" /* Topic */,
                        "TagA" /* Tag */,
                        ("Message without property").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );

                Message msgWithProperty = new Message("self-test-topic" /* Topic */,
                        "TagA" /* Tag */,
                        ("Message with property").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );
                msgWithProperty.putUserProperty("testProperty", "exist");


                SendResult sendResult = producer.send(msgWithoutProperty);
                System.out.printf("%s%n", sendResult);

                sendResult = producer.send(msgWithProperty);
                System.out.printf("%s%n", sendResult);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }

            /*
             * Shut down once the producer instance is not longer in use.
             */
            producer.shutdown();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
