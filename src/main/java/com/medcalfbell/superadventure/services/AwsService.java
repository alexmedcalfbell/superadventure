package com.medcalfbell.superadventure.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AwsService implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(AwsService.class);
    private static final Regions REGION = Regions.EU_WEST_2;

    private AmazonS3 s3Client;

    @Value("${aws.access.key.id:12345}")
    private String accessKey;

    @Value("${aws.secret.access.key:12345}")
    private String secretKey;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public void afterPropertiesSet() throws Exception {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    @Override
    public void destroy() {

    }

    /**
     * Uploads the supplied asset to S3.
     */
    public String uploadAsset(MultipartFile asset) throws IOException {

        final String assetName = asset.getOriginalFilename();

        logger.info("Uploading asset [{}] to S3", assetName);

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(asset.getSize());
        metadata.setContentType(asset.getContentType());

        final PutObjectRequest request = new PutObjectRequest(
                bucketName,
                assetName,
                asset.getInputStream(),
                metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(request);

        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                REGION.getName(),
                request.getKey());
    }
}
