package com.mqdemo.OrderedConsumeExample;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Consumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_4");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.setNamesrvAddr("10.19.1.65:9876");
        //consumer.subscribe("self-test-topic", "TagA || TagC || TagD");
        consumer.subscribe("self-test-topic", "*");

        consumer.registerMessageListener(new MessageListenerOrderly() {

            Random rnd = new Random();
            AtomicLong consumeTimes = new AtomicLong(0);

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs,
                                                       ConsumeOrderlyContext context) {
                context.setAutoCommit(false);
                // System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                printMsg(msgs);
                try {
                    Thread.sleep(rnd.nextInt(10));
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                this.consumeTimes.incrementAndGet();
//                if ((this.consumeTimes.get() % 2) == 0) {
//                    return ConsumeOrderlyStatus.SUCCESS;
//                } else if ((this.consumeTimes.get() % 3) == 0) {
//                    return ConsumeOrderlyStatus.ROLLBACK;
//                } else if ((this.consumeTimes.get() % 4) == 0) {
//                    return ConsumeOrderlyStatus.COMMIT;
//                }  else if ((this.consumeTimes.get() % 5) == 0) {
//                    context.setSuspendCurrentQueueTimeMillis(3000);
//                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
//                }
                return ConsumeOrderlyStatus.SUCCESS;
            }

            public void printMsg(List<MessageExt> msgs) {
                for (int i = 0; i < msgs.size(); ++i) {
                    System.out.printf(i + ", " + (new String(msgs.get(i).getBody())) + "%n");
                }
            }
        });

        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}
