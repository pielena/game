package com.game.controller;

import com.game.entity.Player;
import com.game.exception.InvalidIdException;
import com.game.service.PlayerService;
import com.game.service.PlayerValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerValidateService.validateId(id);
        Player player = playerService.getPlayerById(id);
        playerService.deletePlayer(player);
    }

    @GetMapping("players/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        playerValidateService.validateId(id);
        return playerService.getPlayerById(id);
    }
}
