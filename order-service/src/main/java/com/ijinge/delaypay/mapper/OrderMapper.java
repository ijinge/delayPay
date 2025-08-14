package com.ijinge.delaypay.mapper;

import com.ijinge.delaypay.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO t_order(user_id, product_id, amount, status) " +
            "VALUES(#{userId}, #{productId}, #{amount}, 'PENDING')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order order);

    @Select("SELECT * FROM t_order WHERE id = #{id}")
    Order findById(Long id);
}
