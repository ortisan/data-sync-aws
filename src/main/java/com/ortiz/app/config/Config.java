package com.ortiz.app.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
public class Config implements WebMvcConfigurer {

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Value("${aws.service-endpoint}")
    private String serviceEndpoint;

    @Bean
    public AmazonS3 s3Client() {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        if (serviceEndpoint != null && serviceEndpoint.length() > 0) {
            builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, awsRegion)).withPathStyleAccessEnabled(true);
        }
        return builder.build();
    }

    @Bean
    public AmazonDynamoDB dynamoDBClient() {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        if (serviceEndpoint != null && serviceEndpoint.length() > 0) {
            builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, awsRegion));
        }
        return builder.build();
    }

    @Bean
    public AmazonSNS snsClient() {
        AmazonSNSClientBuilder builder = AmazonSNSClientBuilder.standard();
        if (serviceEndpoint != null && serviceEndpoint.length() > 0) {
            builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, awsRegion));
        }
        return builder.build();
    }

    @Bean
    public AmazonSQS sqsClient() {
        AmazonSQSClientBuilder builder = AmazonSQSClientBuilder.standard();
        if (serviceEndpoint != null && serviceEndpoint.length() > 0) {
            builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, awsRegion));
        }
        return builder.build();
    }
}
