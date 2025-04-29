package org.example.postservice.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.example.postservice.models.Comment;
import org.example.postservice.models.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class CommentRepo {
    @Autowired
    private Firestore firestore;

    @Autowired
    public CommentRepo(Firestore firestore) {
        this.firestore = firestore;
    }

    private CollectionReference getCommentsCollection(String uid, String postId) {
        return firestore.
                collection("users")
                .document(uid)
                .collection("posts")
                .document(postId)
                .collection("comments");
    }

    public void addComment(String uid, String postId, Comment comment)
            throws ExecutionException, InterruptedException {
        String commentId = UUID.randomUUID().toString();

        comment.setCommentID(commentId);

        getCommentsCollection(uid, postId)
                .document(commentId)
                .set(comment).get();
    }

    public void removeComment(String uid, String postId, String comment_id)
            throws ExecutionException, InterruptedException {
        getCommentsCollection(uid, postId).document(comment_id).delete().get();
    }

    public Optional<List<Comment>> getComments(String uid, String postId)
            throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCommentsCollection(uid, postId).get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        return Optional.of(docs.stream()
                .map(doc -> doc.toObject(Comment.class))
                .collect(Collectors.toList()));
    }

    public Optional<Comment> getComment(String uid, String postId, String comment_id)
            throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> future = getCommentsCollection(uid, postId).document(comment_id).get();
        DocumentSnapshot document = future.get();

        if(document.exists()) {
            Comment comment = document.toObject(Comment.class);
            return Optional.of(comment);
        }

        return Optional.empty();
    }

    public int countCommentsForPost(String uid, String postId)
            throws ExecutionException, InterruptedException {
        return getCommentsCollection(uid, postId).get().get().size();
    }
}


