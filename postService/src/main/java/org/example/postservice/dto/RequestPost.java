package org.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Data
@AllArgsConstructor @NoArgsConstructor
public class RequestPost {
    private MultipartFile file;
    private String placeID;
    private String description;
    private Map<String, Object> coordinates;
}
