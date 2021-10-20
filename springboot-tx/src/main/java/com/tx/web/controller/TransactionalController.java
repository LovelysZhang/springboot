package com.tx.web.controller;

import com.tx.service.TransactionalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 事务失效测试
 *
 * @author lovely
 * on 2021/9/24
 */
@RestController
@RequestMapping("/api/trans")
public class TransactionalController {

    @Resource
    TransactionalService transactionalService;

    @RequestMapping("/add")
    public Object addActivityInfo() {
        transactionalService.insert();
        return true;
    }
}
