package com.luckyl.wargame.service;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.PlayerService;
import com.luckyl.wargame.data.PlayerRepository;
import com.luckyl.wargame.provider.PlayerProvider;
import com.luckyl.wargame.service.model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class),
    properties = {"logging.level.org.hibernate.SQL=DEBUG", "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ComponentScans({
    @ComponentScan("com.luckyl.wargame.service"),
    @ComponentScan("com.luckyl.wargame.provider")})
class PlayerServiceImplTest {
  @Autowired
  PlayerRepository playerRepository;

  @Autowired
  PlayerService service;

  @ParameterizedTest
  @ArgumentsSource(PlayerProvider.class)
  void addPlayer(Player player) {
    String name = player.getName();
    Color color = Color.fromRgb(player.getColor());
    Integer wins = player.getWins();

    service.addPlayer(name, color, wins);
    assertThat(playerRepository.getPlayersByName(name))
        .hasSize(1)
        .allSatisfy(p -> {
          assertThat(p.getName().equalsIgnoreCase(name));
          assertThat(p.getColor().equalsIgnoreCase(color.getRgb()));
          assertThat(p.getWins().equals(wins));
        });
  }

  @ParameterizedTest
  @ArgumentsSource(PlayerProvider.class)
  void addPlayer_preExisting(Player player) {
    String name = player.getName();
    Color color = Color.fromRgb(player.getColor());
    Integer wins = player.getWins();

    service.addPlayer(name, color, wins);
    service.addPlayer(name, color, wins);

    assertThat(playerRepository.getPlayersByName(name))
        .hasSize(1)
        .allSatisfy(p -> {
          assertThat(p.getName().equalsIgnoreCase(name));
          assertThat(p.getColor().equalsIgnoreCase(color.getRgb()));
          assertThat(p.getWins().equals(wins));
        });
  }

  @Test
  void removePlayer() {
  }

  @Test
  void testRemovePlayer() {
  }

  @Test
  void findPlayer() {
  }

  @Test
  void findPlayerByName() {
  }
}