package com.ppalma.studentsapi.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {

  @Value("${amazon.dynamodb.endpoint}")
  private String dynamoDBEndpoint;

  @Value("${amazon.aws.accesskey}")
  private String awsAccessKey;

  @Value("${amazon.aws.secretkey}")
  private String awsSecretKey;

  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
    return AmazonDynamoDBClientBuilder.standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(this.dynamoDBEndpoint, "us-west-2"))
        .withCredentials(
            new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.awsAccessKey,
                this.awsSecretKey)))
        .build();
  }
}
