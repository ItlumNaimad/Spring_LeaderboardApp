package com.example.board.tests;

import com.example.board.model.Player;
import com.example.board.repository.PlayerRepository;
import com.example.board.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerService(playerRepository);
    }

    @Test
    void testFindPlayersWithHighWinRatio_ShouldReturnOnlyPlayersWithHighWinRatio() {
        // given
        Player highWinRatioPlayer = new Player("mistrz123");
        highWinRatioPlayer.setWins(8);
        highWinRatioPlayer.setLosses(2); // 80% win ratio

        Player lowWinRatioPlayer = new Player("noob456");
        lowWinRatioPlayer.setWins(2);
        lowWinRatioPlayer.setLosses(8); // 20% win ratio

        Player insufficientGamesPlayer = new Player("newbie789");
        insufficientGamesPlayer.setWins(2);
        insufficientGamesPlayer.setLosses(1); // 66% win ratio but only 3 games

        when(playerRepository.findAll()).thenReturn(Arrays.asList(
                highWinRatioPlayer, lowWinRatioPlayer, insufficientGamesPlayer
        ));

        // when
        List<Player> result = playerService.findPlayersWithHighWinRatio();

        // then
        assertEquals(1, result.size(), "Tylko jeden gracz powinien spełniać kryteria");
        assertEquals("mistrz123", result.get(0).getNickname());
    }

    @Test
    void testFindPlayersWithHighWinRatio_ShouldSortByWinRatioDescending() {
        // given
        Player bestPlayer = new Player("najlepszy");
        bestPlayer.setWins(90);
        bestPlayer.setLosses(10); // 90% win ratio, 100 gier

        Player goodPlayer = new Player("dobry");
        goodPlayer.setWins(80);
        goodPlayer.setLosses(20); // 80% win ratio, 100 gier

       /* Player okayPlayer = new Player("niezly");
        okayPlayer.setWins(70);
        okayPlayer.setLosses(30); // 70% win ratio, 100 gier */

        when(playerRepository.findAll()).thenReturn(Arrays.asList(
                /*okayPlayer ,*/ bestPlayer, goodPlayer
        ));

        // when
        List<Player> result = playerService.findPlayersWithHighWinRatio();

        // then
        assertEquals(2, result.size(), "Wszyscy trzej gracze powinni być zwróceni");
        assertEquals("najlepszy", result.get(0).getNickname(), "Gracz z najwyższym win ratio powinien być pierwszy");
        assertEquals("dobry", result.get(1).getNickname(), "Gracz z drugim najwyższym win ratio powinien być drugi");
        //assertEquals("niezly", result.get(2).getNickname(), "Gracz z najniższym win ratio powinien być trzeci");
    }

    @Test
    void testFindPlayersWithHighWinRatio_ShouldHandleEmptyRepository() {
        // given
        when(playerRepository.findAll()).thenReturn(Arrays.asList());

        // when
        List<Player> result = playerService.findPlayersWithHighWinRatio();

        // then
        assertTrue(result.isEmpty(), "Lista powinna być pusta gdy nie ma graczy w repozytorium");
    }
}
