package com.example.demo.service;

import com.example.demo.model.Follow;
import com.example.demo.model.User;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    // -------------------- Follow a User --------------------
    public Map<String, Object> followUser(Long followerId, Long followingId) {
        Map<String, Object> response = new HashMap<>();

        if (followerId == null || followingId == null) {
            response.put("message", "FollowerId and FollowingId are required.");
            return response;
        }

        if (Objects.equals(followerId, followingId)) {
            response.put("message", "You cannot follow yourself.");
            return response;
        }

        Optional<User> followerOpt = userRepository.findById(followerId);
        Optional<User> followingOpt = userRepository.findById(followingId);

        if (followerOpt.isEmpty() || followingOpt.isEmpty()) {
            response.put("message", "User not found.");
            return response;
        }

        User follower = followerOpt.get();
        User following = followingOpt.get();

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            response.put("message", "Already following this user.");
            return response;
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);

        response.put("message", "Followed successfully.");
        return response;
    }

    // -------------------- Unfollow a User --------------------
    public Map<String, Object> unfollowUser(Long followerId, Long followingId) {
        Map<String, Object> response = new HashMap<>();

        if (followerId == null || followingId == null) {
            response.put("message", "FollowerId and FollowingId are required.");
            return response;
        }

        if (Objects.equals(followerId, followingId)) {
            response.put("message", "You cannot unfollow yourself.");
            return response;
        }

        Optional<User> followerOpt = userRepository.findById(followerId);
        Optional<User> followingOpt = userRepository.findById(followingId);

        if (followerOpt.isEmpty() || followingOpt.isEmpty()) {
            response.put("message", "User not found.");
            return response;
        }

        User follower = followerOpt.get();
        User following = followingOpt.get();

        Optional<Follow> followOpt = followRepository.findByFollowerAndFollowing(follower, following);
        if (followOpt.isEmpty()) {
            response.put("message", "You are not following this user.");
            return response;
        }

        // Delete the follow entity
        followRepository.delete(followOpt.get());
        response.put("message", "Unfollowed successfully.");

        return response;
    }

    // -------------------- Get Followers --------------------
    public List<User> getFollowers(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return Collections.emptyList();

        List<Follow> followers = followRepository.findByFollowing(userOpt.get());
        List<User> followerUsers = new ArrayList<>();
        for (Follow follow : followers) {
            followerUsers.add(follow.getFollower());
        }
        return followerUsers;
    }

    // -------------------- Get Following --------------------
    public List<User> getFollowing(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return Collections.emptyList();

        List<Follow> following = followRepository.findByFollower(userOpt.get());
        List<User> followingUsers = new ArrayList<>();
        for (Follow follow : following) {
            followingUsers.add(follow.getFollowing());
        }
        return followingUsers;
    }
}
