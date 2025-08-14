package com.ijinge.delaypay.controller;

import com.ijinge.delaypay.entity.ApiResponse;
import com.ijinge.delaypay.exception.BizException;
import com.ijinge.delaypay.mapper.OrderMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate; // 后面可用来取消延迟消息

    // 支付回调,模拟支付成功后的处理接口
    @PostMapping("/callback")
    public ApiResponse<String> payCallback(@RequestParam Long orderId) {
        // 数据库乐观锁 实现支付回调接口的幂等性
        int rows = orderMapper.markPaid(orderId);
        if (rows == 0) {
            throw new BizException(400, "订单不存在或状态异常");
        }
        // 支付成功后，可在此处把订单加入“30天未发货”检查任务
        return ApiResponse.success("支付成功");
    }
}
