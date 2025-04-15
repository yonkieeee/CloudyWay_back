package org.example.userservice.controllers;

import org.example.userservice.models.User;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.services.ForgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/reset-password")
@CrossOrigin(origins = "*")
public class ResetPasswordController {
    private final ForgetPasswordService forgetPasswordService;
    private final UserRepository userRepository;

    @Autowired
    public ResetPasswordController(ForgetPasswordService forgetPasswordService,
                                   UserRepository userRepository) {
        this.userRepository = userRepository;
        this.forgetPasswordService = forgetPasswordService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOTP(@RequestBody Map<String, String> body)
            throws ExecutionException, InterruptedException {
        String identifier = body.get("identifier").contains("@") ? "email" : "username";
        User user = userRepository.getUserBy(identifier, body.get("identifier")).orElse(null);

        if(user != null){
            try {
                forgetPasswordService.sendOTP(user.getEmail());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("OTP sent successfully");
        } else{
            return ResponseEntity.ok().body("User not exists");
        }
    }
    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOTP(@RequestBody Map<String, String> body)
            throws ExecutionException, InterruptedException{
        String identifier = body.get("identifier").contains("@") ? "email" : "username";
        User user = userRepository.getUserBy(identifier, body.get("identifier")).orElse(null);

        String otp = body.get("otp");

        if(user == null) return ResponseEntity.badRequest().body("User not found");

        if(forgetPasswordService.validateOTP(user.getEmail(), otp)){
            return ResponseEntity.ok().body("OTP verified");
        } else {
            return ResponseEntity.badRequest().body("OTP not verified");
        }
    }

    @PutMapping
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> body)
            throws ExecutionException, InterruptedException {
        String identifier = body.get("identifier").toString().contains("@") ? "email" : "username";
        User user = userRepository.getUserBy(identifier, body.get("identifier").toString()).orElse(null);

        if(user == null) return ResponseEntity.badRequest().body("User not exists");

        try {
            forgetPasswordService.changePassword(user, body);
            return ResponseEntity.ok().body("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}