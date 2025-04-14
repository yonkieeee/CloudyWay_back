package org.example.userservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FireBaseConfig {

    @Value("${firebase.credentials}")
    private String firebaseCredentials;

    @Value("${firebase.url}")
    private String firebaseURL;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(firebaseCredentials.getBytes())))
                .setDatabaseUrl(firebaseURL)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }
}
