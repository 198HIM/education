package com.voup.education.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.voup.education.bean.R;
import com.voup.education.entity.Course;
import com.voup.education.entity.CoursePage;
import com.voup.education.mapper.CourseMapper;
import com.voup.education.service.CourseService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("//course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseMapper courseMapper;

    //展示数据
    @RequestMapping("/show")
    public R show(@RequestBody CoursePage coursePage) {

        Page<Course> page = new Page<>(coursePage.getCurrentPage(), coursePage.getPageSize());
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        //查询操作
        queryWrapper.like("courseCode", coursePage.getCourse().getCourseCode());
        //填入数据
        courseMapper.selectPage(page, queryWrapper);
        //测试数据
        System.out.println("数据总数:" + page.getTotal());
        System.out.println("分页大小:" + page.getSize());
        System.out.println("当前页数:" + page.getCurrent());
        System.out.println("总页数:" + page.getPages());

        return R.success(page, "课程展示成功");
    }
}
