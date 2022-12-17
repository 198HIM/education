package com.voup.education.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.oracle.tools.packager.Log;
import java.util.Scanner;

//执行 main 方法，控制台输入模块表名，回车自动生成对应项目目录中
public class MybatisPlusCodeGenerator {
    /**
     * <p>
     * 打印字符串,读取控制台内容
     * </p>
     */
    public static String scanner() {
        Scanner scanner = new Scanner(System.in);
        Log.info("请输入正确的表名,多个可使用,分隔");
        System.out.println("请输入正确的表名,多个可使用,分隔:");
        String input = scanner.next();
        if (!input.equals("")) {
            return input;
        }
        throw new MybatisPlusException("请输入正确的表名");
    }
    public static void main(String[] args) {
        //====================配置变量区域=====================//
        String author="voup";//生成文件的作者，可以不填
        String rootPackage="com.voup.education";//生成的entity、controller、service等包所在的公共上一级包路径全限定名
        String moduleName="";//src之前的路径
        //数据库配置
        String url="jdbc:mysql://localhost:3306/education?useSSL=false&characterEncoding=utf8";
        String driverClassName="com.mysql.cj.jdbc.Driver";//或者com.mysql.cj.jdbc.Driver
//        String driverClassName="com.mysql.jdbc.Driver";
        String username="root";
        String password="78787899";
        String tableNames=scanner();//表名，多个使用,分隔
        //====================配置变量区域=====================//

        // 代码生成器
        AutoGenerator generator = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath +"/"+moduleName+"/src/main/java");
        gc.setFileOverride(false);//是否覆盖已有文件，默认false
        gc.setOpen(false);//是否打开输出目录
        gc.setSwagger2(false); //实体属性 Swagger2 注解
        gc.setAuthor(author);
        gc.setServiceName("%sService");//去掉service接口的首字母I
        gc.setControllerName("%sController");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setBaseResultMap(true);//开启 BaseResultMap
        gc.setDateType(DateType.ONLY_DATE);//只使用 java.util.date代替
        gc.setIdType(IdType.ASSIGN_ID);//分配ID (主键类型为number或string）

        generator.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(url);
        dataSourceConfig.setDbType(DbType.MYSQL);//数据库类型
        dataSourceConfig.setDriverName(driverClassName);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        generator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        //packageConfig.setModuleName(scanner("模块名"));
        packageConfig.setParent(rootPackage);//例：com.him198.mybatisplus_auto
        generator.setPackageInfo(packageConfig);

        //注意：模板引擎在mybatisplus依赖中的templates目录下，可以依照此默认模板进行自定义

        // 策略配置：配置根据哪张表生成代码
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(tableNames.split(","));//表名，多个英文逗号分割（与exclude二选一配置）
        //过滤表前缀
        strategy.setTablePrefix(new String[] { "T_" });
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);//lombok模型，@Accessors(chain = true)setter链式操作
        strategy.setRestControllerStyle(true);//controller生成@RestController
        strategy.setEntityTableFieldAnnotationEnable(true);//是否生成实体时，生成字段注解


        generator.setStrategy(strategy);
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        generator.execute();
    }

}