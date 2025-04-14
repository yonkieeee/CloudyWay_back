package org.example.postservice.models;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.*;


@Document
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post {
    @DocumentId
    private String postID;

    private String imageUrl;
    private String coordinates;
    private String description;

    @Builder.Default
    private Integer likes = 0;

    private String uid;
}