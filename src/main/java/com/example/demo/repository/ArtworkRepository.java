package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Artwork;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
}
