package com.voup.education.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author voup
 * @since 2022-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("courseCode")
    private String courseCode;

    @TableField("courseName")
    private String courseName;

    /**
     * 授课老师编号
     */
    @TableField("teacherCode")
    private String teacherCode;

    /**
     * 上课时间
     */
    @TableField("time")
    private String time;

    /**
     * 地点
     */
    @TableField("location")
    private String location;

    /**
     * 周数
     */
    @TableField("weeks")
    private String weeks;

    /**
     * 必修或者选修
     */
    @TableField("courseClass")
    private String courseClass;

    /**
     * 学分
     */
    @TableField("credit")
    private String credit;


}
