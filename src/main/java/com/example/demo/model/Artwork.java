package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String artName;
    private String artistName;
    private String category;
    private String imagePath;
    private String size;

    private int likes = 0;
    private int dislikes = 0;

    public Artwork() {}

    public Artwork(String artName, String artistName, String category, String imagePath, String size) {
        this.artName = artName;
        this.artistName = artistName;
        this.category = category;
        this.imagePath = imagePath;
        this.size = size;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getArtName() { return artName; }
    public void setArtName(String artName) { this.artName = artName; }
    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
    public int getDislikes() { return dislikes; }
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }
}
