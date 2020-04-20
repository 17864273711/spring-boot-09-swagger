package com.mqdemo.mqdemo_with_spring_boot.example.business;

import com.mqdemo.mqdemo_with_spring_boot.component.MDWConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Component
public class ConsumeMsgDemo extends MDWConsumer {

    private LinkedList<String> m_lastFiveMsgList = new LinkedList<String>();

    @Override
    public ConsumeConcurrentlyStatus processMsg(List<MessageExt> msgs, ConsumeConcurrentlyContext context)
    {
        for (int i = 0; i < msgs.size(); ++ i) {
            m_lastFiveMsgList.add(new String(msgs.get(i).getBody()));
        }

        while (m_lastFiveMsgList.size() > 5)
            m_lastFiveMsgList.removeFirst();

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    public String getLastFiveMsg()
    {
        StringBuilder strBuilder = new StringBuilder();

        for (String str : m_lastFiveMsgList)
        {
            strBuilder.append(str);
            strBuilder.append("</br>");
        }
        return strBuilder.toString();
    }
}
