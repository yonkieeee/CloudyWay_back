package org.example.postservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.postservice.models.Coordinates;
import org.example.postservice.repositories.PostRepo;
import org.example.postservice.models.Post;
import org.example.postservice.repositories.VisitedRegionsRepo;
import org.example.postservice.services.S3Service;
import org.example.postservice.services.UserChangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.server.UID;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@CrossOrigin("*")
public class PostController {
    private final S3Service s3Service;
    private final PostRepo postRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserChangesService userChangesService;

    @Autowired
    private VisitedRegionsRepo visitedRegionsRepo;

    @Autowired
    public PostController(S3Service s3Service, PostRepo postRepo) {
        this.s3Service = s3Service;
        this.postRepo = postRepo;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> putPhoto(@RequestParam("UID") String uid,
                                      @RequestPart("file") MultipartFile file,
                                      @RequestPart("placeID") String placeID,
                                      @RequestPart(value = "description", required = false) String description,
                                      @RequestPart("coordinates") String coordinatesJson) {
        try {
            if(!userChangesService.checkUser(uid))
                return ResponseEntity.status(404).body("User does not exist");

            ObjectMapper objectMapper = new ObjectMapper();
            Coordinates coordinates = objectMapper.readValue(coordinatesJson, Coordinates.class);

            String lat = String.valueOf(coordinates.getLat());
            String lon = String.valueOf(coordinates.getLng());

            boolean isUserInRadius = Boolean.TRUE.equals(restTemplate.getForObject("http://3.75.94.120:5001/places/isUserOnPlace?here_api_id="
                    + placeID + "&lat=" + lat + "&lon=" + lon, Boolean.class));

            if(!isUserInRadius)
                return ResponseEntity.status(403).body("User is not right place for making post" + isUserInRadius + lat + lon);

            if(postRepo.existByPlaceId(uid, placeID))
                return ResponseEntity.status(409).body("Post with this place already exists");

            Post post = new Post();

            String fileName = file.getOriginalFilename();

            String key = "posts/" + uid + "/" + fileName;

            var putPhoto = s3Service.PutObject(key, file);

            post.setUid(uid);
            post.setImageUrl(putPhoto);
            post.setPlaceId(placeID);
            post.setPostID(UUID.randomUUID().toString());
            post.setDescription(description);


            var regionData = restTemplate.getForObject("http://3.75.94.120:5001/counties/getCounty?here_api_id="
                    + placeID, Map.class);

            if(regionData == null)
                return ResponseEntity.status(403).body("some trouble");

            if(!visitedRegionsRepo.existsRegion(uid, regionData.get("county").toString()))
                visitedRegionsRepo.addRegionIfNotExists(uid, regionData.get("county").toString());

            visitedRegionsRepo.changeRegion(uid, regionData.get("county").toString(), Integer.parseInt(regionData.get("quantity").toString()));

            post.setRegion(regionData.get("county").toString());
            post.setDate(LocalDate.now().toString());

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
