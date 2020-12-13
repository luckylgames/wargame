package com.luckyl.wargame.data;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.provider.PlayerProvider;
import com.luckyl.wargame.service.model.PieceId;
import com.luckyl.wargame.service.model.Player;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class),
    properties = {"logging.level.org.hibernate.SQL=DEBUG", "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PlayerRepositoryTest {
    @Autowired
    private PlayerRepository repository;

    @ParameterizedTest
    @ArgumentsSource(PlayerProvider.class)
    public void save(Player player) {
        repository.save(player);

        val playerById = repository.getPlayer(player.getId());
        val playerByColor = repository.getPlayerByColor(Color.fromRgb(player.getColor()));
        val playersByName = repository.getPlayersByName(player.getName());
        val players = repository.getPlayers();

        assertNotNull(player.getVersion());
        assertNotNull(player.getId());
        assertEquals(playerById, player);
        assertEquals(playerByColor, player);
        assertThat(players, contains(player));
        assertThat(playersByName, contains(player));
    }

    @ParameterizedTest
    @ArgumentsSource(PlayerProvider.class)
    public void delete(Player player) {
        repository.save(player);
        val count = repository.count();
        repository.delete(player);

        assertThat(count, greaterThan(repository.count()));
    }

    @ParameterizedTest
    @ArgumentsSource(PlayerProvider.class)
    public void getPlayer(Player player) {
        repository.save(player);
        val actual = repository.getPlayer(player.getId());

        assertEquals(player, actual);
    }

    @ParameterizedTest
    @ArgumentsSource(PlayerProvider.class)
    public void getPlayerByColor(Player player) {
        repository.save(player);
        val actual = repository.getPlayerByColor(Color.fromRgb(player.getColor()));

        assertEquals(player, actual);
    }

    @ParameterizedTest
    @ArgumentsSource(PlayerProvider.class)
    public void getPlayersByName(Player player) {
        repository.save(player);
        val actual = repository.getPlayersByName(player.getName());

        assertEquals(player, actual);
    }

    @ParameterizedTest
    @ArgumentsSource(PlayerProvider.class)
    public void getPlayers(Player player) {
        repository.save(player);
        val actual = repository.getPlayers();

        assertThat(actual, contains(player));
    }
}