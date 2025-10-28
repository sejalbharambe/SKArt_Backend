package com.example.demo.repository;

import com.example.demo.model.SelectedArtwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SelectedArtworkRepository extends JpaRepository<SelectedArtwork, Long> {
    Optional<SelectedArtwork> findByIsActiveTrue();
}
