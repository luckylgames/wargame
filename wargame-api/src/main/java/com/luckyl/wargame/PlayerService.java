package com.luckyl.wargame;

import com.luckyl.wargame.service.model.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerService {
    public void addPlayer(String name, Color color, Integer wins);
    public void removePlayer(UUID id);
    public void removePlayer(Player player);
    public Player findPlayer(UUID id);
    public List<Player> findPlayerByName(String name);
}