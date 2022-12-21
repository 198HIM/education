package com.voup.education.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.voup.education.bean.R;
import com.voup.education.entity.*;
import com.voup.education.service.AdminService;
import com.voup.education.service.StudentService;
import com.voup.education.service.TeacherService;
import com.voup.education.utils.SHA1Util;
import com.voup.education.utils.StringNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author HIM198
 * @Date 2022 12:45
 * @Description
 **/
@CrossOrigin(origins = "*",maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("//user")
public class UserController {
    @Autowired
    AdminService adminService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    StudentService studentService;

    //效验用户名
    String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
    //效验密码
    String regEx2 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,12}$";
    StringNumber stringNumber = new StringNumber();

    //用户登录
    @PostMapping("/login")
    public R login(@RequestBody LoginPower loginPower) {
        System.out.println("tCode:" + loginPower.gettCode());

        //管理员登录
        if (loginPower.gettCode().equals("01")) {
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("adminCode", loginPower.getName());
            Admin admin = adminService.getOne(queryWrapper);

            if (admin == null) {
                return R.error("管理员名称不存在");
            } else if (!SHA1Util.sha1(loginPower.getPassword()).equals(admin.getPassword())) {

                return R.error("管理员密码错误");
            }
            return R.success(admin, "欢迎回来,管理员" + admin.getAdminName());
        //教师登录
        } else if (loginPower.gettCode().equals("02")) {
            QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("teacherCode", loginPower.getName());
            Teacher teacher = teacherService.getOne(queryWrapper);
            if (teacher == null) {
                return R.error("教师名称不存在");
            } else if (!SHA1Util.sha1(loginPower.getPassword()).equals(teacher.getPassword())) {
                return R.error("教师密码错误");
            }
            return R.success(teacher, "欢迎回来" + teacher.getTeacherName());

        //学生登录
        } else if (loginPower.gettCode().equals("03")) {
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("studentCode", loginPower.getName());
            Student student = studentService.getOne(queryWrapper);
            if (student == null) {
                return R.error("学生名称不存在");
            } else if (!SHA1Util.sha1(loginPower.getPassword()).equals(student.getPassword())) {
                System.out.println(loginPower.getPassword());
                System.out.println(student.getPassword());
                return R.error("学生密码错误");
            }
            return R.success(student, "欢迎回来" + student.getStudentName() + "同学");
        }
        return R.success(loginPower, "登录成功");
    }

    //注册
    @PostMapping("/register")
    public R register(@RequestBody LoginPower loginPower) {
        System.out.println("tCode:" + loginPower.gettCode());
        //管理员
        if (loginPower.gettCode().equals("01")) {
            Admin admin = new Admin();

            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(loginPower.getName());
            if (m.find()) {
                return R.error("真实姓名或用户名不能含有特殊字符");
            }
            if (loginPower.getName().length() < 1 || loginPower.getName().length() > 10) {
                return R.error("名称必须包含1至10个字符");
            }
            admin.setAdminName(loginPower.getName());


            if (!loginPower.getPassword().matches(regEx2)) {
                return R.error("密码至少包含：大小写英文字母、数字、特殊符号,密码长度大于6位,小于12位");
            }
            admin.setPassword(SHA1Util.sha1(loginPower.getPassword()));

            //自动生成adminCode,未调用StringNumber
            for (int i = 1; i < 9999; i++) {
                if (i<10){
                    admin.setAdminCode("100" + i);
                }else{
                    admin.setAdminCode("10" + i);
                }
                QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("adminCode", admin.getAdminCode());
                if (adminService.getOne(queryWrapper) == null) {
                    break;
                }
            }

            adminService.save(admin);
            return R.success(admin, "管理员注册成功");

        }
        //教师
        if (loginPower.gettCode().equals("02")) {
            Teacher teacher = new Teacher();

            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(loginPower.getName());
            if (m.find()) {
                return R.error("真实姓名或用户名不能含有特殊字符");
            }
            if (loginPower.getName().length() < 1 || loginPower.getName().length() > 10) {
                return R.error("名称必须包含1至10个字符");
            }
            teacher.setTeacherName(loginPower.getName());


            if (!loginPower.getPassword().matches(regEx2)) {
                return R.error("密码至少包含：大小写英文字母、数字、特殊符号,密码长度大于6位,小于12位");
            }
            teacher.setPassword(SHA1Util.sha1(loginPower.getPassword()));
            for (int i = 1; i < 9999; i++) {
                if (i<10){
                    teacher.setTeacherCode("200" + i);
                }else{
                    teacher.setTeacherCode("20"+i);
                }
                QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("teacherCode", teacher.getTeacherCode());
                if (teacherService.getOne(queryWrapper) == null) {
                    break;
                }
            }

            teacherService.save(teacher);
            return R.success(teacher, "教师注册成功,请在管理界面里面完善自己的个人信息");
        }

        //学生
        if (loginPower.gettCode().equals("03")) {
            Student student = new Student();

            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(loginPower.getName());
            if (m.find()) {
                return R.error("真实姓名或用户名不能含有特殊字符");
            }
            if (loginPower.getName().length() < 1 || loginPower.getName().length() > 10) {
                return R.error("名称必须包含1至10个字符");
            }
            student.setStudentName(loginPower.getName());


            if (!loginPower.getPassword().matches(regEx2)) {
                return R.error("密码至少包含：大小写英文字母、数字、特殊符号,密码长度大于6位,小于12位");
            }
            student.setPassword(SHA1Util.sha1(loginPower.getPassword()));
            for (int i = 1; i < 9999; i++) {
                if (i<10){
                    student.setStudentCode("020420" + i);
                }else{
                   student.setStudentCode("02042"+i);
                }
                QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("studentCode", student.getStudentCode());
                if (studentService.getOne(queryWrapper) == null) {
                    break;
                }
            }

            studentService.save(student);
            return R.success(student, "学生注册成功,请在管理界面里面完善自己的个人信息");
        }

        return R.success("", "注册成功");
    }
}
