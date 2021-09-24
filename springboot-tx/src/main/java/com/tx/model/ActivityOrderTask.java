package com.tx.model;

import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author lovely
 * on 2021/9/24
 */
@Data
@Accessors(chain = true)
@Table(name = "tb_activity_order_task")
@NameStyle(Style.camelhumpAndLowercase)
public class ActivityOrderTask {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    /**
     * '活动ID'
     */
    private String activityId;
    /**
     * '任务ID'
     */
    private String taskId;
    /**
     * '状态'
     */
    private String status;
    /**
     * '用户名'
     */
    private String userName;

    /**
     * 订单号
     */
    private String ext;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version = 0;


    private Date updatedTime;
}
