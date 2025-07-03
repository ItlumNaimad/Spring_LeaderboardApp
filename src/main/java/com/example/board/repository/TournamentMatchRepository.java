package com.example.board.repository;

import com.example.board.model.Tournament;
import com.example.board.model.TournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentMatchRepository extends JpaRepository<TournamentMatch, Long> {
    List<TournamentMatch> findByTournamentOrderByScheduledDateAsc(Tournament tournament);
}
