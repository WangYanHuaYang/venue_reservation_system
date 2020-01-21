package com.genolo.venue_reservation_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * @program: sbm_demo
 * @description: 网络请求配置
 * @author: WYHY
 * @create: 2018-10-08 17:28
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 上传文件根路径
     */
    @Value("${files.path}")
    private String filesPath;

    /**
     * 日志文件根路径
     */
    @Value("${log.path}")
    private String logPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations(ResourceUtils.FILE_URL_PREFIX + filesPath + File.separator)
                .addResourceLocations(ResourceUtils.FILE_URL_PREFIX + logPath + File.separator);
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:login/index.html");
    }

}
