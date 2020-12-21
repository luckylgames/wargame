package com.luckyl.wargame.provider;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.service.model.Player;
import lombok.val;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class PlayerProvider implements ArgumentsProvider {

  final List<Player> players = createPlayers();

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
    return players.stream().map(Arguments::of);
  }

  List<Player> createPlayers() {
    List<Player> list = new ArrayList<>();

    for(int i=0; i<10; i++)
      list.add(createPlayer());

    return list;
  }

  Player createPlayer() {
    val player = new Player();
    val nameList = new ArrayList<>(Arrays.asList("Kevin", "April", "Jake", "Dylan", "Kalina", "Perry", "Liam"));
    val nameIdx = ThreadLocalRandom.current().nextInt(0, nameList.size());
    val wins = ThreadLocalRandom.current().nextInt(0, 5);
    val colorIdx = ThreadLocalRandom.current().nextInt(0, Color.values().length-1);
    val color = Color.values()[colorIdx];

    player.setColor(color.getRgb());
    player.setName(nameList.get(nameIdx));
    player.setWins(wins);
    player.setId(UUID.randomUUID());

    return player;
  }
}