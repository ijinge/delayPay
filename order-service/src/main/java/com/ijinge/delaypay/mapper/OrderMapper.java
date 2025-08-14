package com.ijinge.delaypay.mapper;

import com.ijinge.delaypay.entity.Order;
import org.apache.ibatis.annotations.*;

import java.time.Duration;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO t_order(user_id, product_id, amount, status) " +
            "VALUES(#{userId}, #{productId}, #{amount}, 'PENDING')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order order);

    @Select("SELECT * FROM t_order WHERE id = #{id}")
    Order findById(Long id);

    @Update("update t_order set status = #{status} where id=#{id};")
    boolean updateStatus(Long id, String status);

    // 支付订单
    @Update("UPDATE t_order SET status='PAID', paid_time=NOW() WHERE id=#{id} AND status='PENDING'")
    int markPaid(@Param("id") Long id);

    // 发货
    @Update("UPDATE t_order SET status='SHIPPED', shipped_time=NOW() WHERE id=#{id} AND status='PAID'")
    int markShipped(@Param("id") Long id);

}
