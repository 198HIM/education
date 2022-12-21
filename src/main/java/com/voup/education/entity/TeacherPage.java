package com.voup.education.entity;

/**
 * @Author HIM198
 * @Date 2022 21:40
 * @Description
 **/


import lombok.Data;

/**
 * @Author HIM198
 * @Date 2022 10:34
 * @Description
 **/
@Data
public class TeacherPage {
    private Integer currentPage;
    private Integer pageSize;
    private Teacher teacher;
}

