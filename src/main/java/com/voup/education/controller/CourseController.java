package com.voup.education.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.voup.education.bean.R;
import com.voup.education.entity.Course;
import com.voup.education.entity.CoursePage;
import com.voup.education.mapper.CourseMapper;
import com.voup.education.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    //展示数据,模糊查询
    @PostMapping("/show")
    public R show(@RequestBody CoursePage coursePage) {

        Page<Course> page = new Page<>(coursePage.getCurrentPage(), coursePage.getPageSize());
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        //查询操作
        queryWrapper.like("courseName", coursePage.getCourse().getCourseName());
        //填入数据
        courseMapper.selectPage(page, queryWrapper);
        //测试数据
        System.out.println("数据总数:" + page.getTotal());
        System.out.println("分页大小:" + page.getSize());
        System.out.println("当前页数:" + page.getCurrent());
        System.out.println("总页数:" + page.getPages());

        return R.success(page, "课程展示成功");
    }

    //删除
    @PostMapping("/delete")
    public R delete(@RequestBody Course course) {
        if (courseService.removeById(course.getId())) {

            return R.success("", "删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    //更新
    @PostMapping("/update")
    public R update(@RequestBody Course course) {
        LambdaUpdateWrapper<Course> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Course::getId, course.getId())
                .set(Course::getCourseName, course.getCourseName())
                .set(Course::getTeacherCode, course.getTeacherCode())
                .set(Course::getTime, course.getTime())
                .set(Course::getLocation, course.getLocation())
                .set(Course::getWeeks, course.getWeeks())
                .set(Course::getCourseClass, course.getCourseClass())
                .set(Course::getCredit, course.getCredit());

        if (courseService.update(null, lambdaUpdateWrapper)) {
            return R.success("", "更新成功");
        } else {
            return R.error("更新失败");
        }
    }

    //添加
    @PostMapping("/insert")
    public R insert(@RequestBody Course course) {
        //自增长courseCode
        for (int i = 0; i < 9999; i++) {
            course.setCourseCode(i + "");

            QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("courseCode", i + "");
            if (courseService.getOne(queryWrapper) == null) {
                break;
            }
        }

        if (courseService.save(course)) {
            return R.success("", "更新成功");
        } else {
            return R.error("更新失败");
        }
    }
}
