package com.example.board.service;

import com.example.board.model.*;
import com.example.board.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * Serwis odpowiedzialny za zarządzanie meczami w systemie.
 */
@Service
public class GameMatchService {
    private static final Logger logger = LoggerFactory.getLogger(GameMatchService.class);

    private final GameMatchRepository gameMatchRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;

    public GameMatchService(GameMatchRepository gameMatchRepository,
                            PlayerRepository playerRepository,
                            TeamRepository teamRepository,
                            GameRepository gameRepository) {
        this.gameMatchRepository = gameMatchRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Dodaje nowy mecz drużynowy.
     * @param gameId ID gry
     * @param winnerTeamId ID drużyny zwycięskiej
     * @param loserTeamId ID drużyny przegranej
     * @return utworzony obiekt GameMatch
     * @throws IllegalArgumentException gdy parametry są nieprawidłowe
     * @throws RuntimeException gdy wystąpi błąd podczas zapisu do bazy danych
     */
    @Transactional
    public GameMatch addTeamMatch(Long gameId, Long winnerTeamId, Long loserTeamId) {
        validateTeamMatchParameters(gameId, winnerTeamId, loserTeamId);

        try {
            Game game = getGameById(gameId);
            Team winnerTeam = getTeamById(winnerTeamId);
            Team loserTeam = getTeamById(loserTeamId);

            validateMatchDate(LocalDateTime.now());

            updateTeamStatistics(winnerTeam, loserTeam);

            GameMatch match = createTeamMatch(game, winnerTeam, loserTeam);

            saveTeamChanges(winnerTeam, loserTeam);
            logger.info("Dodano nowy mecz drużynowy: {} vs {}", winnerTeamId, loserTeamId);

            return gameMatchRepository.save(match);
        } catch (DataAccessException e) {
            logger.error("Błąd podczas zapisywania meczu drużynowego", e);
            throw new RuntimeException("Błąd podczas zapisywania meczu", e);
        }
    }

    /**
     * Dodaje nowy mecz indywidualny.
     * @param gameId ID gry
     * @param winnerPlayerId ID zwycięskiego gracza
     * @param loserPlayerId ID przegranego gracza
     * @return utworzony obiekt GameMatch
     * @throws IllegalArgumentException gdy parametry są nieprawidłowe
     * @throws RuntimeException gdy wystąpi błąd podczas zapisu do bazy danych
     */
    @Transactional
    public GameMatch addPlayerMatch(Long gameId, Long winnerPlayerId, Long loserPlayerId) {
        validatePlayerMatchParameters(gameId, winnerPlayerId, loserPlayerId);

        try {
            Game game = getGameById(gameId);
            Player winner = getPlayerById(winnerPlayerId);
            Player loser = getPlayerById(loserPlayerId);

            validateMatchDate(LocalDateTime.now());

            updatePlayerStatistics(winner, loser);

            GameMatch match = createPlayerMatch(game, winner, loser);

            savePlayerChanges(winner, loser);
            logger.info("Dodano nowy mecz indywidualny: {} vs {}", winnerPlayerId, loserPlayerId);

            return gameMatchRepository.save(match);
        } catch (DataAccessException e) {
            logger.error("Błąd podczas zapisywania meczu indywidualnego", e);
            throw new RuntimeException("Błąd podczas zapisywania meczu", e);
        }
    }

    private void validateTeamMatchParameters(Long gameId, Long winnerTeamId, Long loserTeamId) {
        if (gameId == null || winnerTeamId == null || loserTeamId == null) {
            throw new IllegalArgumentException("Parametry nie mogą być null");
        }
        if (winnerTeamId.equals(loserTeamId)) {
            throw new IllegalArgumentException("Drużyny nie mogą być takie same");
        }
    }

    private void validatePlayerMatchParameters(Long gameId, Long winnerPlayerId, Long loserPlayerId) {
        if (gameId == null || winnerPlayerId == null || loserPlayerId == null) {
            throw new IllegalArgumentException("Parametry nie mogą być null");
        }
        if (winnerPlayerId.equals(loserPlayerId)) {
            throw new IllegalArgumentException("Gracze nie mogą być tacy sami");
        }
    }

    private Game getGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gry o ID: " + gameId));
    }

    private Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono drużyny o ID: " + teamId));
    }

    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gracza o ID: " + playerId));
    }

    private void validateMatchDate(LocalDateTime matchDate) {
        if (matchDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data meczu nie może być z przyszłości");
        }
    }

    private void updateTeamStatistics(Team winner, Team loser) {
        winner.setWins(winner.getWins() + 1);
        loser.setLosses(loser.getLosses() + 1);
    }

    private void updatePlayerStatistics(Player winner, Player loser) {
        winner.setWins(winner.getWins() + 1);
        loser.setLosses(loser.getLosses() + 1);
    }

    private GameMatch createTeamMatch(Game game, Team winner, Team loser) {
        GameMatch match = new GameMatch();
        match.setGame(game);
        match.setWinnerTeam(winner);
        match.setLoserTeam(loser);
        match.setMatchType(GameMatch.MatchType.TEAM);
        match.setMatchDate(LocalDateTime.now());
        return match;
    }

    private GameMatch createPlayerMatch(Game game, Player winner, Player loser) {
        GameMatch match = new GameMatch();
        match.setGame(game);
        match.setWinnerPlayer(winner);
        match.setLoserPlayer(loser);
        match.setMatchType(GameMatch.MatchType.PLAYER);
        match.setMatchDate(LocalDateTime.now());
        return match;
    }

    private void saveTeamChanges(Team winner, Team loser) {
        teamRepository.save(winner);
        teamRepository.save(loser);
    }

    private void savePlayerChanges(Player winner, Player loser) {
        playerRepository.save(winner);
        playerRepository.save(loser);
    }
}