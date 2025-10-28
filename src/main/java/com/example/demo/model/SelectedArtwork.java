package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class SelectedArtwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store IDs of selected artworks
    @ElementCollection
    private List<Long> artworkIds;
    private boolean isActive = false;

    public SelectedArtwork() {}

    public SelectedArtwork(List<Long> artworkIds) {
        this.artworkIds = artworkIds;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getArtworkIds() {
        return artworkIds;
    }

    public void setArtworkIds(List<Long> artworkIds) {
        this.artworkIds = artworkIds;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
