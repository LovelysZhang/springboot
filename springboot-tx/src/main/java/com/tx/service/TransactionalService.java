package com.tx.service;

import com.tx.model.ActivityOrderTask;
import com.tx.repository.dao.ActivityOrderTaskDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 事务代码示例
 *
 * @author lovely
 * on 2021/9/23
 */
@Service
public class TransactionalService {

    @Resource
    ActivityOrderTaskDao activityOrderTaskDao;

    @Transactional(rollbackFor = Exception.class, value = "memberTransactionManager")
    public void insert() {
        ActivityOrderTask task1 = new ActivityOrderTask().setActivityId("activityid").setTaskId("taskid").setUserName("aaa").setStatus("sss");
        activityOrderTaskDao.insert(task1);

        // ActivityOrderTask task2 = new ActivityOrderTask().setActivityId("1").setUserName("aaa").setStatus("sss");
        // activityOrderTaskDao.insert(task2);
    }


}
