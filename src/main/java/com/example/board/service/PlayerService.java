package com.example.board.service;

import com.example.board.model.Player;
import com.example.board.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serwis odpowiedzialny za logikę biznesową związaną z zarządzaniem graczami.
 * Obsługuje operacje takie jak tworzenie nowych graczy i pobieranie listy wszystkich graczy.
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;


    /**
     * Konstruktor serwisu PlayerService.
     *
     * @param playerRepository repozytorium do operacji na danych graczy
     */
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Pobiera listę wszystkich graczy z systemu.
     *
     * @return lista wszystkich zarejestrowanych graczy
     */
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Tworzy nowego gracza w systemie.
     *
     * @param nickname nazwa użytkownika dla nowego gracza
     * @return utworzony obiekt Player
     * @throws IllegalArgumentException gdy gracz o podanej nazwie już istnieje
     */
    public Player createPlayer(String nickname) {
        // Dodatkowa walidacja, aby uniknąć duplikatów
        if (playerRepository.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("Player with nickname " + nickname + " already exists.");
        }
        Player newPlayer = new Player(nickname);
        return playerRepository.save(newPlayer);
    }

    /**
     * Wyszukuje graczy z wysokim współczynnikiem wygranych (win ratio).
     *
     * Metoda stosuje następujące kryteria:
     * 1. Gracz musi mieć rozegrane minimum 5 meczy
     * 2. Współczynnik wygranych (wins/total games) musi być większy niż 0.7 (70%)
     *
     * Wyniki są sortowane malejąco według współczynnika wygranych,
     * dzięki czemu najlepsi gracze znajdują się na początku listy.
     *
     * @return lista graczy spełniających kryteria, posortowana malejąco według współczynnika wygranych
     * @implNote W przypadku graczy bez rozegranych meczy (totalGames = 0),
     *           są oni pomijani aby uniknąć dzielenia przez zero
     */

    public List<Player> findPlayersWithHighWinRatio() {
        List<Player> allPlayers = playerRepository.findAll();

        return allPlayers.stream()
                .filter(player -> (player.getWins() + player.getLosses()) > 5) // Opcjonalnie: filtrujemy graczy z min. liczbą meczy
                .filter(player -> {
                    int totalGames = player.getWins() + player.getLosses();
                    if (totalGames == 0) {
                        return false; // Unikamy dzielenia przez zero
                    }
                    double winRatio = (double) player.getWins() / totalGames;
                    return winRatio > 0.7;
                })
                .sorted(Comparator.comparingDouble(player ->
                        -((double) player.getWins() / (player.getWins() + player.getLosses()))
                )) // Sortowanie malejąco po win ratio
                .collect(Collectors.toList());
    }
}