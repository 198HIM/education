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
 * @since 2022-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("teacher")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("teacherCode")
    private String teacherCode;

    @TableField("teacherName")
    private String teacherName;

    @TableField("sex")
    private String sex;

    @TableField("birth")
    private String birth;

    @TableField("background")
    private String background;

    @TableField("position")
    private String position;

    @TableField("entryTime")
    private String entryTime;

    @TableField("college")
    private String college;


}
