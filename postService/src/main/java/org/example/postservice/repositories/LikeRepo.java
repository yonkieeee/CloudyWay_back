package org.example.postservice.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.example.postservice.models.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class LikeRepo {
    @Autowired
    private Firestore firestore;

    @Autowired
    public LikeRepo(Firestore firestore) {
        this.firestore = firestore;
    }

    private CollectionReference getLikesCollection(String uid, String postId) {
        return firestore.
                collection("users")
                .document(uid)
                .collection("posts")
                .document(postId)
                .collection("likes");
    }

    public void addLike(String uid, String postId, String user_id, String user_nickname, String user_photo)
            throws ExecutionException, InterruptedException {
        String like_id = UUID.randomUUID().toString();

        Like like = new Like(like_id, user_id, user_nickname, user_photo);

        getLikesCollection(uid, postId).document(user_id).set(like).get();
    }

    public void removeLike(String uid, String postId, String user_id)
            throws ExecutionException, InterruptedException {
        getLikesCollection(uid, postId).document(user_id).delete().get();
    }

    public boolean likeExist(String uid, String postId, String user_id)
            throws ExecutionException, InterruptedException {
        return getLikesCollection(uid, postId).document(user_id).get().get().exists();
    }

    public List<Like> getLikes(String uid, String postId)
            throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getLikesCollection(uid, postId).get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        return docs.stream().map(doc -> doc.toObject(Like.class)).collect(Collectors.toList());
    }

    public int countLikesForPost(String uid, String postId)
            throws ExecutionException, InterruptedException {
        return getLikesCollection(uid, postId).get().get().size();
    }
}


