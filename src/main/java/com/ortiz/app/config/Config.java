package com.ortiz.app.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Value("${s3.service-endpoint}")
    private String ssmServiceEndpoint;

    @Bean
    public AmazonS3 s3Client() {
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .build();
        return s3client;
    }
}
