package com.example.board.controller;

import com.example.board.service.GameMatchService;
import com.example.board.service.GameService;
import com.example.board.service.PlayerService;
import com.example.board.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class GameController {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final GameService gameService;
    private final GameMatchService gameMatchService;

    @GetMapping("/rankings")
    public String showRankings(@RequestParam(required = false) String type,
                               @RequestParam(required = false) Long gameId,
                               Model model) {
        model.addAttribute("games", gameService.getAllGames());

        if ("teams".equals(type)) {
            model.addAttribute("teams", teamService.getAllTeams());
            model.addAttribute("currentView", "teams");
            model.addAttribute("pageTitle", "Ranking Drużyn");
        } else if ("top-players".equals(type)) {
            model.addAttribute("players", playerService.findPlayersWithHighWinRatio());
            model.addAttribute("currentView", "top-players");
            model.addAttribute("pageTitle", "Ranking Mistrzów");
        } else {
            model.addAttribute("players", playerService.getAllPlayers());
            model.addAttribute("currentView", "players");
            model.addAttribute("pageTitle", "Ranking Graczy");
        }

        return "ranking";
    }

    @GetMapping("/")
    public String showMainPage(Model model) {
        return showRankings("players", null, model);
    }

    @GetMapping("/top-players")
    public String showTopPlayers(Model model) {
        model.addAttribute("players", playerService.findPlayersWithHighWinRatio());
        model.addAttribute("pageTitle", "Ranking Mistrzów (Win Ratio > 70%)");
        return "ranking";
    }

    @GetMapping("/matches/new")
    public String showAddMatchForm(Model model) {
        model.addAttribute("players", playerService.getAllPlayers());
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("games", gameService.getAllGames());
        return "formularz-meczu";
    }

    @PostMapping("/matches/player")
    public String addPlayerMatch(@RequestParam Long gameId,
                                 @RequestParam Long winnerPlayerId,
                                 @RequestParam Long loserPlayerId) {
        gameMatchService.addPlayerMatch(gameId, winnerPlayerId, loserPlayerId);
        return "redirect:/";
    }

    @PostMapping("/matches/team")
    public String addTeamMatch(@RequestParam Long gameId,
                               @RequestParam Long winnerTeamId,
                               @RequestParam Long loserTeamId) {
        gameMatchService.addTeamMatch(gameId, winnerTeamId, loserTeamId);
        return "redirect:/";
    }

}
