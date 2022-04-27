package com.game.service;

import com.game.entity.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {

    List<Player> findAllPlayers(Map<String, String> allParams, Integer limit);

    void savePlayer(Player player);

    void deletePlayer(Player player);

    Player getPlayerById(Long id);
}
