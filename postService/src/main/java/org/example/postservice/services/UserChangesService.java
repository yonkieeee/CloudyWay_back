package org.example.postservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserChangesService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void addPost(String uid){
        String url = "http://localhost:8080/profile";

        var user = restTemplate.getForObject(url + "?uid=" + uid, Map.class);

        assert user != null;
        var visitedPlaces = Integer.parseInt(user.get("visitedPlaces").toString());
        var changes = new HashMap<String, Object>();

        changes.put("visitedPlaces", visitedPlaces + 1);

        restTemplate.put(url + "/change?uid=" + uid, changes);
    }
}
