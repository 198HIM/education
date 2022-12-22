package com.voup.education.mapper;

import com.voup.education.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.swing.*;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author voup
 * @since 2022-12-20
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {


}
