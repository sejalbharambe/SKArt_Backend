package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    // -------------------- Follow User --------------------
    @PostMapping("/{followingId}")
    public Map<String, Object> followUser(
            @PathVariable Long followingId,
            @RequestParam Long followerId) {
        return followService.followUser(followerId, followingId);
    }

    // -------------------- Unfollow User --------------------
    @DeleteMapping("/{followingId}")
    public Map<String, Object> unfollowUser(
            @PathVariable Long followingId,
            @RequestParam Long followerId) {
        return followService.unfollowUser(followerId, followingId);
    }

    // -------------------- Followers List --------------------
    @GetMapping("/followers/{userId}")
    public List<User> getFollowers(@PathVariable Long userId) {
        return followService.getFollowers(userId);
    }

    // -------------------- Following List --------------------
    @GetMapping("/following/{userId}")
    public List<User> getFollowing(@PathVariable Long userId) {
        return followService.getFollowing(userId);
    }
}
