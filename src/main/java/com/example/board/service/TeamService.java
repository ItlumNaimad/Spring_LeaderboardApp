package com.example.board.service;

import com.example.board.model.Team;
import com.example.board.model.Player;
import com.example.board.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team createTeam(String name) {
        if (teamRepository.existsByName(name)) {
            throw new IllegalArgumentException("Drużyna o nazwie " + name + " już istnieje.");
        }
        return teamRepository.save(new Team(name));
    }

    public void addPlayerToTeam(Team team, Player player) {
        team.addPlayer(player);
        teamRepository.save(team);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Team> findTeamsWithHighWinRatio() {
         List<Team> allTeams = teamRepository.findAll();

        return allTeams.stream()
                .sorted(Comparator.comparingDouble(player ->
                        (player.getWins() + player.getLosses())
                )) // Sortowanie malejąco po win ratio
                .collect(Collectors.toList());
    }

    public List<Team> getTeamsByIds(List<Long> teamIds) {
        return teamRepository.findAllById(teamIds);
    }
}