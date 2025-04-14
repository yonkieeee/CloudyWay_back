package org.example.postservice.dto;

import org.springframework.web.multipart.MultipartFile;

public record RequestPost(MultipartFile file, String description, String coordinates) {
}
