package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"userId", "artwork_id"})
})
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // Assuming user IDs come from your auth table

    @ManyToOne
    @JoinColumn(name = "artwork_id")
    private Artwork artwork;

    private String type; // "like" or "dislike"

    public Reaction() {}

    public Reaction(Long userId, Artwork artwork, String type) {
        this.userId = userId;
        this.artwork = artwork;
        this.type = type;
    }

    // Getters & setters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Artwork getArtwork() { return artwork; }
    public void setArtwork(Artwork artwork) { this.artwork = artwork; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
