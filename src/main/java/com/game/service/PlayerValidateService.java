package com.game.service;

import com.game.exception.InvalidIdException;
import com.game.exception.NoSuchPlayerException;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerValidateService {

    private final PlayerRepository playerRepository;

    public PlayerValidateService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void validateId(Long id) {
        if (id <= 0) {
            throw new InvalidIdException("Invalid id");
        } else if (id > playerRepository.count() || !playerRepository.findById(id).isPresent()) {
            throw new NoSuchPlayerException("Player with id " + id + " doesn't exist");
        }
    }
}
