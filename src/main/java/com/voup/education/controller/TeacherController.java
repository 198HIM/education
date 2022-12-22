package com.voup.education.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.voup.education.bean.R;

import com.voup.education.entity.*;

import com.voup.education.mapper.StudentMapper;
import com.voup.education.mapper.TeacherMapper;
import com.voup.education.service.SelectcourseService;
import com.voup.education.service.StudentService;
import com.voup.education.service.TeacherService;
import com.voup.education.utils.SHA1Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author voup
 * @since 2022-12-17
 */

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("//teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    SelectcourseService selectcourseService;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    StudentService studentService;

    String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
    //效验密码
    String regEx2 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,12}$";

    @PostMapping("/show")
    public R show(@RequestBody TeacherPage teacherPage) {

        Page<Teacher> page = new Page<>(teacherPage.getCurrentPage(), teacherPage.getPageSize());
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();

        //查询操作
        queryWrapper.like("teacherName", teacherPage.getTeacher().getTeacherName());
        queryWrapper.like("teacherCode", teacherPage.getTeacher().getTeacherCode());
        //填入数据
        teacherMapper.selectPage(page, queryWrapper);
        //测试数据
        System.out.println("数据总数:" + page.getTotal());
        System.out.println("分页大小:" + page.getSize());
        System.out.println("当前页数:" + page.getCurrent());
        System.out.println("总页数:" + page.getPages());

        return R.success(page, "教师信息展示成功");
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Teacher teacher) {
        if (teacherService.removeById(teacher.getId())) {

            return R.success("", "删除教师信息成功");
        } else {
            return R.error("删除教师信息失败");
        }
    }

    //教师管理更新和添加
    @PostMapping("/update")
    public R update(@RequestBody Teacher teacher) {
        //添加
        if (teacherService.getOne(new QueryWrapper<Teacher>().eq("teacherCode", teacher.getTeacherCode())) == null) {
            //自增长teacherCode
            for (int i = 1; i < 9999; i++) {
                if (i < 10) {
                    teacher.setTeacherCode("200" + i);
                } else {
                    teacher.setTeacherCode("20" + i);
                }
                QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("teacherCode", teacher.getTeacherCode());
                if (teacherService.getOne(queryWrapper) == null) {
                    break;
                }
            }

            if (teacherService.save(teacher)) {
                return R.success("", "教师添加成功");
            } else {
                return R.error("教师添加失败");
            }
        }
        //更新
        LambdaUpdateWrapper<Teacher> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (teacher.getPassword()!=""){
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(teacher.getTeacherName());
            if (m.find()) {
                return R.error("真实姓名或用户名不能含有特殊字符");
            }
            if (teacher.getTeacherName().length() < 1 || teacher.getTeacherName().length() > 10) {
                return R.error("名称必须包含1至10个字符");
            }

            if (!teacher.getPassword().matches(regEx2)) {
                return R.error("密码至少包含：大小写英文字母、数字、特殊符号,密码长度大于6位,小于12位");
            }
            lambdaUpdateWrapper.eq(Teacher::getTeacherCode, teacher.getTeacherCode())
                    .set(Teacher::getTeacherName, teacher.getTeacherName())
                    .set(Teacher::getTeacherCode, teacher.getTeacherCode())
                    .set(Teacher::getSex, teacher.getSex())
                    .set(Teacher::getBirth, teacher.getBirth())
                    .set(Teacher::getBackground, teacher.getBackground())
                    .set(Teacher::getPosition, teacher.getPosition())
                    .set(Teacher::getEntryTime, teacher.getEntryTime())
                    .set(Teacher::getCollege, teacher.getCollege())
                    .set(Teacher::getPassword, SHA1Util.sha1(teacher.getPassword()));
        }else{
            teacher.setPassword(teacherService.getOne(new QueryWrapper<Teacher>().eq("teacherCode",teacher.getTeacherCode())).getPassword());
            lambdaUpdateWrapper.eq(Teacher::getTeacherCode, teacher.getTeacherCode())
                    .set(Teacher::getTeacherName, teacher.getTeacherName())
                    .set(Teacher::getTeacherCode, teacher.getTeacherCode())
                    .set(Teacher::getSex, teacher.getSex())
                    .set(Teacher::getBirth, teacher.getBirth())
                    .set(Teacher::getBackground, teacher.getBackground())
                    .set(Teacher::getPosition, teacher.getPosition())
                    .set(Teacher::getEntryTime, teacher.getEntryTime())
                    .set(Teacher::getCollege, teacher.getCollege())
                    .set(Teacher::getPassword, teacher.getPassword());
        }

        //更新中的用户名密码效验
        if (teacherService.update(null, lambdaUpdateWrapper)) {
            return R.success("", "教师信息更新成功");
        } else {
            return R.error("教师信息更新失败");
        }
    }

    @PostMapping("/editMyself")
    public R editMyself(@RequestBody Teacher teacher) {
        LambdaUpdateWrapper<Teacher> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (teacherService.getOne(lambdaUpdateWrapper.eq(Teacher::getTeacherCode, teacher.getTeacherCode())) == null) {
            return R.error("系统错误,教师编号不存在,请重新登录再尝试");
        }
        //更新
        if (teacher.getPassword()!=""){
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(teacher.getTeacherName());
            if (m.find()) {
                return R.error("真实姓名或用户名不能含有特殊字符");
            }
            if (teacher.getTeacherName().length() < 1 || teacher.getTeacherName().length() > 10) {
                return R.error("名称必须包含1至10个字符");
            }

            if (!teacher.getPassword().matches(regEx2)) {
                return R.error("密码至少包含：大小写英文字母、数字、特殊符号,密码长度大于6位,小于12位");
            }
            lambdaUpdateWrapper.eq(Teacher::getTeacherCode, teacher.getTeacherCode())
                    .set(Teacher::getTeacherName, teacher.getTeacherName())
                    .set(Teacher::getTeacherCode, teacher.getTeacherCode())
                    .set(Teacher::getSex, teacher.getSex())
                    .set(Teacher::getBirth, teacher.getBirth())
                    .set(Teacher::getBackground, teacher.getBackground())
                    .set(Teacher::getPosition, teacher.getPosition())
                    .set(Teacher::getEntryTime, teacher.getEntryTime())
                    .set(Teacher::getCollege, teacher.getCollege())
                    .set(Teacher::getPassword, SHA1Util.sha1(teacher.getPassword()));
        }else{
            teacher.setPassword(teacherService.getOne(new QueryWrapper<Teacher>().eq("teacherCode",teacher.getTeacherCode())).getPassword());
            lambdaUpdateWrapper.eq(Teacher::getTeacherCode, teacher.getTeacherCode())
                    .set(Teacher::getTeacherName, teacher.getTeacherName())
                    .set(Teacher::getTeacherCode, teacher.getTeacherCode())
                    .set(Teacher::getSex, teacher.getSex())
                    .set(Teacher::getBirth, teacher.getBirth())
                    .set(Teacher::getBackground, teacher.getBackground())
                    .set(Teacher::getPosition, teacher.getPosition())
                    .set(Teacher::getEntryTime, teacher.getEntryTime())
                    .set(Teacher::getCollege, teacher.getCollege())
                    .set(Teacher::getPassword, teacher.getPassword());
        }

        //更新中的用户名密码效验
        if (teacherService.update(null, lambdaUpdateWrapper)) {
            return R.success("", "教师信息更新成功");
        } else {
            return R.error("教师信息更新失败");
        }
    }

    @PostMapping("/getStudent")
    public R getCourse(@RequestBody CoursePage coursePage) {
        QueryWrapper<Selectcourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("courseCode", coursePage.getCourse().getCourseCode());


        List<Selectcourse> list = selectcourseService.list(queryWrapper);
        int count = selectcourseService.count(queryWrapper);
        if (count==0){
            return R.error("该课程没有学生报名");
        }
        List<String> resultList = new ArrayList<>();
        list.forEach(index ->{
            resultList.add(index.getStudentCode());
        });
        //分页查询
        Page<Student> page = new Page<>(coursePage.getCurrentPage(), coursePage.getPageSize());
        QueryWrapper<Student> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("studentCode", resultList);


        studentService.page(page, queryWrapper1);

        return R.success(page, "本课学生查询成功");
    }

    @PostMapping("/delMyStudent")
    public R delMyStudent(@RequestBody Selectcourse selectcourse) {
        if (selectcourseService.remove(new QueryWrapper<Selectcourse>().eq("studentCode", selectcourse.getStudentCode()).eq("courseCode", selectcourse.getCourseCode()))) {
            return R.success("","学生移除成功");
        }else{
            return R.error("学生移除失败");
        }
    }

}
