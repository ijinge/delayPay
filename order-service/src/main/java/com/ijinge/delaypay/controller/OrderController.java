package com.ijinge.delaypay.controller;

import com.ijinge.delaypay.entity.ApiResponse;
import com.ijinge.delaypay.entity.Order;
import com.ijinge.delaypay.exception.BizException;
import com.ijinge.delaypay.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMapper orderMapper;

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/create")
    public ApiResponse<Long> createOrder(@RequestParam Long userId,
                                         @RequestParam Long productId,
                                         @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException(404,"订单金额必须大于0");
        }
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setAmount(amount);
        orderMapper.insert(order);
        // 发送 15min 延迟消息
        // 发送延迟消息（进入 TTL 队列）
        rabbitTemplate.convertAndSend("order.delay", "order.unpaid.delay", order.getId());
        return ApiResponse.success(order.getId());
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> getOrder(@PathVariable Long id) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BizException(404, "订单不存在");
        }
        if(order.getStatus().equals("CLOSED")){
            throw new BizException(404, "订单已超时，请重新下单");
        }
        // 加入发货检查定时任务
        return ApiResponse.success(order);
    }

    @PostMapping("/ship")
    public ApiResponse<String> shipOrder(@RequestParam Long orderId) {
        int rows = orderMapper.markShipped(orderId);
        if (rows == 0) {
            throw new BizException(400, "订单不存在或状态不允许发货");
        }
        // 发货成功后，可在此处把订单加入“14天未收货”检查任务
        return ApiResponse.success("发货成功");
    }
}
