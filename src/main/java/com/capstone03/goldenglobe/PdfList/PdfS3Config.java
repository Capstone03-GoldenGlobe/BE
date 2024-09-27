package com.capstone03.goldenglobe.PdfList;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class PdfS3Config {
  @Value("${spring.cloud.aws.credentials.accessKey}")
  private String accessKey;

  @Value("${spring.cloud.aws.credentials.secretKey}")
  private String secretKey;

  @Value("${spring.cloud.aws.region.static}")
  private String region;

  @Bean
  @Primary
  public BasicAWSCredentials awsCredentialsProviderPDF() {
    BasicAWSCredentials basicAWSCredentialsPDF = new BasicAWSCredentials(accessKey, secretKey);
    return basicAWSCredentialsPDF;
  }

  @Bean
  public AmazonS3 amazonS3PDF(BasicAWSCredentials awsCredentialsProviderPDF) {
    return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentialsProviderPDF))
            .build();
  }
}
