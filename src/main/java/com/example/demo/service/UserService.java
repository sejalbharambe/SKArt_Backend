package com.example.demo.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Artwork;
import com.example.demo.model.User;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Path UPLOAD_DIR = Paths.get("uploads/profile-images");

    // --- 1. Basic Registration (No image, no artist info) ---
    public Map<String, Object> registerBasicUser(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            String name = (String) payload.get("name");
            String username = (String) payload.get("username");
            String email = (String) payload.get("email");
            String password = (String) payload.get("password");
            String phoneNumber = (String) payload.get("phoneNumber");
            String role = (String) payload.getOrDefault("role", "user");

            if (userRepository.findByEmail(email).isPresent()) {
                response.put("message", "Email already exists!");
                return response;
            }

            User user = new User();
            user.setName(name);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setPhoneNumber(phoneNumber);
            user.setRole(role);
            user.setOtp(String.format("%06d", new Random().nextInt(999999)));
            user.setVerified(false);

            User savedUser = userRepository.save(user);
            emailService.sendOtpEmail(email, user.getOtp());

            response.put("message", "User registered successfully. OTP sent to email.");
            response.put("userId", savedUser.getId());
            return response;
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return response;
        }
    }

    // --- 2. Artist Details Registration ---
    public Map<String, Object> registerArtistDetails(Long userId, Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                response.put("message", "User not found.");
                return response;
            }

            User user = optionalUser.get();
            if (!"artist".equalsIgnoreCase(user.getRole())) {
                response.put("message", "This user is not an artist.");
                return response;
            }

            String artistSignature = (String) payload.get("artistSignature");
            String about = (String) payload.get("about");
            String location = (String) payload.get("location");
            String base64Image = (String) payload.get("profileImage");

            Map<String, String> links = null;
            Object linksObj = payload.get("links");
            if (linksObj instanceof Map) {
                // noinspection unchecked
                links = (Map<String, String>) linksObj;
            }

            user.setArtistSignature(artistSignature);
            user.setAbout(about);
            user.setLocation(location);
            user.setLinks(links);

            // Handle image
            if (base64Image != null && base64Image.startsWith("data:image")) {
                Files.createDirectories(UPLOAD_DIR);
                String ext = guessExtensionFromDataUri(base64Image);
                String fileName = UUID.randomUUID() + "_profile" + ext;
                Path filePath = UPLOAD_DIR.resolve(fileName);
                byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
                Files.write(filePath, imageBytes);
                user.setProfileImage(fileName);
            }

            userRepository.save(user);
            response.put("message", "Artist profile details saved successfully.");
            response.put("user", user);
            return response;
        } catch (Exception e) {
            response.put("message", "Error saving artist profile: " + e.getMessage());
            return response;
        }
    }

    // --- Helper ---
    private String guessExtensionFromDataUri(String dataUri) {
        try {
            String mime = dataUri.substring(5, dataUri.indexOf(';'));
            String ext = mime.substring(mime.indexOf('/') + 1);
            return "." + ext;
        } catch (Exception e) {
            return ".jpg";
        }
    }

    // -------------------- OTP Verification --------------------
    public Map<String, Object> verifyOtp(Long userId, String otp) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            response.put("message", "User not found!");
            return response;
        }

        User user = optionalUser.get();

        if (user.getOtp().equals(otp)) {
            user.setVerified(true);
            user.setOtp(null);
            userRepository.save(user);

            response.put("message", "Email verified successfully!");
            response.put("userDetails", user);
        } else {
            response.put("message", "Invalid OTP!");
        }
        return response;
    }

    // -------------------- Login --------------------
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            response.put("message", "User not found!");
            return response;
        }

        User user = optionalUser.get();

        if (!user.isVerified()) {
            response.put("message", "Email  not verified. Please verify your account.");
            return response;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("message", "Invalid credentials!");
            return response;
        }

        response.put("message", "Login successful!");
        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("username", user.getUsername());
        response.put("phoneNumber", user.getPhoneNumber());
        response.put("artistSignature", user.getArtistSignature());
        return response;
    }

    // -------------------- Get All Users --------------------
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Map<String, Object> updateUserProfile(Long userId, Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                response.put("message", "User not found!");
                return response;
            }

            User user = optionalUser.get();

            // Basic fields
            if (payload.containsKey("name"))
                user.setName((String) payload.get("name"));
            if (payload.containsKey("username"))
                user.setUsername((String) payload.get("username"));
            if (payload.containsKey("phoneNumber"))
                user.setPhoneNumber((String) payload.get("phoneNumber"));
            if (payload.containsKey("about"))
                user.setAbout((String) payload.get("about"));
            if (payload.containsKey("location"))
                user.setLocation((String) payload.get("location"));

            // Links
            if (payload.containsKey("links")) {
                Map<String, String> links = (Map<String, String>) payload.get("links");
                user.setLinks(links);
            }

            // Artist-specific
            if ("artist".equalsIgnoreCase(user.getRole()) && payload.containsKey("artistSignature")) {
                user.setArtistSignature((String) payload.get("artistSignature"));
            }

            // Handle base64 image update
            if (payload.containsKey("profileImage")) {
                String image = (String) payload.get("profileImage");

                // CASE 1: Base64 upload (artist)
                if (image != null && image.startsWith("data:image")) {
                    String fileName = UUID.randomUUID() + "_profile.jpg";
                    Path uploadDir = Paths.get("uploads/profile-images");
                    Files.createDirectories(uploadDir);
                    Path filePath = uploadDir.resolve(fileName);

                    String base64Data = image.split(",")[1];
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
                    Files.write(filePath, decodedBytes);

                    user.setProfileImage("/uploads/profile-images/" + fileName);
                }
                // CASE 2: Default profile image (normal user/admin)
                else if (image != null && (image.startsWith("/default") || image.startsWith("http"))) {
                    user.setProfileImage(image);
                } // CASE 2: Default or preloaded images (icon_images, default, http)
                else if (image != null && (image.startsWith("/default") ||
                        image.startsWith("http") ||
                        image.startsWith("/icon_images"))) {
                    user.setProfileImage(image);
                }
            }
            userRepository.save(user);

            response.put("message", "Profile updated successfully!");
            response.put("updatedUser", user);
        } catch (Exception e) {
            response.put("message", "Error updating profile: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> getUserProfile(Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                response.put("message", "User not found!");
                return response;
            }

            User user = optionalUser.get();

            // Convert to response map (avoid returning password/OTP)
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("name", user.getName());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            userData.put("phoneNumber", user.getPhoneNumber());
            userData.put("role", user.getRole());
            userData.put("artistSignature", user.getArtistSignature());
            userData.put("about", user.getAbout());
            userData.put("location", user.getLocation());
            userData.put("links", user.getLinks());
            userData.put("verified", user.isVerified());

            // Include public URL or file reference for profile image
            if (user.getProfileImage() != null) {
                // userData.put("profileImage", "/uploads/profile-images/" +
                // user.getProfileImage());
                // Include public URL or file reference for profile image
                String img = user.getProfileImage();
                if (img != null && !img.isEmpty()) {
                    userData.put("profileImage", img); // just the filename
                } else {
                    userData.put("profileImage", null);
                }
            } else {
                userData.put("profileImage", null);
            }

            response.put("message", "User profile fetched successfully!");
            response.put("user", userData);

        } catch (Exception e) {
            response.put("message", "Error fetching profile: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> getArtistFullProfile(Long userId) {

        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Clean user response
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("role", user.getRole());
        userData.put("artistSignature", user.getArtistSignature());
        userData.put("about", user.getAbout());
        userData.put("location", user.getLocation());
        userData.put("links", user.getLinks());
        userData.put("profileImage", user.getProfileImage());

        response.put("user", userData);

        // Followers
        List<User> followers = followRepository.findFollowers(userId);
        response.put("followers", followers);
        response.put("followersCount", followers.size());

        // Following
        List<User> following = followRepository.findFollowing(userId);
        response.put("following", following);
        response.put("followingCount", following.size());

        // Artworks
        List<Artwork> artworks = artworkRepository.findByUserId(userId);
        response.put("artworks", artworks);
        response.put("artworksCount", artworks.size());

        // Total Likes
        int totalLikes = artworks.stream().mapToInt(Artwork::getLikes).sum();
        response.put("totalLikes", totalLikes);

        return response;
    }

}
