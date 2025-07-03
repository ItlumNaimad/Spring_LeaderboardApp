package com.example.board.controller;

import com.example.board.model.*;
import com.example.board.service.GameService;
import com.example.board.service.TeamService;
import com.example.board.service.TournamentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/tournaments")
@AllArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;
    private final TeamService teamService;
    private final GameService gameService;

    @GetMapping
    public String listTournaments(Model model) {
        model.addAttribute("tournaments", tournamentService.getAllTournaments());
        return "tournament";
    }

    @GetMapping("/new")
    public String showNewTournamentForm(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        model.addAttribute("teams", teamService.getAllTeams());
        return "tournament/create";
    }

    @PostMapping("/new")
    public String createTournament(@RequestParam String name,
                                   @RequestParam Long gameId,
                                   @RequestParam List<Long> teamIds) {
        Game game = gameService.getGameById(gameId);
        List<Team> teams = teamService.getTeamsByIds(teamIds);
        tournamentService.createTournament(name, game, teams, LocalDateTime.now());
        return "redirect:/tournaments";
    }

    @GetMapping("/{id}")
    public String showTournamentDetails(@PathVariable Long id, Model model) {
        Tournament tournament = tournamentService.getTournamentById(id);
        model.addAttribute("tournament", tournament);
        model.addAttribute("standings", tournamentService.getTournamentStandings(id));
        return "tournament/details";
    }

    @GetMapping("/{id}/matches")
    public String showTournamentMatches(@PathVariable Long id, Model model) {
        Tournament tournament = tournamentService.getTournamentById(id);
        model.addAttribute("tournament", tournament);
        model.addAttribute("matches", tournament.getMatches());
        return "tournament/matches";
    }

    @PostMapping("/matches/{matchId}")
    public String updateMatchScore(@PathVariable Long matchId,
                                   @RequestParam Integer scoreA,
                                   @RequestParam Integer scoreB,
                                   @RequestParam Long tournamentId) {
        tournamentService.updateMatchScore(matchId, scoreA, scoreB);
        return "redirect:/tournaments/" + tournamentId + "/matches";
    }
}

