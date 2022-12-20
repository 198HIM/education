package com.voup.education.service.impl;

import com.voup.education.entity.Student;
import com.voup.education.mapper.StudentMapper;
import com.voup.education.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author voup
 * @since 2022-12-20
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
