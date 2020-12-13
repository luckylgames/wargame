package com.luckyl.wargame.data;

import com.luckyl.wargame.service.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PlayerDao extends JpaRepository<Player, UUID> {
    @Query("SELECT DISTINCT p FROM Player p WHERE p.color = :color")
    Player getPlayerByColor(@Param("color") String color);

    @Query("SELECT p FROM Player p WHERE p.name = :name")
    List<Player> getPlayersByName(@Param("name") String name);
}
