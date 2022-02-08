package com.cos.insta.config;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @Value("${file.path}")
    private String uploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry
                .addResourceHandler("/upload/**") // jsp페이지에서 /upload/** 이런 주소 패턴이 나오면 발동
                .addResourceLocations(amazonS3Client.getUrl(bucket,"").toString())
//                .addResourceLocations("file:///" + uploadFolder)
                .setCachePeriod(60 * 10 * 6)// 1시간동안 캐시 설정
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
