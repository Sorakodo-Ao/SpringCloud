package com.atguigu.cloud.MyGateway;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SetPathGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Component
public class MyFilterGatewayFilterFactory extends
        AbstractGatewayFilterFactory<MyFilterGatewayFilterFactory.Config> {

    public MyFilterGatewayFilterFactory() {
        super(MyFilterGatewayFilterFactory.Config.class);
    }
    @Override
    public GatewayFilter apply(MyFilterGatewayFilterFactory.Config config) {
        return new GatewayFilter() {
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                System.out.println(request.getQueryParams());

                System.out.println("进入自定义网关，status = " + config.getStatus() );
                if(request.getQueryParams().containsKey(config.getStatus())){
                    return chain.filter(exchange);
                }else {
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    return exchange.getResponse().setComplete();
                }
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("status");
    }

    @Data
    public static class Config{
        //设置一个状态属性
        private String status;
    }
}
