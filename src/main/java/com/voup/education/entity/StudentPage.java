package com.voup.education.entity;

import lombok.Data;

/**
 * @Author HIM198
 * @Date 2022 14:07
 * @Description
 **/

@Data
public class StudentPage {

    private Integer currentPage;
    private Integer pageSize;
    private Student student;
}
