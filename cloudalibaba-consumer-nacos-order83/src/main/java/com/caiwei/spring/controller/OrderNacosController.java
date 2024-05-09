package com.caiwei.spring.controller;

import com.alibaba.nacos.api.model.v2.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class OrderNacosController {
    @Resource
    private RestTemplate restTemplate;

//    private static String URL = "http://localhost:9001";
    @Value("${service-url.nacos-user-service}")
    private String URL;


    @GetMapping("/consumer/pay/nacos/{id}")
    public String getPayInfo(@PathVariable("id") Integer id){
        log.info("id = " + id);
        String result = restTemplate.getForObject(
                URL + "/pay/nacos/" + id,
                String.class,id);
        return "nacos consumer 调用 "+ result;
    }
}
