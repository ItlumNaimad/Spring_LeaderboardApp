package com.example.board.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TournamentMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "team_a_id", nullable = false)
    private Team teamA;

    @ManyToOne
    @JoinColumn(name = "team_b_id", nullable = false)
    private Team teamB;

    private Integer scoreA;
    private Integer scoreB;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    private LocalDateTime completedDate;

    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Version
    private Long version;

    public enum MatchStatus {
        SCHEDULED,  // Zaplanowany
        COMPLETED, // Zakończony
        CANCELLED  // Anulowany
    }

    public boolean isDraw() {
        return scoreA != null && scoreB != null && scoreA.equals(scoreB);
    }

    public Team getWinner() {
        if (scoreA == null || scoreB == null) return null;
        if (scoreA > scoreB) return teamA;
        if (scoreB > scoreA) return teamB;
        return null; // remis
    }

    public Team getLoser() {
        if (scoreA == null || scoreB == null) return null;
        if (scoreA < scoreB) return teamA;
        if (scoreB < scoreA) return teamB;
        return null; // remis
    }
}
