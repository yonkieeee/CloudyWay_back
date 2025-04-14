package org.example.postservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;

@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    private final S3Client s3;

    @Autowired
    public S3Service(S3Client s3) {
        this.s3 = s3;
    }

    public String PutObject(String key, MultipartFile file) {
        try (var inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
            GetUrlRequest request = GetUrlRequest.builder().bucket(bucketName).key(key).build();

            return s3.utilities().getUrl(request).toExternalForm();
        }catch (IOException e) {
            return e.getMessage();
        }
    }
}