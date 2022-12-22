package com.voup.education.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_selectcourse")
public class Selectcourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("studentCode")
    private String studentCode="";

    @TableField("courseCode")
    private String courseCode="";


}
