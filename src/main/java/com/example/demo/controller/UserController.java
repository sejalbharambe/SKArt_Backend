package com.example.demo.controller;

import com.example.demo.dto.OtpVerifyRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class UserController {

    @Autowired
    private UserService userService;

    // --- 1. Basic Registration ---
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> payload) {
        return userService.registerBasicUser(payload);
    }

    // --- 2. Artist Profile Registration ---
    @PostMapping("/artist/register/{userId}")
    public Map<String, Object> registerArtistDetails(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> payload) {
        return userService.registerArtistDetails(userId, payload);
    }

    // --- OTP Verify ---
    @PostMapping("/verify-otp")
    public Map<String, Object> verifyOtp(@RequestBody OtpVerifyRequest request) {
        return userService.verifyOtp(request.getUserId(), request.getOtp());
    }

    // --- Login ---
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        return userService.login(loginData.get("email"), loginData.get("password"));
    }

    // --- Get All Users ---
    @GetMapping("/all-users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // --- Update Profile ---
    @PutMapping("/profile/{userId}")
    public Map<String, Object> updateProfile(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> payload) {
        return userService.updateUserProfile(userId, payload);
    }

    // get profile by id
    @GetMapping("/profile/{userId}")
    public Map<String, Object> getUserProfile(@PathVariable Long userId) {
        return userService.getUserProfile(userId);
    }

    // ---------- Get complete artist profile ----------
    @GetMapping("/artist-profile/{userId}")
    public Map<String, Object> getArtistProfile(@PathVariable Long userId) {
        return userService.getArtistFullProfile(userId);
    }

}
