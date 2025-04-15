package org.example.userservice.models;

import com.google.cloud.spring.data.firestore.Document;
import com.google.firebase.database.annotations.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;


@Builder
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Document(collectionName="users")
public class User {

    @Id
    @NotNull
    private String uid;

    private String username;

    @NotNull
    private String email;
    private String dateOfBirth;
    private String gender;
    private String region;
    private String profileImageUrl;
    private String description;

    @Builder.Default
    private Integer countOfFriens = 0;

    @Builder.Default
    private Integer visitedPlaces = 0;

    @Builder.Default
    private Integer mapVisitPercent = 0;

    @Builder.Default
    private String role = "USER";
}

