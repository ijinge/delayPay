package com.ijinge.delaypay.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    // 普通交换机 - 处理业务
    @Bean
    public TopicExchange orderEventExchange() {
        return new TopicExchange("order.event");
    }

    // 延迟交换机（也可直接用普通交换机）
    @Bean
    public DirectExchange orderDelayExchange() {
        return new DirectExchange("order.delay");
    }

    // 延迟队列（15分钟TTL）
    @Bean
    public Queue orderUnpaidDelayQueue() {
        Map<String, Object> args = new HashMap<>();
        // 死信队列 绑定order.even交换机 (超时后自动转发至 order.event 交换机)
        args.put("x-dead-letter-exchange", "order.event");
        // 绑定 交换机下的队列
        args.put("x-dead-letter-routing-key", "order.unpaid.timeout");
//        args.put("x-message-ttl", 15 * 60 * 1000); // 15min
        args.put("x-message-ttl", 15 * 1000); // 15s
        return new Queue("order.unpaid.delay", true, false, false, args);
    }

    // 真正消费队列
    @Bean
    public Queue orderUnpaidTimeoutQueue() {
        return new Queue("order.unpaid.timeout", true);
    }

    // 绑定
    @Bean
    public Binding bindUnpaidDelay() {
        return BindingBuilder.bind(orderUnpaidDelayQueue()).to(orderDelayExchange()).with("order.unpaid.delay");
    }

    @Bean
    public Binding bindUnpaidTimeout() {
        return BindingBuilder.bind(orderUnpaidTimeoutQueue()).to(orderEventExchange()).with("order.unpaid.timeout");
    }
}
