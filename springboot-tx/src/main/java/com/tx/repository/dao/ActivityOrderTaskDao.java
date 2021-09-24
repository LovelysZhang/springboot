package com.tx.repository.dao;

import com.tx.model.ActivityOrderTask;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author lovely
 * on 2021/9/24
 */
public interface ActivityOrderTaskDao extends Mapper<ActivityOrderTask>, InsertListMapper<ActivityOrderTask> {

}
