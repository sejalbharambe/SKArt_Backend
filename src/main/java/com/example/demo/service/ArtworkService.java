package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.model.Artwork;
import com.example.demo.repository.ArtworkRepository;

@Service
public class ArtworkService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ArtworkRepository artworkRepository;

    public Artwork saveArtwork(String artName, String artistName, String category, MultipartFile imageFile, String size) throws IOException {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = imageFile.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;

        Files.copy(imageFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        Artwork artwork = new Artwork(artName, artistName, category, filePath, size);
        return artworkRepository.save(artwork);
    }

    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    public Artwork likeArtwork(Long id) {
        Artwork artwork = artworkRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Artwork not found"));
        artwork.setLikes(artwork.getLikes() + 1);
        return artworkRepository.save(artwork);
    }

    public Artwork dislikeArtwork(Long id) {
        Artwork artwork = artworkRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Artwork not found"));
        artwork.setDislikes(artwork.getDislikes() + 1);
        return artworkRepository.save(artwork);
    }
}
