package com.game.controller;

import com.game.dto.FilterDto;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.service.PlayerValidateService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerValidateService playerValidateService;

    public PlayerController(PlayerService playerService, PlayerValidateService playerValidateService) {
        this.playerService = playerService;
        this.playerValidateService = playerValidateService;
    }

    @GetMapping("/players")
    public List<Player> getPlayersList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false, defaultValue = "0") Long after,
            @RequestParam(value = "before", required = false, defaultValue = "0") Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false, defaultValue = "0") Integer minExperience,
            @RequestParam(value = "maxExperience", required = false, defaultValue = "0") Integer maxExperience,
            @RequestParam(value = "minLevel", required = false, defaultValue = "0") Integer minLevel,
            @RequestParam(value = "maxLevel", required = false, defaultValue = "0") Integer maxLevel,
            @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize
    ) {
        FilterDto filterDto = playerService.mapRequestParamToFilterDto(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel, order, pageSize, pageNumber);

        return playerService.getListPlayers(filterDto).getContent();
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

    @GetMapping("/players/count")
    public int getPlayersCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false, defaultValue = "0") Long after,
            @RequestParam(value = "before", required = false, defaultValue = "0") Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false, defaultValue = "0") Integer minExperience,
            @RequestParam(value = "maxExperience", required = false, defaultValue = "0") Integer maxExperience,
            @RequestParam(value = "minLevel", required = false, defaultValue = "0") Integer minLevel,
            @RequestParam(value = "maxLevel", required = false, defaultValue = "0") Integer maxLevel,
            @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize
    ) {
        FilterDto filterDto = playerService.mapRequestParamToFilterDto(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize);
        return playerService.playersCount(filterDto);
    }
}
