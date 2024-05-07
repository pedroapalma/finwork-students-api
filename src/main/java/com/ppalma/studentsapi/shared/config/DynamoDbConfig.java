package com.ppalma.studentsapi.shared.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {

  @Value("${aws.dynamodb.endpoint}")
  private String dynamoDBEndpoint;

  @Value("${aws.dynamodb.accesskey}")
  private String awsAccessKey;

  @Value("${aws.dynamodb.secretkey}")
  private String awsSecretKey;

  @Value("${aws.region}")
  private String region;

  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
    return AmazonDynamoDBClientBuilder.standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(this.dynamoDBEndpoint, this.region))
        .withCredentials(
            new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.awsAccessKey,
                this.awsSecretKey)))
        .build();
  }

  @Bean
  public DynamoDBMapper amazonDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
    return new DynamoDBMapper(amazonDynamoDB);
  }
}
