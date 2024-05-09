package com.atguigu.cloud.MyGateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MyGlobalFilter implements GlobalFilter, Ordered {
    //自定义filter(统计每个调用接口的运行时间)?全局调用！！！！！
    public static String BEGIN_TIME = "begin_time";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //调用接口前的开始时间
        exchange.getAttributes().put(BEGIN_TIME, System.currentTimeMillis());

        return chain.filter(exchange).then(Mono.fromRunnable(
                () -> {
                    Long beginTime = exchange.getAttribute(BEGIN_TIME);
                    if (beginTime != null) {
                        log.info("访问接口的主机" + exchange.getRequest().getURI().getHost());
                        log.info("访问接口的主机端口" + exchange.getRequest().getURI().getPort());
                        log.info("访问接口的URL" + exchange.getRequest().getURI().getPath());
                        log.info("访问接口的parameter" + exchange.getRequest().getURI().getRawQuery());
                        log.info("访问接口的时长" + (System.currentTimeMillis() - beginTime) +"毫秒");
                    }
                }
        ));
    }

    @Override
    public int getOrder() {
        return 0;//数字越小越优先
    }
}
