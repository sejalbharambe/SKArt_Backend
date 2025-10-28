package com.example.demo.dto;

import com.example.demo.model.Artwork;
import java.util.List;

public class SelectedArtworkResponse {
    private Long id;
    private boolean isActive;
    private List<Artwork> artworks;

    public SelectedArtworkResponse(Long id, boolean isActive, List<Artwork> artworks) {
        this.id = id;
        this.isActive = isActive;
        this.artworks = artworks;
    }

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<Artwork> getArtworks() {
        return artworks;
    }
}
