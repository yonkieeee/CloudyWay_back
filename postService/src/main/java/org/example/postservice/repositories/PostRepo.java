package org.example.postservice.repositories;

import org.example.postservice.models.Post;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Repository
public class PostRepo {

    private final CollectionReference postsCollection;

    @Autowired
    private Firestore firestore;

    @Autowired
    public PostRepo(Firestore firestore) {
        this.postsCollection = firestore.collection("posts");
    }

    public void addPost(String uid, Post post) throws ExecutionException, InterruptedException {
        CollectionReference postsRef = firestore.collection("users").document(uid).collection("posts");

        if(post.getPostID() == null || post.getPostID().isEmpty()) {
            post.setPostID(postsRef.document().getId());
        }
        post.setUid(uid);
        postsRef.document(post.getPostID()).set(post).get();
    }

    public Optional<List<Post>> getPosts(String uid) throws ExecutionException, InterruptedException {
        CollectionReference postsRef = firestore.collection("users").document(uid).collection("posts");
        ApiFuture<QuerySnapshot> query = postsRef.get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        List<Post> posts = new ArrayList<>();

        for(QueryDocumentSnapshot document : documents) {
            Post post = document.toObject(Post.class);
            posts.add(post);
        }

        return Optional.of(posts);
    }

    public Optional<Post> getPost(String uid, String postId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(uid).collection("posts").document(postId);
        DocumentSnapshot snapshot = docRef.get().get();

        return Optional.ofNullable(snapshot.toObject(Post.class));
    }

    public void deletePost(String uid, String postId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(uid).collection("posts").document(postId);

        docRef.delete().get();
    }
}
