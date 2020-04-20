package com.mqdemo.mqdemo_with_spring_boot.example.controller;

import com.mqdemo.mqdemo_with_spring_boot.example.business.ConsumeMsgDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowLastFiveMsgController {

    @Autowired
    private ConsumeMsgDemo m_consumeMsgDemo;

    @RequestMapping("/showLastFiveMsg")
    public String showLastFiveMsg() {
        return m_consumeMsgDemo.getLastFiveMsg();
    }
}
