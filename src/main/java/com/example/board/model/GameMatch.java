package com.example.board.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Encja reprezentująca pojedynczy mecz między graczami.
 * Przechowuje informacje o zwycięzcy, przegranym oraz dacie rozegrania meczu.
 */
@Entity
@Getter
@Setter
public class GameMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "winner_team_id")
    private Team winnerTeam;

    @ManyToOne
    @JoinColumn(name = "loser_team_id")
    private Team loserTeam;

    @ManyToOne
    @JoinColumn(name = "winner_player_id")
    private Player winnerPlayer;

    @ManyToOne
    @JoinColumn(name = "loser_player_id")
    private Player loserPlayer;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchType matchType;

    @Version
    private Long version;

    public enum MatchType {
        TEAM,
        PLAYER
    }


}