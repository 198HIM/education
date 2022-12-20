package com.voup.education.entity;

import lombok.Data;

/**
 * @Author HIM198
 * @Date 2022 10:34
 * @Description
 **/
@Data
public class CoursePage {
    private Integer currentPage;
    private Integer pageSize;
    private Course course;
}
