package com.springbootinit.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis Plus 配置
 *
 * @author https://github.com/liyupi
 */
@Configuration
@MapperScan("com.springbootinit.mapper")
public class MyBatisPlusConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * 拦截器配置
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 全局配置 - 关闭驼峰自动转下划线
     */
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        // 关闭驼峰自动转下划线
        dbConfig.setTableUnderline(false);
        globalConfig.setDbConfig(dbConfig);
        return globalConfig;
    }

    /**
     * SqlSessionFactory 配置
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setGlobalConfig(globalConfig());
        // 添加分页拦截器
        sqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor());

        // 设置 Mapper XML 路径
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml")
        );

        return sqlSessionFactoryBean.getObject();
    }
}
