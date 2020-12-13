package com.luckyl.wargame.data;


import com.luckyl.wargame.Color;
import com.luckyl.wargame.provider.PieceProvider;
import com.luckyl.wargame.provider.RemainingProvider;
import com.luckyl.wargame.service.model.Piece;
import com.luckyl.wargame.service.model.PieceId;
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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class),
    properties = {"logging.level.org.hibernate.SQL=DEBUG", "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PieceRepositoryTest {
    @Autowired
    private PieceRepository repository;

    @ParameterizedTest
    @ArgumentsSource(PieceProvider.class)
    public void save(Piece piece) {
        repository.save(piece);

        val pieceId = new PieceId(piece.getColor(), piece.getRank());
        val pieces = repository.getPiecesByColor(Color.fromRgb(pieceId.getColor()));
        val pieceById = repository.getPiece(pieceId).get();
        val pieceByColorRank = repository.getPiece(Color.fromRgb(piece.getColor()), piece.getRank()).get();

        assertNotNull(piece.getVersion());
        assertEquals(piece, pieceById);
        assertEquals(piece, pieceByColorRank);
        assertThat(pieces, contains(piece));
    }

    @ParameterizedTest
    @ArgumentsSource(PieceProvider.class)
    public void delete(Piece piece) {
        repository.save(piece);
        val count = repository.count();
        repository.delete(piece);

        assertThat(count, greaterThan(repository.count()));
    }

    @ParameterizedTest
    @ArgumentsSource(PieceProvider.class)
    public void getPiece(Piece piece) {
        val id = new PieceId(piece.getColor(), piece.getRank());
        repository.save(piece);
        val actual = repository.getPiece(id).get();

        assertEquals(piece, actual);
    }

    @ParameterizedTest
    @ArgumentsSource(PieceProvider.class)
    public void getPieces(Piece piece) {
        repository.save(piece);
        val pieces = repository.getPieces();

        assertThat(pieces, contains(piece));
    }

    @ParameterizedTest
    @ArgumentsSource(PieceProvider.class)
    public void getPiecesByColor(Piece piece) {
        repository.save(piece);
        val color = Color.fromRgb(piece.getColor());
        val pieces = repository.getPiecesByColor(color);

        assertThat(pieces, contains(piece));
    }

    @ParameterizedTest
    @ArgumentsSource(PieceProvider.class)
    public void getPiecesByRank(Piece piece) {
        repository.save(piece);
        val rank = piece.getRank();
        val pieces = repository.getPiecesByRank(rank);

        assertThat(pieces, contains(piece));
    }

    @ParameterizedTest
    @ArgumentsSource(RemainingProvider.class)
    public void getDepletedPieces(List<Piece> remainingPieces) {
        remainingPieces.forEach(repository::save);
        val colorRgb = remainingPieces.get(0).getColor();
        val color = Color.fromRgb(colorRgb);
        val actual = repository.getDepletedPieces(color);
        val equalsAndGreaterThanZeroCount =
            actual.stream()
                .filter(p -> p.getMax_count().equals(p.getCurrent_count()) && p.getCurrent_count() > 0 )
                .count();

        assertThat(actual, hasItems(hasProperty("color", equalTo(colorRgb))));
        assertThat(Integer.toUnsignedLong(actual.size()), greaterThanOrEqualTo(equalsAndGreaterThanZeroCount));
    }

    @ParameterizedTest
    @ArgumentsSource(RemainingProvider.class)
    public void getRemainingPieces(List<Piece> remainingPieces) {
        remainingPieces.forEach(repository::save);
        remainingPieces.forEach(p->p.setCurrent_count(p.getMax_count()-1));
        val colorRgb = remainingPieces.get(0).getColor();
        val color = Color.fromRgb(colorRgb);
        val actual = repository.getRemainingPieces(color);

        val currentLessThanMax =
            actual.stream()
                .filter(p -> p.getCurrent_count() < p.getMax_count())
                .count();

        assertThat(actual, hasItems(hasProperty("color", equalTo(colorRgb))));
        assertThat(Integer.toUnsignedLong(actual.size()), greaterThanOrEqualTo(currentLessThanMax));
    }
}