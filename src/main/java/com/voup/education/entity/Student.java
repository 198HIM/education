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
@TableName("t_student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("studentCode")
    private String studentCode="";

    @TableField("studentName")
    private String studentName="";

    @TableField("sex")
    private String sex="";

    @TableField("college")
    private String college="";

    @TableField("password")
    private String password="";


}
