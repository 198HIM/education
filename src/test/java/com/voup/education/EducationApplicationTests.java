package com.voup.education;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.voup.education.entity.Course;
import com.voup.education.mapper.CourseMapper;
import com.voup.education.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
@Slf4j
class EducationApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseMapper courseMapper;

    @Test
    void contextLoads() throws SQLException {
        log.info("数据库链接"+dataSource.getConnection().toString());
    }


    @Test
    void pageTest() {
        Page<Course> page = new Page<>(2,3);
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacherCode", "1001");
        courseMapper.selectPage(page, queryWrapper);
        System.out.println(page);

    }

   



}
