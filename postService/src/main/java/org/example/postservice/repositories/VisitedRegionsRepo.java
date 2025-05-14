package org.example.postservice.repositories;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.example.postservice.models.VisitedRegions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class VisitedRegionsRepo {

    @Autowired
    private Firestore firestore;
    @Autowired
    public VisitedRegionsRepo(Firestore firestore) {
        this.firestore = firestore;
    }

    private CollectionReference getVisitedRegionsCollection(String uid) {
        return firestore.
                collection("users")
                .document(uid)
                .collection("visitedRegions");
    }

    public boolean existsRegion(String uid, String region)
            throws ExecutionException, InterruptedException {
        return getVisitedRegionsCollection(uid).document(region).get().get().exists();
    }

    public void addRegionIfNotExists(String uid, String region)
            throws ExecutionException, InterruptedException {
        VisitedRegions visitedRegion = new VisitedRegions();
        visitedRegion.setRegion(region);
        visitedRegion.setCount(0);
        visitedRegion.setPercentage(0.0);

        getVisitedRegionsCollection(uid).document(region).set(visitedRegion).get();
    }

    public void changeRegion(String uid, String region, double totalCountPlaces)
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = getVisitedRegionsCollection(uid).document(region);
        VisitedRegions regionData = docRef.get().get().toObject(VisitedRegions.class);

        if (regionData == null) {
            return;
        }

        double newCount = regionData.getCount() + 1;
        double newPercentage = (newCount / totalCountPlaces) * 100.0;

        regionData.setCount((int) newCount);
        regionData.setPercentage(newPercentage);

        docRef.set(regionData).get();
    }

    public List<VisitedRegions> getAllRegions(String uid) throws ExecutionException, InterruptedException {
        return getVisitedRegionsCollection(uid)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> doc.toObject(VisitedRegions.class))
                .toList();
    }
}
