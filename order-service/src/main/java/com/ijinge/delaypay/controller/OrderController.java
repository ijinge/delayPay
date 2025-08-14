package com.ijinge.delaypay.controller;

import com.ijinge.delaypay.entity.ApiResponse;
import com.ijinge.delaypay.entity.Order;
import com.ijinge.delaypay.exception.BizException;
import com.ijinge.delaypay.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMapper orderMapper;

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
        return ApiResponse.success(order.getId());
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> getOrder(@PathVariable Long id) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BizException(404, "订单不存在");
        }
        return ApiResponse.success(order);
    }
}
