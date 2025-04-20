package org.example.postservice.models;

import com.google.cloud.spring.data.firestore.Document;
import lombok.*;
import org.springframework.data.annotation.Id;

@Document
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Like {
    @Id
    private String like_id;

    private String user_id;

    private String user_nickname;

    private String user_photo;
}
