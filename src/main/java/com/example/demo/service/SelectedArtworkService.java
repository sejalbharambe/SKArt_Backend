package com.example.demo.service;

import com.example.demo.dto.SelectedArtworkResponse;
import com.example.demo.model.Artwork;
import com.example.demo.model.SelectedArtwork;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.SelectedArtworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectedArtworkService {

    @Autowired
    private SelectedArtworkRepository repository;

    @Autowired
    private ArtworkRepository artworkRepository;

    // Save a new selection (inactive by default)
    public SelectedArtworkResponse saveSelectedArtworks(List<Long> artworkIds) {
        SelectedArtwork newSelection = new SelectedArtwork(artworkIds);
        newSelection.setActive(false); // don't auto-activate
        SelectedArtwork saved = repository.save(newSelection);

        List<Artwork> artworks = artworkRepository.findAllById(saved.getArtworkIds());
        return new SelectedArtworkResponse(saved.getId(), saved.isActive(), artworks);
    }

    // Get all groups
    public List<SelectedArtworkResponse> getAllSelectedArtworks() {
        List<SelectedArtwork> all = repository.findAll();
        return all.stream()
                .map(sa -> {
                    List<Artwork> artworks = artworkRepository.findAllById(sa.getArtworkIds());
                    return new SelectedArtworkResponse(sa.getId(), sa.isActive(), artworks);
                })
                .toList();
    }

    // Get only the active one
    public SelectedArtworkResponse getActiveSelectedArtworks() {
        SelectedArtwork active = repository.findByIsActiveTrue().orElse(null);
        if (active == null) return null;
        List<Artwork> artworks = artworkRepository.findAllById(active.getArtworkIds());
        return new SelectedArtworkResponse(active.getId(), active.isActive(), artworks);
    }

    // Activate a specific group by ID
    public SelectedArtworkResponse activateSelectedArtwork(Long id) {
        // deactivate all
        List<SelectedArtwork> all = repository.findAll();
        for (SelectedArtwork sa : all) sa.setActive(false);
        repository.saveAll(all);

        // activate the selected one
        SelectedArtwork selected = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SelectedArtwork not found with id " + id));
        selected.setActive(true);
        repository.save(selected);

        List<Artwork> artworks = artworkRepository.findAllById(selected.getArtworkIds());
        return new SelectedArtworkResponse(selected.getId(), selected.isActive(), artworks);
    }
}
