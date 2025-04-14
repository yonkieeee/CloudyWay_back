package org.example.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;

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
    public void deleteObjectByUrl(String urlString) {
        try {
            URL url = new URL(urlString);

            String path = url.getPath();
            String key = path.startsWith("/") ? path.substring(1) : path;

            String host = url.getHost();
            if (!host.contains(bucketName)) {
                throw new IllegalArgumentException("URL does not match configured bucket");
            }

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
