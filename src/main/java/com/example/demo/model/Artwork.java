package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String artName;
    private String artistName;
    private String description;
    private String category;
    private String imagePath;
    private String size;
    private String paintedOn;
    private double price;
    private boolean sold = false;
    private int likes = 0;
    private int dislikes = 0;

    // 🔹 Add this field
    private Long userId;

    public Artwork() {
    }

    public Artwork(String artName, String artistName, String category, String imagePath,
            String size, String paintedOn, double price, boolean sold, Long userId,
            String description) {
        this.artName = artName;
        this.artistName = artistName;
        this.description = description;
        this.category = category;
        this.imagePath = imagePath;
        this.size = size;
        this.paintedOn = paintedOn;
        this.price = price;
        this.sold = sold;
        this.userId = userId;
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDescription() {
        return description;
    }

    public void SetDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPaintedOn() {
        return paintedOn;
    }

    public void setPaintedOn(String paintedOn) {
        this.paintedOn = paintedOn;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
