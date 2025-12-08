package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.model.Artwork;
import com.example.demo.model.Reaction;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.ReactionRepository;

@Service
public class ArtworkService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    // ✅ Save new artwork
    public Artwork saveArtwork(String artName, String artistName, String category,
                               MultipartFile imageFile, String size,
                               String paintedOn, double price, boolean sold, Long userId, String description) throws IOException {

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = imageFile.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;
        Files.copy(imageFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        Artwork artwork = new Artwork(artName, artistName, category, filePath, size, paintedOn, price, sold, userId, description);
        return artworkRepository.save(artwork);
    }

    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    public List<Artwork> getArtworksByCategory(String category) {
        return artworkRepository.findByCategoryIgnoreCase(category);
    }

    public List<Artwork> getArtworksByUserId(Long userId) {
        return artworkRepository.findByUserId(userId);
    }

    // ✅ Unified like/dislike handler
    public Map<String, Object> reactToArtwork(Long artworkId, Long userId, String reactionType) {
        Map<String, Object> response = new HashMap<>();

        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new RuntimeException("Artwork not found"));

        Optional<Reaction> existingOpt = reactionRepository.findByUserIdAndArtworkId(userId, artworkId);

        if (existingOpt.isPresent()) {
            Reaction existing = existingOpt.get();

            if (existing.getType().equals(reactionType)) {
                response.put("status", "info");
                response.put("message", "You already " +
                        (reactionType.equals("like") ? "liked" : "disliked") + " this artwork.");
                response.put("artwork", artwork);
                return response;
            }

            if (existing.getType().equals("like")) {
                artwork.setLikes(Math.max(artwork.getLikes() - 1, 0));
                artwork.setDislikes(artwork.getDislikes() + 1);
                existing.setType("dislike");
                response.put("message", "You changed your reaction to dislike.");
            } else {
                artwork.setDislikes(Math.max(artwork.getDislikes() - 1, 0));
                artwork.setLikes(artwork.getLikes() + 1);
                existing.setType("like");
                response.put("message", "You changed your reaction to like.");
            }

            reactionRepository.save(existing);
            artworkRepository.save(artwork);
        } else {
            Reaction newReaction = new Reaction(userId, artwork, reactionType);
            reactionRepository.save(newReaction);

            if (reactionType.equals("like")) {
                artwork.setLikes(artwork.getLikes() + 1);
                response.put("message", "You liked this artwork.");
            } else {
                artwork.setDislikes(artwork.getDislikes() + 1);
                response.put("message", "You disliked this artwork.");
            }
            artworkRepository.save(artwork);
        }

        response.put("status", "success");
        response.put("artwork", artwork);
        return response;
    }

    //get total likes for all artworks by user id 
    public Map<String, Object> getTotalLikesByUserId(Long userId) {
        Map<String, Object> response = new HashMap<>();
        List<Artwork> artworks = artworkRepository.findByUserId(userId);

        if (artworks.isEmpty()) {
            response.put("message", "No artworks found for this user.");
            response.put("totalLikes", 0);
            return response;
        }

        int totalLikes = artworks.stream().mapToInt(Artwork::getLikes).sum();
        int totalDislikes = artworks.stream().mapToInt(Artwork::getDislikes).sum();

        response.put("userId", userId);
        response.put("totalArtworks", artworks.size());
        response.put("totalLikes", totalLikes);
        response.put("totalDislikes", totalDislikes);
        response.put("message", "Link data fetched successfully");

        return response;
    }
}
