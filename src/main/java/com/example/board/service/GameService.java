package com.example.board.service;

import com.example.board.model.Game;
import com.example.board.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(String name) {
        if (gameRepository.existsByName(name)) {
            throw new IllegalArgumentException("Gra o nazwie " + name + " już istnieje.");
        }
        return gameRepository.save(new Game(name));
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono gry o id " + gameId));
    }
}