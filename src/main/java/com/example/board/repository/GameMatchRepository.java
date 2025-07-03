package com.example.board.repository;

import com.example.board.model.GameMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repozytorium do zarządzania encjami GameMatch w bazie danych.
 * Rozszerza JpaRepository, zapewniając standardowe operacje CRUD oraz dodatkowe metody wyszukiwania meczy.
 */
public interface GameMatchRepository extends JpaRepository<GameMatch, Long> {

    List<GameMatch> findByWinnerTeamId(Long teamId);
    List<GameMatch> findByWinnerPlayerId(Long playerId);

    // Możesz też dodać metody dla przegranych
    List<GameMatch> findByLoserTeamId(Long teamId);
    List<GameMatch> findByLoserPlayerId(Long playerId);

    List<GameMatch> findByWinnerTeamIdAndMatchType(Long teamId, GameMatch.MatchType matchType);


}
