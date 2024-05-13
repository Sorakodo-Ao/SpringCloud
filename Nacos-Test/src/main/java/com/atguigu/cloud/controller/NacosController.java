package com.atguigu.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class NacosController {
/*    @Value("${LOVER.Name}")
    private String name;*/
    @Value("${nacos.name}")
    private String name;


    @GetMapping("/get/name")
    public String getName(){
        return name;
    }
}
