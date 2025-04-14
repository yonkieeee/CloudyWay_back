package org.example.postservice.controllers;

import org.example.postservice.repositories.PostRepo;
import org.example.postservice.dto.RequestPost;
import org.example.postservice.models.Post;
import org.example.postservice.services.S3Service;
import org.example.postservice.services.UserChangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.UID;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@CrossOrigin("*")
public class PostController {
    private final S3Service s3Service;
    private final PostRepo postRepo;

    @Autowired
    private UserChangesService userChangesService;

    @Autowired
    public PostController(S3Service s3Service, PostRepo postRepo) {
        this.s3Service = s3Service;
        this.postRepo = postRepo;
    }

    @PutMapping
    public ResponseEntity<?> putPhoto(@RequestParam("UID") String uid,
                                      @ModelAttribute RequestPost requestPost) {
        try {
            Post post = new Post();

            String fileName = requestPost.file().getOriginalFilename();

            String key = "posts/" + uid + "/" + fileName;

            var putPhoto = s3Service.PutObject(key, requestPost.file());

            post.setUid(uid);
            post.setImageUrl(putPhoto);
            post.setCoordinates(requestPost.coordinates());
            post.setUid(UUID.randomUUID().toString());
            post.setDescription(requestPost.description());

            postRepo.addPost(uid, post);
            userChangesService.addPost(uid);

            return ResponseEntity.ok().body(putPhoto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts(@RequestParam(value = "uid") String uid,
                                         @RequestParam(value = "post-id", required = false) String postId) {
        try{
            if (postId != null) {
                return ResponseEntity.ok(postRepo.getPost(uid, postId));
            }
            return ResponseEntity.ok(postRepo.getPosts(uid));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
