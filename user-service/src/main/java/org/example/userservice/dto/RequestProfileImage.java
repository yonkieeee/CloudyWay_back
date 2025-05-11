package org.example.userservice.dto;

import org.springframework.web.multipart.MultipartFile;

public record RequestProfileImage(MultipartFile file) {
}
