package com.atguigu.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RestLimitController {
    @GetMapping("/rateLimit/byUrl")
    public String byUrl() {
        return "按rest地址限流测试OK";
    }

    @GetMapping("/rateLimit/byResource")
    @SentinelResource(value = "resourceLimit"
            , blockHandler = "handlerResource"/*, fallback = "fallbackFunction"*/)
    public String Resource() {
        return "resourceLimit";
    }
    @GetMapping("/rateLimit/{id}")
    @SentinelResource(value = "resourceError"
            , blockHandler = "handlerResource", fallback = "fallbackFunction")
    public String ResourceError(@PathVariable("id") Integer id) {
        int e;
        if (id == 0) {
            throw new RuntimeException("zero");
        }
        return "resourceLimitError" ;
    }
    public String handlerResource(@PathVariable("id") Integer id, BlockException blockException) {
        return "sentinel服务限流";
    }
    public String fallbackFunction(@PathVariable("id") Integer id, Throwable throwable) {
        return "program error, reason = " + throwable.getMessage();
    }
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "dealHandler_testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,

                             @RequestParam(value = "p2",required = false) String p2){
        return "------testHotKey";
    }
    public String dealHandler_testHotKey(String p1,String p2,BlockException exception)
    {
        return "-----dealHandler_testHotKey";
    }
}
