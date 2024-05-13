package com.atguigu.cloud.controller;


import cn.hutool.core.date.DateUtil;
import com.atguigu.cloud.service.FlowLimitService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class FlowLimitController {
    @Resource
    private FlowLimitService flowLimitService;
    @GetMapping("/A")
    public String testA()
    {
        return "------testA------";
    }

    @GetMapping("/B")
    public String testB()
    {
        System.out.println("B");
        return "------testB------";
    }

    @GetMapping("/C")
    public String testC()
    {
        flowLimitService.common();
        return "------testC------";
    }
    @GetMapping("/D")
    public String testD()
    {
        flowLimitService.common();
        return "------testD------";
    }
    @GetMapping("/testE")
    public String testE()
    {
        System.out.println(/*System.currentTimeMillis()*/DateUtil.now() +"      testE,排队等待");
        return "------testE------";
    }
    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") Integer id) throws InterruptedException {
        if(id == 1){
            TimeUnit.SECONDS.sleep(3);
        }
        flowLimitService.waitRequest();
        return "success";
    }

    /**
     * 新增熔断规则-异常比例
     * @return
     */
    @GetMapping("/testG")
    public String testG()
    {
        System.out.println("----测试:新增熔断规则-异常比例 ");
        int age = 10/0;
        return "------testG,新增熔断规则-异常比例 ";
    }

}
