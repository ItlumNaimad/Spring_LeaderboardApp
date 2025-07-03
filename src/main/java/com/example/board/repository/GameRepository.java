package com.example.board.repository;

import com.example.board.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByName(String name);
}
