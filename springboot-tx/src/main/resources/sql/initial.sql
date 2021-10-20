drop
`tb_activity_order_task`;

CREATE TABLE `tb_activity_order_task`
(
    `id`           BIGINT(19,0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_name`    VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户名称' COLLATE 'utf8mb4_general_ci',
    `activity_id`  VARCHAR(50) NOT NULL DEFAULT '' COMMENT '活动id' COLLATE 'utf8mb4_general_ci',
    `task_id`      VARCHAR(20) NOT NULL DEFAULT '' COMMENT '任务ID' COLLATE 'utf8mb4_general_ci',
    `status`       VARCHAR(20) NOT NULL DEFAULT '' COMMENT '状态' COLLATE 'utf8mb4_general_ci',
    `ext`          VARCHAR(50) NULL COMMENT '订单号' COLLATE 'utf8mb4_general_ci',
    `updated_time` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_time` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `version`      INT(11) NOT NULL DEFAULT '0' COMMENT '版本号',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `activity_id` (`activity_id`, `task_id`, `user_name`) USING BTREE
) COMMENT='活动任务表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=4
;
