package com.mqdemo;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.*;

public class TransactionProducer {

    public static void main(String[] args) {
        /*
         * Instantiate with a producer group name.
         */

        TransactionMQProducer producer = new TransactionMQProducer("self_test_topic_producer");

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

        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        producer.setExecutorService(executorService);
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                // tell broker do not commit nor rollback the message.
                return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                // tell broker to commit the message.
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        /*
         * Launch the instance.
         */
        try {
            producer.start();

            for (int i = 0; i < 1; i++) {
                try {

                    /*
                     * Create a message instance, specifying topic, tag and message body.
                     */
                    Message msg = new Message("self-test-topic" /* Topic */,
                            "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                    );

                    // use transaction-version send method.
                    TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, null);

                    System.out.printf("%s%n", sendResult);
                    // the default value of transactionCheckInterval is 60000ms, which is 1min.
                    // sleep 2min to ensure that checkLocalTransaction will be called.
                    Thread.sleep(120000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        } finally {
            /*
             * Shut down once the producer instance is not longer in use.
             */
            producer.shutdown();
        }
    }

}
