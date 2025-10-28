package com.example.demo.controller;

import com.example.demo.dto.SelectedArtworkResponse;
import com.example.demo.service.SelectedArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/selected-artworks")
public class SelectedArtworkController {

    @Autowired
    private SelectedArtworkService service;

    // POST: Save selected artwork IDs
    @PostMapping
    public SelectedArtworkResponse saveSelectedArtworks(@RequestBody List<Long> artworkIds) {
        return service.saveSelectedArtworks(artworkIds);
    }

    // GET: Get all selected artworks
    @GetMapping
    public List<SelectedArtworkResponse> getAllSelectedArtworks() {
        return service.getAllSelectedArtworks();
    }

    // GET: Get the currently active selection
    @GetMapping("/active")
    public SelectedArtworkResponse getActiveSelectedArtworks() {
        return service.getActiveSelectedArtworks();
    }

    // PUT: Activate a specific selection by ID
    @PutMapping("/active/{id}")
    public SelectedArtworkResponse activateSelectedArtwork(@PathVariable Long id) {
        return service.activateSelectedArtwork(id);
    }
}
