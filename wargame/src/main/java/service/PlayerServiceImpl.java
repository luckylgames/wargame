package service;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.PlayerService;
import com.luckyl.wargame.data.PlayerRepository;
import com.luckyl.wargame.service.model.Player;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public void addPlayer(String name, Color color, Integer wins) {
        val player = new Player();
        player.setName(name);
        player.setColor(color.getRgb());
        player.setWins(wins);

        playerRepository.save(player);
    }

    @Override
    public void removePlayer(UUID id) {
        Player player = new Player();
        player.setId(id);
        removePlayer(player);
    }

    @Override
    public void removePlayer(Player player) {
        val deletePlayer = getExistingPlayer(player);
        if (deletePlayer != null)
            playerRepository.delete(deletePlayer);
    }

    @Override
    public Player findPlayer(UUID id) {
        return playerRepository.getPlayer(id).orElse(null);
    }

    @Override
    public List<Player> findPlayerByName(String name) {
        return playerRepository.getPlayersByName(name);
    }

    private Player getExistingPlayer(@NonNull Player player) {
        val id = player.getId();
        return id == null ?
            null :
            playerRepository.getPlayer(id).orElse(null);
    }
}
