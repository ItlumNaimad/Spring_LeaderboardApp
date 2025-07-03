package com.example.board.repository;

import com.example.board.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    boolean existsByName(String name);
}