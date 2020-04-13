package com.genolo.venue_reservation_system.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
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
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
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

    @Bean

    public Connector connector() {

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");

        connector.setScheme("http");

        connector.setPort(8181);

        connector.setSecure(false);

        connector.setRedirectPort(8443);

        return connector;

    }

    @Bean

    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {

            @Override
            protected void postProcessContext(Context context) {

                SecurityConstraint securityConstraint = new SecurityConstraint();

                securityConstraint.setUserConstraint("CONFIDENTIAL");

                SecurityCollection collection = new SecurityCollection();

                collection.addPattern("/*");

                securityConstraint.addCollection(collection);

                context.addConstraint(securityConstraint);

            }

        };

        tomcat.addAdditionalTomcatConnectors(connector());

        return tomcat;

    }


}
