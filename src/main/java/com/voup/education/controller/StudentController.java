package com.voup.education.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.voup.education.bean.R;

import com.voup.education.entity.*;
import com.voup.education.mapper.CourseMapper;
import com.voup.education.mapper.SelectcourseMapper;
import com.voup.education.mapper.StudentMapper;
import com.voup.education.service.CourseService;
import com.voup.education.service.SelectcourseService;
import com.voup.education.service.StudentService;
import com.voup.education.utils.SHA1Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author voup
 * @since 2022-12-20
 */
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    SelectcourseService selectcourseService;

    @Autowired
    SelectcourseMapper selectcourseMapper;
    String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
    //效验密码
    String regEx2 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,12}$";


    @PostMapping("/show")
    public R show(@RequestBody StudentPage studentPage) {

        Page<Student> page = new Page<>(studentPage.getCurrentPage(), studentPage.getPageSize());
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        //查询操作
        queryWrapper.like("studentCode", studentPage.getStudent().getStudentCode());
        queryWrapper.like("studentName", studentPage.getStudent().getStudentName());
        //填入数据
        studentMapper.selectPage(page, queryWrapper);
        //测试数据
        System.out.println("数据总数:" + page.getTotal());
        System.out.println("分页大小:" + page.getSize());
        System.out.println("当前页数:" + page.getCurrent());
        System.out.println("总页数:" + page.getPages());

        return R.success(page, "学生信息展示成功");
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Student student) {
        if (studentService.removeById(student.getId())) {

            return R.success("", "删除学生信息成功");
        } else {
            return R.error("删除学生信息失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody Student student) {

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentCode", student.getStudentCode());
        //添加
        if (studentService.getOne(queryWrapper) == null) {
            //自增长teacherCode
            for (int i = 1; i < 9999; i++) {
                if (i < 10) {
                    student.setStudentCode("020420" + i);

                } else {
                    student.setStudentCode("02042" + i);
                }

                if (studentService.getOne(queryWrapper) == null) {
                    break;
                }
            }

            if (studentService.save(student)) {
                return R.success("", "学生添加成功");
            } else {
                return R.error("学生添加失败");
            }
        }
        //更新
        LambdaUpdateWrapper<Student> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (student.getPassword() != "") {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(student.getStudentName());
            if (m.find()) {
                return R.error("真实姓名或用户名不能含有特殊字符");
            }
            if (student.getStudentName().length() < 1 || student.getStudentName().length() > 10) {
                return R.error("名称必须包含1至10个字符");
            }

            if (!student.getPassword().matches(regEx2)) {
                return R.error("密码至少包含：大小写英文字母、数字、特殊符号,密码长度大于6位,小于12位");
            }

            lambdaUpdateWrapper.eq(Student::getStudentCode, student.getStudentCode())
                    .set(Student::getStudentName, student.getStudentName())
                    .set(Student::getStudentCode, student.getStudentCode())
                    .set(Student::getSex, student.getSex())
                    .set(Student::getCollege, student.getCollege())
                    .set(Student::getPassword, SHA1Util.sha1(student.getPassword()));


        } else {
            student.setPassword(studentService.getOne(new QueryWrapper<Student>().eq("studentCode", student.getStudentCode())).getPassword());
            lambdaUpdateWrapper.eq(Student::getStudentCode, student.getStudentCode())
                    .set(Student::getStudentName, student.getStudentName())
                    .set(Student::getStudentCode, student.getStudentCode())
                    .set(Student::getSex, student.getSex())
                    .set(Student::getCollege, student.getCollege())
                    .set(Student::getPassword, student.getPassword());
        }

        //更新中的用户名密码效验
        if (studentService.update(null, lambdaUpdateWrapper)) {
            return R.success("", "学生信息更新成功");
        } else {
            return R.error("学生信息更新失败");
        }
    }

    //学生查询自己所选课程
    @PostMapping("/selectCourse")
    public R selectCourse(@RequestBody StudentPage studentPage) {
        //效验
        if (studentPage.getStudent().getStudentCode() == "") {
            return R.error("没有提交正确的学生编号");
        }
        //在selectCourse表中查询出改学生所选的数据(全部数据,需要筛选)
        QueryWrapper<Selectcourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentCode", studentPage.getStudent().getStudentCode());
        List<Selectcourse> list = selectcourseMapper.selectList(queryWrapper);
        int count = selectcourseMapper.selectCount(queryWrapper);

        //进行有用数据的筛选,放入list中
        List<String> resultList = new ArrayList<>();
        list.forEach(index -> {
            resultList.add(index.getCourseCode());
        });

        //分页查询
        Page<Course> page = new Page<>(studentPage.getCurrentPage(), studentPage.getPageSize());
        QueryWrapper<Course> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("courseCode", resultList);

        courseMapper.selectPage(page, queryWrapper1);

        return R.success(page, "成功");
    }

    //学生修改个人信息
    @PostMapping("/editMyself")
    public R editMyself(@RequestBody Student student) {


        //更新
        LambdaUpdateWrapper<Student> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (studentService.getOne(lambdaUpdateWrapper.eq(Student::getStudentCode, student.getStudentCode())) == null) {
            return R.error("系统错误,学生编号不存在,请重新登录再尝试");
        }
        if (student.getPassword() != "") {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(student.getStudentName());
            if (m.find()) {
                return R.error("真实姓名或用户名不能含有特殊字符");
            }
            if (student.getStudentName().length() < 1 || student.getStudentName().length() > 10) {
                return R.error("名称必须包含1至10个字符");
            }

            if (!student.getPassword().matches(regEx2)) {
                return R.error("密码至少包含：大小写英文字母、数字、特殊符号,密码长度大于6位,小于12位");
            }

            lambdaUpdateWrapper.eq(Student::getStudentCode, student.getStudentCode())
                    .set(Student::getStudentName, student.getStudentName())
                    .set(Student::getStudentCode, student.getStudentCode())
                    .set(Student::getSex, student.getSex())
                    .set(Student::getCollege, student.getCollege())
                    .set(Student::getPassword, SHA1Util.sha1(student.getPassword()));


        } else {
            student.setPassword(studentService.getOne(new QueryWrapper<Student>().eq("studentCode", student.getStudentCode())).getPassword());
            lambdaUpdateWrapper.eq(Student::getStudentCode, student.getStudentCode())
                    .set(Student::getStudentName, student.getStudentName())
                    .set(Student::getStudentCode, student.getStudentCode())
                    .set(Student::getSex, student.getSex())
                    .set(Student::getCollege, student.getCollege())
                    .set(Student::getPassword, student.getPassword());
        }

        //更新中的用户名密码效验
        if (studentService.update(null, lambdaUpdateWrapper)) {
            Student tempStudent = studentService.getOne(new QueryWrapper<Student>().eq("studentCode", student.getStudentCode()));
            return R.success(tempStudent, "学生信息更新成功");
        } else {
            return R.error("学生信息更新失败");
        }
    }

    @PostMapping("/getCourse")
    public R getCourse(@RequestBody Selectcourse selectcourse) {
        QueryWrapper<Selectcourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentCode", selectcourse.getStudentCode());
        queryWrapper.eq("courseCode", selectcourse.getCourseCode());
        if (selectcourseService.getOne(queryWrapper)==null){
            if (selectcourseService.save(selectcourse)) {
                return R.success("", "学生选课成功");
            } else {
                return R.error("系统错误,学生选课失败");
            }
        }else{
            return R.error("你已经选择这门课程了");
        }

    }

    //学生取消选课
    @PostMapping("/delCourse")
    public R delCourse(@RequestBody Selectcourse selectcourse) {

        QueryWrapper<Selectcourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentCode", selectcourse.getStudentCode());
        queryWrapper.eq("courseCode", selectcourse.getCourseCode());

        if (selectcourseService.remove(queryWrapper)) {
            return R.success("", "学生取消选课成功");
        } else {
            return R.error("系统错误,学生取消失败");
        }
    }

}
