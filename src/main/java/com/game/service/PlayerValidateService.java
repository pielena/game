package com.game.service;

import com.game.entity.Player;
import com.game.exception.InvalidIdException;
import com.game.exception.NoSuchPlayerException;
import com.game.exception.ValidationException;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class PlayerValidateService {

    private final PlayerRepository playerRepository;

    public PlayerValidateService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public boolean isIdValid(Long id) {
        if (id <= 0) {
            throw new InvalidIdException("Invalid id");
        } else if (id > playerRepository.count() || !playerRepository.findById(id).isPresent()) {
            throw new NoSuchPlayerException("Player with id " + id + " doesn't exist");
        }
        return true;
    }

    public boolean isNameValid(Player player) {
        if (player.getName().length() > 12) {
            throw new ValidationException("Invalid name");
        }
        return true;
    }

    public boolean isTitleValid(Player player) {
        if (player.getTitle().length() > 30) {
            throw new ValidationException("Invalid title");
        }
        return true;
    }

    public  boolean isExperienceValid(Player player) {
        if(player.getExperience()<0 || player.getExperience() > 10_000_000) {
            throw new ValidationException("Invalid Experience");
        }
        return true;
    }

    public boolean isBirthdayValid(Player player) {
        if (player.getBirthday().getTime() < 0) {
            throw new ValidationException("Invalid birthday");
        }
        Calendar start = new GregorianCalendar(2000, Calendar.JANUARY, 01);
        Calendar end = new GregorianCalendar(3000, Calendar.DECEMBER, 31);
        if (player.getBirthday().before(start.getTime()) || player.getBirthday().after(end.getTime())) {
            throw new ValidationException("Invalid birthday");
        }
        return true;
    }
}
