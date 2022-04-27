package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PlayerServiceImpl implements  PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> findAllPlayers(Map<String, String> allParams, Integer limit) {
        return null;
    }

    @Override
    public void savePlayer(Player player) {

    }

    @Override
    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    @Override
    public Player getPlayerById(Long id) {
        Player player = null;
        Optional<Player> optional = playerRepository.findById(id);
        if (optional.isPresent()) {
            player = optional.get();
        }
        return player;
    }
}
