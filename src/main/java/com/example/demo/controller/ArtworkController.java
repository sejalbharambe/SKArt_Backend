package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
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

    // ✅ Define allowed categories right here (used internally)
    private static final List<String> ART_CATEGORIES = List.of(
        "Portrait",
        "Landscape",
        "Abstract",
        "Modern Art",
        "Surrealism",
        "Realism",
        "Pop Art",
        "Impressionism"
    );

    @PostMapping("/upload")
    public ResponseEntity<Artwork> uploadArtwork(
            @RequestParam("artName") String artName,
            @RequestParam("artistName") String artistName,
            @RequestParam("category") String category,
            @RequestParam("size") String size,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        // ✅ Optional: Validate category before saving
        if (!ART_CATEGORIES.contains(category)) {
            return ResponseEntity.badRequest().build(); // invalid category
        }

        Artwork saved = artworkService.saveArtwork(artName, artistName, category, imageFile, size);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Artwork> getAllArtworks() {
        return artworkService.getAllArtworks();
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<Artwork> likeArtwork(@PathVariable Long id) {
        Artwork updated = artworkService.likeArtwork(id);
        return ResponseEntity.ok(updated);
    }

    // ✅ Dislike an artwork
    @PutMapping("/{id}/dislike")
    public ResponseEntity<Artwork> dislikeArtwork(@PathVariable Long id) {
        Artwork updated = artworkService.dislikeArtwork(id);
        return ResponseEntity.ok(updated);
    }
}
