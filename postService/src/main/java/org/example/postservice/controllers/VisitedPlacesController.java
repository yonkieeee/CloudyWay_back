package org.example.postservice.controllers;

import org.example.postservice.repositories.VisitedRegionsRepo;
import org.example.postservice.services.UserChangesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visitedPlaces")
public class VisitedPlacesController {
    @Autowired
    private UserChangesService userChangesService;

    @Autowired
    private VisitedRegionsRepo visitedRegionsRepo;

    @GetMapping
    public ResponseEntity<?> getVisitedPlaces(@RequestParam(value = "uid") String uid) {
        try{
            if(!userChangesService.checkUser(uid))
                return ResponseEntity.status(404).body("User not found");

            return ResponseEntity.ok(visitedRegionsRepo.getAllRegions(uid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
