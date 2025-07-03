package com.example.board.repository;

import com.example.board.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repozytorium do zarządzania encjami Player w bazie danych.
 * Rozszerza JpaRepository, zapewniając standardowe operacje CRUD oraz dodatkowe metody wyszukiwania.
 */
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    /**
     * Wyszukuje gracza po jego nazwie użytkownika (nickname).
     *
     * @param nickname nazwa użytkownika gracza do wyszukania
     * @return Optional zawierający znalezionego gracza lub pusty, jeśli gracz nie istnieje
     */
    Optional<Player> findByNickname(String nickname);
}