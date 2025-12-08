package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.model.Artwork;
import com.example.demo.service.ArtworkService;

@RestController
@RequestMapping("/api/artworks")
// @CrossOrigin(origins = "http://localhost:5173")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    private static final List<String> ART_CATEGORIES = List.of(
            "Fine Arts",
            "Craft and Design",
            "Digital and Media Art",
            "Cultural and Traditional Art",
            "Decorative and LifeStyle Art");

    // Upload artwork with userId
    @PostMapping("/upload")
    public ResponseEntity<?> uploadArtwork(
            @RequestParam("artName") String artName,
            @RequestParam("artistName") String artistName,
            @RequestParam("category") String category,
            @RequestParam("size") String size,
            @RequestParam("paintedOn") String paintedOn,
            @RequestParam("price") double price,
            @RequestParam("sold") boolean sold,
            @RequestParam("userId") Long userId,
            @RequestParam("description") String description,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if (!ART_CATEGORIES.contains(category)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid category. Please select a valid art category."));
        }

        Artwork saved = artworkService.saveArtwork(
                artName, artistName, category, imageFile, size, paintedOn, price, sold, userId, description);

        return ResponseEntity.ok(Map.of(
                "message", "Artwork uploaded successfully.",
                "data", saved));
    }

    // Get all artworks
    @GetMapping
    public List<Artwork> getAllArtworks() {
        return artworkService.getAllArtworks();
    }

    // Get artworks by category
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getArtworksByCategory(@PathVariable String category) {
        List<Artwork> artworks = artworkService.getArtworksByCategory(category);
        if (artworks.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No artworks found for this category"));
        }
        return ResponseEntity.ok(artworks);
    }

    // Get artworks by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getArtworksByUserId(@PathVariable Long userId) {
        List<Artwork> artworks = artworkService.getArtworksByUserId(userId);
        if (artworks.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No artworks found for this user."));
        }
        return ResponseEntity.ok(artworks);
    }

    // Like artwork
    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> likeArtwork(@PathVariable Long id, @PathVariable Long userId) {
        Map<String, Object> result = artworkService.reactToArtwork(id, userId, "like");
        return ResponseEntity.ok(result);
    }

    // Dislike artwork
    @PutMapping("/{id}/dislike/{userId}")
    public ResponseEntity<?> dislikeArtwork(@PathVariable Long id, @PathVariable Long userId) {
        Map<String, Object> result = artworkService.reactToArtwork(id, userId, "dislike");
        return ResponseEntity.ok(result);
    }

    // Get total likes by userId
    @GetMapping("/likes/{userId}")
    public ResponseEntity<?> getTotalLikesByUserId(@PathVariable Long userId) {
        Map<String, Object> result = artworkService.getTotalLikesByUserId(userId);
        return ResponseEntity.ok(result);
    }

}
