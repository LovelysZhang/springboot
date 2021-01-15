package com.core.controller;

import com.core.controller.vo.UserInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 参数校验
 *
 * @author lovely
 * @version 1.0
 * on 2021/1/13
 */
@RestController
@RequestMapping("/api/validation")
public class DataValidationController {

    /**
     * http://localhost:8080/api/validation/get?userId=&userName=aaa&account=bbb&password=ccc
     *
     * @param userInfoVo
     * @return
     */
    @GetMapping("/get")
    public Object get(@Valid UserInfoVo userInfoVo) {
        return userInfoVo;
    }
}
