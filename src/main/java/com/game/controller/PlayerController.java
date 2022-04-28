package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import com.game.service.PlayerValidateService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerValidateService playerValidateService;

    public PlayerController(PlayerService playerService, PlayerValidateService playerValidateService) {
        this.playerService = playerService;
        this.playerValidateService = playerValidateService;
    }

    @PostMapping("/players")
    public Player createPlayer(@RequestBody Player player) {
        playerValidateService.isBirthdayValid(player);
        playerValidateService.isExperienceValid(player);
        playerService.savePlayer(player);
        return player;
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        playerValidateService.isIdValid(id);
        Player newPlayer = playerService.getPlayerById(id);
        if (player.getName() != null && playerValidateService.isNameValid(player)) {
            newPlayer.setName(player.getName());
        }
        if (player.getTitle() != null && playerValidateService.isTitleValid(player)) {
            newPlayer.setTitle(player.getTitle());
        }
        if (player.getRace() != null) {
            newPlayer.setRace(player.getRace());
        }
        if (player.getProfession() != null) {
            newPlayer.setProfession(player.getProfession());
        }
        if (player.getBirthday() != null && playerValidateService.isBirthdayValid(player)) {
            newPlayer.setBirthday(player.getBirthday());
        }
        if (player.getBanned() != null) {
            newPlayer.setBanned(player.getBanned());
        }
        if (player.getExperience() != null && playerValidateService.isExperienceValid(player)) {
            newPlayer.setExperience(player.getExperience());
            int experience = player.getExperience();
            int level = (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
            int untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
            newPlayer.setUntilNextLevel(untilNextLevel);
        }
        playerService.savePlayer(newPlayer);
        return newPlayer;
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerValidateService.isIdValid(id);
        Player player = playerService.getPlayerById(id);
        playerService.deletePlayer(player);
    }

    @GetMapping("/players/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        playerValidateService.isIdValid(id);
        return playerService.getPlayerById(id);
    }
}
