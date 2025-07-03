package com.example.board.controller;

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
public class EntityManagementController {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final GameService gameService;

    @GetMapping("/manage")
    public String showManagementPage(Model model) {
        model.addAttribute("players", playerService.getAllPlayers());
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("games", gameService.getAllGames());
        return "manage-entities";
    }

    @PostMapping("/games")
    public String addGame(@RequestParam String name) {
        if (name != null && !name.trim().isEmpty()) {
            gameService.createGame(name.trim());
        }
        return "redirect:/manage";
    }

    @PostMapping("/players")
    public String addPlayer(@RequestParam String nickname) {
        if (nickname != null && !nickname.trim().isEmpty()) {
            playerService.createPlayer(nickname.trim());
        }
        return "redirect:/manage";
    }

    @PostMapping("/teams")
    public String addTeam(@RequestParam String name) {
        if (name != null && !name.trim().isEmpty()) {
            teamService.createTeam(name.trim());
        }
        return "redirect:/manage";
    }
}
