package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.dto.FilterDto;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;

public interface PlayerService {

    Page<Player> getListPlayers(FilterDto filterDto);

    void savePlayer(Player player);

    void deletePlayer(Player player);

    Player getPlayerById(Long id);

    int playersCount(FilterDto filterDto);

    FilterDto mapRequestParamToFilterDto(
            String name,
            String title,
            Race race,
            Profession profession,
            Long after,
            Long before,
            Boolean banned,
            Integer minExperience,
            Integer maxExperience,
            Integer minLevel,
            Integer maxLevel,
            PlayerOrder order,
            Integer pageSize,
            Integer pageNumber
    );
}
