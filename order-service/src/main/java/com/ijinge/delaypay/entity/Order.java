package com.ijinge.delaypay.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName(value = "t_order")
public class Order {
    @TableId
    private Long id;
    private Long userId;
    private Long productId;
    private BigDecimal amount;
    private String status;

}