package org.example.userservice.controllers;

import org.example.userservice.dto.RequestProfileImage;
import org.example.userservice.models.User;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@CrossOrigin("*")
public class ProfileController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3Service s3Service;

    @PutMapping("/photo")
    public ResponseEntity<?> putPhoto(@RequestParam(value="uid") String uid,
                                      @ModelAttribute RequestProfileImage requestProfileImage) throws IOException {
        try {
            User user = userRepository.getUserBy("uid", uid).orElse(null);

            if(user == null){
                return ResponseEntity.badRequest().body("User not found");
            }

            if(user.getProfileImageUrl() != null){
                s3Service.deleteObjectByUrl(user.getProfileImageUrl());
            }
            String fileName = requestProfileImage.file().getOriginalFilename();

            String key = "profileImage/" + uid + "/" + fileName;

            var putPhoto = s3Service.PutObject(key,  requestProfileImage.file());

            user.setProfileImageUrl(putPhoto);

            userRepository.saveUser(user);

            return ResponseEntity.ok().body(putPhoto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserById(@RequestParam(value="uid") String uid) {
        try{
            if (userRepository.existById(uid)) {
                return ResponseEntity.ok(userRepository.getUser(uid));
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return null;
    }

    @PutMapping("/change")
    public ResponseEntity<?> changeUser(@RequestParam(value="uid") String uid,
                                        @RequestBody Map<String, Object> body) {
        try{
            if (userRepository.existById(uid)) {
                if(body.containsKey("username")){
                    if(userRepository.getUserBy("username", body.get("username").toString()).isPresent()){
                        return ResponseEntity.status(409).body("Username already exists");
                    }
                }

                userRepository.changeUser(uid, body);
                return ResponseEntity.ok().body("User change successful");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return null;
    }
}
