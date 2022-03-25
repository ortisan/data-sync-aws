package com.ortiz.app.repository.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortiz.app.CustomException;
import com.ortiz.app.domains.Post;
import com.ortiz.app.repository.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Repository("postS3Repository")
public class PostS3Repository implements IPostRepository {

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${s3.bucket-name}")
    private String bucketName;

    @Override
    public Post save(Post post) {
        post.setId(UUID.randomUUID());
        try {
            byte[] bytesToWrite = objectMapper.writeValueAsBytes(post);
            // putting metadata
            ObjectMetadata omd = new ObjectMetadata();
            omd.setContentLength(bytesToWrite.length);
            omd.addUserMetadata("id", post.getId().toString());
            omd.addUserMetadata("authors", post.getAuthors().toString());
            omd.addUserMetadata("timestamp", "" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "posts/" + post.getId().toString(), new ByteArrayInputStream(bytesToWrite), omd);
            s3Client.putObject(putObjectRequest);
            return post;
        } catch (Exception exc) {
            throw new CustomException("Error to serialize or put object into s3", exc);
        }
    }

    @Override
    public Post getById(UUID id) {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, "posts/" + id.toString());
            S3Object object = s3Client.getObject(getObjectRequest);
            Post post = objectMapper.readValue(object.getObjectContent(), Post.class);
            return post;
        } catch (Exception exc) {
            throw new RuntimeException("Error to serialize or get object into s3", exc);
        }
    }
}
