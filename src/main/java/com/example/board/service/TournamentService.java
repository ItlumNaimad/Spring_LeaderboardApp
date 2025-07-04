package com.example.board.service;

import com.example.board.model.*;
import com.example.board.repository.TournamentMatchRepository;
import com.example.board.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
/**
 * Serwis odpowiedzialny za zarządzanie turniejami w systemie.
 * Obsługuje tworzenie, aktualizację i pobieranie informacji o turniejach.
 */

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TournamentMatchRepository matchRepository;

    public TournamentService(TournamentRepository tournamentRepository,
                             TournamentMatchRepository matchRepository) {
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
    }

    @Transactional
    public Tournament createTournament(String name, Game game, List<Team> teams, LocalDateTime startDate) {
        if (tournamentRepository.existsByName(name)) {
            throw new IllegalArgumentException("Turniej o nazwie " + name + " już istnieje.");
        }

        if (teams.size() < 2) {
            throw new IllegalArgumentException("Turniej wymaga minimum 2 drużyn.");
        }

        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setGame(game);
        tournament.setStartDate(startDate);
        tournament.setStatus(Tournament.TournamentStatus.PENDING);

        tournament = tournamentRepository.save(tournament);

        // Generowanie meczy turniejowych
        List<TournamentMatch> matches = generateMatches(tournament, teams);
        tournament.getMatches().addAll(matches);
        matchRepository.saveAll(matches);

        return tournament;
    }

    private List<TournamentMatch> generateMatches(Tournament tournament, List<Team> teams) {
        List<TournamentMatch> matches = new ArrayList<>();
        List<Team> shuffledTeams = new ArrayList<>(teams);
        Collections.shuffle(shuffledTeams); // Losowa kolejność

        LocalDateTime matchDate = tournament.getStartDate();

        // Każdy z każdym
        for (int i = 0; i < shuffledTeams.size(); i++) {
            for (int j = i + 1; j < shuffledTeams.size(); j++) {
                TournamentMatch match = new TournamentMatch();
                match.setTournament(tournament);
                match.setTeamA(shuffledTeams.get(i));
                match.setTeamB(shuffledTeams.get(j));
                match.setScheduledDate(matchDate);
                match.setStatus(TournamentMatch.MatchStatus.SCHEDULED);
                matches.add(match);

                // Kolejny mecz za godzinę
                matchDate = matchDate.plusHours(1);
            }
        }

        return matches;
    }

    @Transactional
    public void updateMatchScore(Long matchId, Integer scoreA, Integer scoreB) {
        TournamentMatch match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Mecz nie istnieje"));

        match.setScoreA(scoreA);
        match.setScoreB(scoreB);
        match.setCompletedDate(LocalDateTime.now());
        match.setStatus(TournamentMatch.MatchStatus.COMPLETED);

        // Aktualizacja punktów w turnieju
        updateTournamentStandings(match.getTournament());
    }

    private void updateTournamentStandings(Tournament tournament) {
        Map<Team, Integer> points = new HashMap<>();
        Map<Team, Integer> wins = new HashMap<>();

        // Obliczanie punktów dla wszystkich drużyn
        for (TournamentMatch match : tournament.getMatches()) {
            if (match.getStatus() != TournamentMatch.MatchStatus.COMPLETED) continue;

            if (match.isDraw()) {
                // 1 punkt za remis
                points.merge(match.getTeamA(), 1, Integer::sum);
                points.merge(match.getTeamB(), 1, Integer::sum);
            } else {
                Team winner = match.getWinner();
                // 3 punkty za wygraną
                points.merge(winner, 3, Integer::sum);
                wins.merge(winner, 1, Integer::sum);
            }
        }

        // Sprawdzenie czy wszystkie mecze są zakończone
        boolean allMatchesCompleted = tournament.getMatches().stream()
                .allMatch(m -> m.getStatus() == TournamentMatch.MatchStatus.COMPLETED);

        if (allMatchesCompleted) {
            // Znalezienie zwycięzcy
            Optional<Map.Entry<Team, Integer>> winner = points.entrySet().stream()
                    .max(Map.Entry.comparingByValue());

            if (winner.isPresent()) {
                // Sprawdź czy jest remis na pierwszym miejscu
                int maxPoints = winner.get().getValue();
                List<Team> tiedTeams = points.entrySet().stream()
                        .filter(e -> e.getValue() == maxPoints)
                        .map(Map.Entry::getKey)
                        .toList();

                if (tiedTeams.size() > 1) {
                    // Rozstrzygnięcie po liczbie zwycięstw
                    Optional<Map.Entry<Team, Integer>> mostWins = wins.entrySet().stream()
                            .filter(e -> tiedTeams.contains(e.getKey()))
                            .max(Map.Entry.comparingByValue());

                    if (mostWins.isPresent() &&
                            wins.entrySet().stream()
                                    .filter(e -> tiedTeams.contains(e.getKey()))
                                    .filter(e -> e.getValue().equals(mostWins.get().getValue()))
                                    .count() == 1) {
                        // Mamy jednoznacznego zwycięzcę
                        tournament.setStatus(Tournament.TournamentStatus.COMPLETED);
                    } else {
                        // Potrzebny dodatkowy mecz
                        createTiebreakMatch(tournament, tiedTeams);
                    }
                } else {
                    tournament.setStatus(Tournament.TournamentStatus.COMPLETED);
                }
            }
            tournament.setEndDate(LocalDateTime.now());
            tournamentRepository.save(tournament);
        }
    }

    private void createTiebreakMatch(Tournament tournament, List<Team> tiedTeams) {
        // Tworzenie dodatkowego meczu dla drużyn remisujących
        LocalDateTime tiebreakDate = tournament.getMatches().stream()
                .map(TournamentMatch::getScheduledDate)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now())
                .plusDays(1);

        for (int i = 0; i < tiedTeams.size(); i += 2) {
            if (i + 1 < tiedTeams.size()) {
                TournamentMatch tiebreakMatch = new TournamentMatch();
                tiebreakMatch.setTournament(tournament);
                tiebreakMatch.setTeamA(tiedTeams.get(i));
                tiebreakMatch.setTeamB(tiedTeams.get(i + 1));
                tiebreakMatch.setScheduledDate(tiebreakDate);
                tiebreakMatch.setStatus(TournamentMatch.MatchStatus.SCHEDULED);
                matchRepository.save(tiebreakMatch);

                tiebreakDate = tiebreakDate.plusHours(1);
            }
        }
    }

    public Object getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournamentById(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turniej o id " + id + " nie istnieje."));
    }

    public Object getTournamentStandings(Long id) {
        return tournamentRepository.findById(id);
    }
}

