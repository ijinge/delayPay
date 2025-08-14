USE order_db;

CREATE TABLE `t_order` (
                           `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                           `user_id` BIGINT NOT NULL,
                           `product_id` BIGINT NOT NULL,
                           `amount` DECIMAL(10,2) NOT NULL,
                           `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                           `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
                           `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);