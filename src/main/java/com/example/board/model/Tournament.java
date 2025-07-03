package com.example.board.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<TournamentMatch> matches = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private TournamentStatus status = TournamentStatus.PENDING;

    @Version
    private Long version;

    public enum TournamentStatus {
        PENDING,    // Oczekujący
        IN_PROGRESS,// W trakcie
        COMPLETED  // Zakończony
    }
}
