package com.luckyl.wargame.data;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.provider.LocationProvider;
import com.luckyl.wargame.service.model.Location;
import com.luckyl.wargame.service.model.LocationId;
import lombok.val;
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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class),
    properties = {"logging.level.org.hibernate.SQL=DEBUG", "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class LocationRepositoryTest {
    @Autowired
    private LocationRepository repository;

    @ParameterizedTest
    @ArgumentsSource(LocationProvider.class)
    public void save(Location location) {
        repository.save(location);

        val locationsByPiece = repository.getLocationsByPiece(location.getPiece());
        val color = Color.fromRgb(location.getPiece().getColor());
        val locationsByColor = repository.getLocationsByColor(color);
        val actualId = repository.getId(location);
        val locationId = new LocationId(location.getPiece_row(), location.getPiece_col());

        assertNotNull(location.getVersion());
        assertThat(locationsByPiece, containsInAnyOrder(location));
        assertThat(locationsByColor, containsInAnyOrder(location));
        assertEquals(locationId, actualId);
    }

    @ParameterizedTest
    @ArgumentsSource(LocationProvider.class)
    public void delete(Location location) {
        repository.save(location);
        val count = repository.count();
        repository.delete(location);

        assertThat(count, greaterThan(repository.count()));
    }

    @ParameterizedTest
    @ArgumentsSource(LocationProvider.class)
    public void getId(Location location) {
        val expected = new LocationId(location.getPiece_row(), location.getPiece_col());
        repository.save(location);
        val actual = repository.getId(location);

        assertEquals(actual, expected);
    }

    @ParameterizedTest
    @ArgumentsSource(LocationProvider.class)
    public void getLocation(Location location) {
        val locationId = new LocationId(location.getPiece_row(), location.getPiece_col());
        repository.save(location);
        val actual = repository.getLocation(locationId).get();

        assertEquals(actual, location);
    }

    @ParameterizedTest
    @ArgumentsSource(LocationProvider.class)
    public void getLocationsByColor(Location location) {
        val expected = location.getPiece().getColor();
        repository.save(location);
        val locations = repository.getLocationsByColor(Color.fromRgb(expected));

        assertThat(locations, not(empty()));
        assertThat(locations, containsInAnyOrder(location));
    }

    @ParameterizedTest
    @ArgumentsSource(LocationProvider.class)
    public void getLocationsByPiece(Location location) {
        val expected = location.getPiece();
        repository.save(location);
        val locations = repository.getLocationsByPiece(expected);

        assertThat(locations, not(empty()));
        assertThat(locations, containsInAnyOrder(location));
    }
}