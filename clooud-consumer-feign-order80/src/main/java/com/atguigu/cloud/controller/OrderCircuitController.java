package com.atguigu.cloud.controller;

import com.atguigu.cloud.api.PayFeignApi;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class OrderCircuitController {
    @Resource
    private PayFeignApi payFeignApi;

    @GetMapping("/feign/pay/circuit/{id}")
    //fallbackMethod = "myCircuitFallBack": 无法调用8001服务，服务降级，返回备用结果（兜底）
    @CircuitBreaker(name = "cloud-payment-service",fallbackMethod = "myCircuitFallBack")
    public String myCircuit(@PathVariable("id") Integer id){
        return payFeignApi.myCircuit(id);
    }

    //服务兜底方法
    public String myCircuitFallBack(Integer id ,Throwable throwable){
        return throwable.getMessage()+ "系统繁忙，请稍后重试~/(ToT)/~";
    }

//    /**
//     *(船的)舱壁,隔离
//     * @param id
//     * @return
//     */
//    @GetMapping(value = "/feign/pay/bulkhead/{id}")
//    @Bulkhead(name = "cloud-payment-service",fallbackMethod = "myBulkheadFallback",type = Bulkhead.Type.SEMAPHORE)
//    public String myBulkhead(@PathVariable("id") Integer id)
//    {
//        return payFeignApi.myBulkhead(id);
//    }
//    public String myBulkheadFallback(Throwable t)
//    {
//        return "myBulkheadFallback，隔板超出最大数量限制，系统繁忙，请稍后再试-----/(ㄒoㄒ)/~~";
//    }

    /**
     *(船的)舱壁,隔离线程池
     * @param id
     * @return
     */
    @GetMapping(value = "/feign/pay/bulkhead/{id}")
    @Bulkhead(name = "cloud-payment-service",fallbackMethod = "myBulkheadPoolFallback",type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<String> myBulkheadThreadPool(@PathVariable("id") Integer id)
    {
        System.out.println(Thread.currentThread().getName()+"开始");
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (Exception e) {
            throw new RuntimeException();
        }

        return CompletableFuture.supplyAsync(() ->  payFeignApi.myBulkhead(id) + "POOL");
    }
    public CompletableFuture<String> myBulkheadPoolFallback(Integer id , Throwable t)
    {
        return CompletableFuture.supplyAsync(
                () -> "ThreadPool，隔板超出最大数量限制，系统繁忙，请稍后再试-----/(ㄒoㄒ)/~~"
        );

    }


    //限流
    @GetMapping(value = "/feign/pay/ratelimit/{id}")
    @RateLimiter(name = "cloud-payment-service",fallbackMethod = "myRatelimitFallback")
    public String myBulkhead(@PathVariable("id") Integer id)
    {
        return payFeignApi.myRatelimit(id);
    }
    public String myRatelimitFallback(Integer id,Throwable t)
    {
        return "你被限流了，禁止访问/(ㄒoㄒ)/~~";
    }






}

