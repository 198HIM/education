package com.voup.education.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.voup.education.bean.R;
import com.voup.education.entity.Admin;

import com.voup.education.mapper.AdminMapper;
import com.voup.education.service.AdminService;
import com.voup.education.utils.SHA1Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;



    //验证用户名
    String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
    //效验密码
    String regEx2 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,12}$";

    @PostMapping("/editMyself")
    public R update(@RequestBody Admin admin) {


        //更新
        LambdaUpdateWrapper<Admin> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (admin.getPassword()!=""){
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(admin.getAdminName());
            if (m.find()) {
                return R.error("真实姓名或用户名不能含有特殊字符");
            }
            if (admin.getAdminName().length() < 1 || admin.getAdminName().length() > 10) {
                return R.error("名称必须包含1至10个字符");
            }

            if (!admin.getPassword().matches(regEx2)) {
                return R.error("密码至少包含：大小写英文字母、数字、特殊符号,密码长度大于6位,小于12位");
            }
            lambdaUpdateWrapper.eq(Admin::getAdminCode, admin.getAdminCode())
                    .set(Admin::getAdminName, admin.getAdminName())
                    .set(Admin::getPassword, SHA1Util.sha1(admin.getPassword()));
        }else{
            admin.setPassword(adminService.getOne(new QueryWrapper<Admin>().eq("adminCode",admin.getAdminCode())).getPassword());
            lambdaUpdateWrapper.eq(Admin::getAdminCode, admin.getAdminCode())
                    .set(Admin::getAdminName, admin.getAdminName())

                    .set(Admin::getPassword, admin.getPassword());
        }

        //更新中的用户名密码效验
        if (adminService.update(null, lambdaUpdateWrapper)) {

            return R.success("", "管理员信息更新成功");
        } else {
            return R.error("管理员信息更新失败");
        }

    }
}
