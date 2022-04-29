package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.dto.FilterDto;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private FilterDto filterDto;

    private Specification<Player> playerSpecification = new Specification<Player>() {
        @Override
        public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();
            if (filterDto.getName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterDto.getName().toLowerCase() + "%"));
            }
            if (filterDto.getTitle() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + filterDto.getTitle().toLowerCase() + "%"));
            }
            if (filterDto.getRace() != null) {
                predicates.add(criteriaBuilder.equal(root.get("race"), filterDto.getRace()));
            }
            if (filterDto.getProfession() != null) {
                predicates.add(criteriaBuilder.equal(root.get("profession"), filterDto.getProfession()));
            }
            if (filterDto.getMinExperience() < filterDto.getMaxExperience()) {
                predicates.add(criteriaBuilder.between(root.get("experience"), filterDto.getMinExperience(), filterDto.getMaxExperience()));
            } else if (filterDto.getMinExperience() != 0 && filterDto.getMaxExperience() == 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), filterDto.getMinExperience()));
            } else if (filterDto.getMinExperience() == 0 && filterDto.getMaxExperience() != 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("experience"), filterDto.getMaxExperience()));
            }

            Date before = new Date(filterDto.getBefore());
            Date after = new Date(filterDto.getAfter());
            if (filterDto.getAfter() != 0 && filterDto.getBefore() != 0 && filterDto.getAfter() < filterDto.getBefore()) {
                predicates.add(criteriaBuilder.between(root.get("birthday"), after, before));
            } else if (filterDto.getAfter() != 0 && filterDto.getBefore() == 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), after));
            } else if (filterDto.getAfter() == 0 && filterDto.getBefore() != 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), before));
            }

            if (filterDto.getBanned() != null) {
                predicates.add(criteriaBuilder.equal(root.get("banned"), filterDto.getBanned()));
            }

            if (filterDto.getMinLevel() != 0 && filterDto.getMaxLevel() != 0 && filterDto.getMinLevel() < filterDto.getMaxLevel()) {
                predicates.add(criteriaBuilder.between(root.get("level"), filterDto.getMinLevel(), filterDto.getMaxLevel()));
            } else if (filterDto.getMinLevel() != 0 && filterDto.getMaxLevel() == 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), filterDto.getMinLevel()));
            } else if (filterDto.getMinLevel() == 0 && filterDto.getMaxLevel() != 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("level"), filterDto.getMaxLevel()));
            }

            orderBy(filterDto, root, criteriaBuilder, query);

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }
    };

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void setFilterDto(FilterDto filterDto) {
        this.filterDto = filterDto;
    }

    @Override
    public Page<Player> getListPlayers(FilterDto filterDto) {
        setFilterDto(filterDto);

        Pageable pageable = PageRequest.of(filterDto.getPageNumber(), filterDto.getPageSize());

        Page<Player> page = playerRepository.findAll(playerSpecification, pageable);

        return page;
    }

    @Override
    public void savePlayer(Player player) {
        int experience = player.getExperience();
        int level = (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
        int untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
        player.setLevel(level);
        player.setUntilNextLevel(untilNextLevel);

        playerRepository.save(player);
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

    @Override
    public int playersCount(FilterDto filterDto) {
        setFilterDto(filterDto);
        if (filterDto == null) {
            return Math.toIntExact(playerRepository.count());
        }
        return Math.toIntExact(playerRepository.count(playerSpecification));
    }

    private void orderBy(FilterDto filterDto, Root<Player> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query) {
        Order order = null;
        switch (filterDto.getOrder()) {
            case ID:
                order = criteriaBuilder.asc(root.get("id"));
                break;
            case NAME:
                order = criteriaBuilder.asc(root.get("name"));
                break;
            case EXPERIENCE:
                order = criteriaBuilder.asc(root.get("experience"));
                break;
            case BIRTHDAY:
                order = criteriaBuilder.asc(root.get("birthday"));
                break;
            case LEVEL:
                order = criteriaBuilder.asc(root.get("level"));
                break;
        }
        query.orderBy(order);
    }

    public FilterDto mapRequestParamToFilterDto(
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
    ) {
        FilterDto filterDto = new FilterDto();
        filterDto.setName(name);
        filterDto.setTitle(title);
        filterDto.setRace(race);
        filterDto.setProfession(profession);
        filterDto.setAfter(after);
        filterDto.setBefore(before);
        filterDto.setBanned(banned);
        filterDto.setMinExperience(minExperience);
        filterDto.setMaxExperience(maxExperience);
        filterDto.setMinLevel(minLevel);
        filterDto.setMaxLevel(maxLevel);
        filterDto.setOrder(order);
        filterDto.setPageSize(pageSize);
        filterDto.setPageNumber(pageNumber);
        return filterDto;
    }
}
