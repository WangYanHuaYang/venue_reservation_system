package com.genolo.venue_reservation_system.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: sbm_demo
 * @description: mybatis配置类
 * @author: WYHY
 * @create: 2018-09-26 13:10
 **/
@EnableTransactionManagement
@Configuration
@MapperScan("com.genolo.venue_reservation_system.dao")
public class MybatisPlusConfig {


    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /***
     * Date日期类型转换器
     * @return
     */
    @Bean
    public Converter<String, LocalDateTime> LocalDateTimeConvert() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = null;
                try {
                    date = LocalDateTime.parse((String) source,df);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return date;
            }
        };
    }

}

