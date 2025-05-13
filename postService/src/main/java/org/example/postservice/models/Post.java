package org.example.postservice.models;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.*;

import java.util.Map;


@Document
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post {
    @DocumentId
    private String postID;

    private String placeId;
    private String imageUrl;
    private Coordinates coordinates;
    private String description;

    @Builder.Default
    private Integer likes = 0;

    @Builder.Default
    private Integer comments = 0;

    private String uid;
}