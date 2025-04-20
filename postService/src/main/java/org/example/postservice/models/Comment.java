package org.example.postservice.models;

import com.google.cloud.spring.data.firestore.Document;
import lombok.*;
import org.springframework.data.annotation.Id;

@Document
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Comment {
    @Id
    private String commentID;

    private String fatherCommentID;

    private String userID;

    private String userNickname;

    private String userPhoto;

    private String comment;
}
