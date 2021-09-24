package com.tx.repository;

import com.google.common.collect.Lists;
import com.tx.model.ActivityOrderTask;
import com.tx.repository.dao.ActivityOrderTaskDao;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lovely
 * on 2021/9/23
 */
@Repository
public class ActivityOrderTaskRepository {

    @Resource
    ActivityOrderTaskDao activityOrderTaskDao;

    public ActivityOrderTask queryUserTaskById(String activityId, String taskId, String userName) {
        Example exmaple = new Example(ActivityOrderTask.class);
        exmaple.createCriteria().andEqualTo("activityId", activityId)
                .andEqualTo("userName", userName)
                .andEqualTo("taskId", taskId);
        return activityOrderTaskDao.selectOneByExample(exmaple);
    }

    public List<ActivityOrderTask> queryUserTasks(String activityId, String userName) {
        Example example = new Example(ActivityOrderTask.class);
        example.createCriteria().andEqualTo("activityId", activityId).andEqualTo("userName", userName);
        return activityOrderTaskDao.selectByExample(example);
    }

    public List<ActivityOrderTask> queryProcessTaskUsers(String activityId) {
        List<Object> taskIds = Lists.newArrayList("target1", "target2", "target3");

        Example example = new Example(ActivityOrderTask.class);
        Example.Criteria process = example.createCriteria();
        process.andEqualTo("activityId", activityId)
                .andEqualTo("status", "PROCESSING");
        return activityOrderTaskDao.selectByExample(example);
    }
}
