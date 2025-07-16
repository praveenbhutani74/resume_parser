package com.resume.resume_parser.util;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public  class S3Utility {

	
	private static final String REGION ="ap-south-1";// System.getenv("REGION");

    private static final String ACCESS_KEY = "AKIA4SDNVRFHF74CFZM3";// System.getenv("awsAccessKey");
    private static final String SECRET_KEY = "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";// System.getenv("awsSecretKey");
    public static final String MEDIA_BASE_CDN_URL = "https://d1a3wxpa3ilhx6.cloudfront.net/";
    
    public static AWSSimpleSystemsManagement getSSMClient() {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return AWSSimpleSystemsManagementClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(REGION).build();
    }

    public static AmazonS3 getS3Client() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return AmazonS3ClientBuilder.standard().withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }
    
    public static S3Presigner getS3Presigner() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);

        AwsCredentialsProvider credentialsProvider = () -> credentials; 
        return S3Presigner.builder()
                .region(Region.of(REGION))
                .credentialsProvider(credentialsProvider)
                .build();
    }
    public static String getParameterFromSSMByName(String parameterKey) {
        try {
            GetParameterRequest parameterRequest = new GetParameterRequest();
            parameterRequest.withName(parameterKey).setWithDecryption(Boolean.valueOf(true));
            AWSSimpleSystemsManagement ssmClient = getSSMClient();
            GetParameterResult parameterResult = ssmClient.getParameter(parameterRequest);
            return parameterResult.getParameter().getValue();
        } catch (Exception e) {
            System.out.println("Exception=" + e.getMessage());
        }
        return null;
    }
    
    public static String signBucket(String keyName, String contentType) {
        try {
            String bucketName = "resume-parser-p";
            PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(keyName)
                    .contentType(contentType).build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)).putObjectRequest(objectRequest).build();

            PresignedPutObjectRequest presignedRequest = getS3Presigner().presignPutObject(presignRequest);

            String myURL = presignedRequest.url().toString();
            System.out.println("Presigned URL to upload a file to: " + myURL);
            System.out.println("Which HTTP method needs to be used when uploading a file: "
                    + presignedRequest.httpRequest().method());
            return myURL;

        } catch (S3Exception e) {
            e.getStackTrace();
        }
        return null;
        // snippet-end:[presigned.java2.generatepresignedurlimage.main]
    }

	

}
