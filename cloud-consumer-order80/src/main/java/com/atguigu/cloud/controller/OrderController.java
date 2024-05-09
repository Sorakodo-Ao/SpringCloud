package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Result;
import javax.xml.xpath.XPathEvaluationResult;

@Slf4j
@RestController
public class OrderController {

    //public static final String PaymentSrv_URL = "http://localhost:8001";//硬编码
    public static final String PaymentSrv_URL = "http://cloud-payment-service";//consul注册中心的微服务名称

    @Resource
    private RestTemplate restTemplate;



    @GetMapping("/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id){
        return restTemplate.getForObject(PaymentSrv_URL+"/pay/get/" + id,ResultData.class);
    }

    /*
        @PostMapping("/pay/add")
        public ResultData<String> add(@RequestBody Pay pay) {
            System.out.println(pay.toString());
            int affect = payService.add(pay);
            return ResultData.success("添加成功，返回值：" + affect);
        }
     */
    @GetMapping("/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO){
        return restTemplate.postForObject(PaymentSrv_URL+"/pay/add",payDTO,ResultData.class);
    }

    /*     @DeleteMapping("/pay/del/{id}")
     *     public ResultData<String> deletePay(@PathVariable("id") Integer id) {
     *         int affect = payService.delete(id);
     *         return ResultData.success("删除" + affect + "行");
     *     }
     */
    @GetMapping("/consumer/pay/del/{id}")
    public ResultData deletePay(@PathVariable("id") Integer id){
        restTemplate.delete(PaymentSrv_URL+"/pay/del/"+ id );
        return ResultData.success("删除成功");
    }
    /*     @PutMapping("/pay/update")
     *     public ResultData<String> updatePay(@RequestBody PayDTO payDTO) {
     *         Pay pay = new Pay();
     *         BeanUtils.copyProperties(payDTO,pay);
     *         int affect = payService.update(pay);
     *         return ResultData.success("修改" + affect + "行");
     *     }
     */

    @GetMapping("/consumer/pay/update")
    public ResultData updatePay(PayDTO payDTO){
        restTemplate.put(PaymentSrv_URL+"/pay/update",payDTO);
        return ResultData.success("修改成功");
    }

    @GetMapping("/consumer/pay/get/info")
    public ResultData getInfoByConsul(){
       return restTemplate.getForObject(PaymentSrv_URL+"/pay/get/info", ResultData.class);
    }


}

