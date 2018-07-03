//package com.ht.connected.home.backend.config.service;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
// 
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
// 
//@Configuration
//@EnableWebMvc
//@EnableSwagger2
//public class SwaggerConfig extends WebMvcConfigurerAdapter {
//    
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
//    
//    @Bean
//    public Docket api(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.common.controller")) // 해당 패키지만 필터
//                .paths(PathSelectors.ant("/api/common/**")) // 해당 request url 필터
//                .build()
//                .apiInfo(apiInfo())
//                .useDefaultResponseMessages(false); // default response description 사용하지 않음
//    }
// 
//    private ApiInfo apiInfo() {
//        @SuppressWarnings("deprecation")
//        ApiInfo apiInfo = new ApiInfo("API", "API 서비스", "API Doc v1.0", "", "sun", "License of Sun", "/");
//        return apiInfo;
//    }
//}
//
