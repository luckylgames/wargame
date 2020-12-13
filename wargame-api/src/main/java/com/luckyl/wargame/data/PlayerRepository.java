package com.luckyl.wargame.data;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.service.model.Player;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerRepository {
    private final PlayerDao dao;

    @Transactional
    public Player save(@NonNull Player player) {
       return dao.save(player);
    }

    @Transactional
    public void delete(@NonNull Player player) {
        dao.delete(player);
    }

    public Optional<Player> getPlayer(@NonNull UUID id) { return dao.findById(id); }

    public Player getPlayerByColor(@NonNull Color color) {
        return dao.getPlayerByColor(color.getRgb());
    }

    public List<Player> getPlayersByName(@NonNull String name) {
        return dao.getPlayersByName(name);
    }

    public List<Player> getPlayers() {
        return dao.findAll();
    }

    public long count() {
        return dao.count();
    }
}
