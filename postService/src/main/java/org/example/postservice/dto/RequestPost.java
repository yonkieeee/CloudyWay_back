package org.example.postservice.dto;

import org.springframework.web.multipart.MultipartFile;

public record RequestPost(MultipartFile file, String placeID, String description, String coordinates) {
}
