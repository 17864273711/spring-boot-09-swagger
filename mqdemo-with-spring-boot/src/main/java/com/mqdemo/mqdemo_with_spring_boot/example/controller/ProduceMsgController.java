package com.mqdemo.mqdemo_with_spring_boot.example.controller;

import com.mqdemo.mqdemo_with_spring_boot.example.business.ProduceMsgDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProduceMsgController {

    @Autowired
    private ProduceMsgDemo m_produceMsgDemo;

    @RequestMapping("/produceMsg")
    public String produceMsg(@RequestParam(value = "count") int msgCount) {
        m_produceMsgDemo.setMsgCount(msgCount);

        try {
            m_produceMsgDemo.produceMsg();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return m_produceMsgDemo.getResult();
    }
}
