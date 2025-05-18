package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class UserResponseDto {
    private String uid;
    private String username;
    private String profileImageUrl;
}
