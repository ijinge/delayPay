package com.ijinge.delaypay.listener;

import com.ijinge.delaypay.entity.Order;
import com.ijinge.delaypay.mapper.OrderMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "order.unpaid.timeout")
@Log4j2
public class OrderUnpaidTimeoutListener {

    @Autowired
    private OrderMapper orderMapper;

    @RabbitHandler
    public void handle(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order == null || !"PENDING".equals(order.getStatus())) {
            return;
        }
        orderMapper.updateStatus(orderId, "CLOSED");
        log.info("订单未支付超时关闭：{}",orderId);
    }
}
