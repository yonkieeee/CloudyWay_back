package org.example.postservice.controllers;

import org.example.postservice.dto.RequestComment;
import org.example.postservice.dto.RequestLike;
import org.example.postservice.models.Comment;
import org.example.postservice.models.Post;
import org.example.postservice.repositories.CommentRepo;
import org.example.postservice.repositories.PostRepo;
import org.example.postservice.services.UserChangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/comment")
@CrossOrigin("*")
public class CommentController {
    @Autowired
    private UserChangesService userChangesService;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private PostRepo postRepo;

    @PutMapping("/add")
    public ResponseEntity<?> addComment(@RequestParam(value = "uid") String uid,
                                        @RequestParam(value = "post-id") String postId,
                                        @RequestParam(value = "user-uid") String userUid,
                                        @RequestParam(value = "answer-comment-id", required = false) String answerCommentId,
                                        @RequestBody RequestComment requestComment){
        try{
            if(!userChangesService.checkUser(uid)
                    || !userChangesService.checkUser(userUid)){
                return ResponseEntity.status(404).body("User not found");
            }
            Post post = postRepo.getPost(uid, postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

            if(answerCommentId != null){
                commentRepo.getComment(uid, postId, answerCommentId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
            }

            commentRepo.addComment(uid, postId, new Comment(uid
                    ,answerCommentId, userUid, requestComment.userNickname()
                    ,requestComment.userPhotoUrl(), requestComment.commentText()));

            post.setComments(post.getComments() + 1);

            postRepo.changePost(uid, post);

            return ResponseEntity.ok().body("Comment created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getComments(@RequestParam(value = "uid") String uid
            ,@RequestParam(value = "post-id") String postId){
        try{
            if(!userChangesService.checkUser(uid)){
                return ResponseEntity.status(404).body("User not found");
            }

            Post post = postRepo.getPost(uid, postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

            return new ResponseEntity<>(commentRepo.getComments(uid, postId), HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
