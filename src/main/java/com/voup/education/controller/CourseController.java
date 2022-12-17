package com.voup.education.controller;


import com.voup.education.bean.R;
import com.voup.education.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author voup
 * @since 2022-12-17
 */
@RestController
@RequestMapping("//course")
public class CourseController {
    @Autowired
    CourseService courseService;

    @RequestMapping("/show")
    public R show(){
        return R.success("","测试成功");
    }
}
