package org.example.postservice.controllers;

import org.example.postservice.dto.RequestLike;
import org.example.postservice.models.Like;
import org.example.postservice.models.Post;
import org.example.postservice.repositories.LikeRepo;
import org.example.postservice.repositories.PostRepo;
import org.example.postservice.services.UserChangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/like")
@CrossOrigin("*")
public class LikeController {
    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserChangesService userChangesService;

    @PutMapping("/add")
    public ResponseEntity<?> addLike(@RequestParam(value = "uid") String uid,
                                     @RequestParam(value = "post-id") String postId,
                                     @RequestParam(value = "user-uid") String userUid,
                                     @RequestBody RequestLike like) {
        try {
            if(!userChangesService.checkUser(uid)
                    || !userChangesService.checkUser(userUid)){
                return ResponseEntity.status(404).body("User not found");
            }

            if(likeRepo.likeExist(uid, postId, userUid)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Like already exists");
            }

            Post post = postRepo.getPost(uid, postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

            likeRepo.addLike(uid, postId, userUid, like.userNickname(), like.userPhotoUrl());

            post.setLikes(post.getLikes() + 1);

            postRepo.changePost(uid, post);
            return ResponseEntity.ok().body("Like is good");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/remove")
    public ResponseEntity<?> removeLike(@RequestParam(value = "uid") String uid,
                                     @RequestParam(value = "post-id") String postId,
                                     @RequestParam(value = "user-uid") String userUid) {
        try {
            if(!userChangesService.checkUser(uid)
                    || !userChangesService.checkUser(userUid)){
                return ResponseEntity.status(404).body("User not found");
            }

            if(!likeRepo.likeExist(uid, postId, userUid)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Like already exists");
            }

            Post post = postRepo.getPost(uid, postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

            if(post.getLikes() == 0)
                return ResponseEntity.status(418).build();

            likeRepo.removeLike(uid, postId, userUid);

            post.setLikes(post.getLikes() - 1);

            postRepo.changePost(uid, post);
            return ResponseEntity.ok().body("Like is good");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
