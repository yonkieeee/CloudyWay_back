package org.example.postservice.models;

import com.google.cloud.spring.data.firestore.Document;
import lombok.*;

@Document
@Data @NoArgsConstructor @AllArgsConstructor
public class VisitedRegions {
    private String region;
    private Integer count;
    private Double percentage;
}
