package com.atguigu.cloud.controller;


import com.alibaba.fastjson2.JSON;
import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import com.atguigu.cloud.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class PayController {
    /*@Resource*/
    @Autowired
    private PayService payService;


    @PostMapping("/pay/add")
    public ResultData<String> add(@RequestBody Pay pay) {

        System.out.println(pay.toString());
        int affect = payService.add(pay);
        return ResultData.success("添加成功，返回值：" + affect);
    }

    @DeleteMapping("/pay/del/{id}")
    public ResultData<String> deletePay(@PathVariable("id") Integer id) {
        int affect = payService.delete(id);
        return ResultData.success("删除" + affect + "行");
    }


    @PutMapping("/pay/update")
    public ResultData<String> updatePay(@RequestBody PayDTO payDTO) {

        Pay pay = new Pay();
        BeanUtils.copyProperties(payDTO,pay);
        int affect = payService.update(pay);
        return ResultData.success("修改" + affect + "行");
    }


    @GetMapping("/pay/get/{id}")
    public ResultData<Pay> getById(@PathVariable("id") Integer id){
        if(id < 0){
            throw new RuntimeException("id is nagetive");
        }
        Pay pay = payService.getById(id);
        return ResultData.success(pay);
    }

    @GetMapping("/pay/all")
    public ResultData<List<Pay>> getAll(){
        List<Pay> all = payService.getAll();
        return ResultData.success(all);
    }

    @GetMapping("/pay/error")
    public ResultData<Integer> errorTest(){
        Integer integer = Integer.valueOf(200);
        try{
            int i = 10 / 0;

        }catch (Exception e){
            return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
        }
        return ResultData.success(integer);
    }

    @Value("${server.port}")
    private String port;

    @GetMapping("/pay/get/info")
    public ResultData getInfoByConsul(@Value("${atguigu.info}") String atguiguInfo){
       /* return "atguiguInfo：" + atguiguInfo *//*+ "port:" + port*//*;*/
        Map<String, Object> map = new HashMap<>();
        map.put("atguiguInfo" , atguiguInfo);
        map.put("port" , port);
        Object json = JSON.toJSON(map);
        return ResultData.success(json);
    }
}
