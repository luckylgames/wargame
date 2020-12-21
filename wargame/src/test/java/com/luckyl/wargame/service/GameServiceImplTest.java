//package com.luckyl.wargame.service;
//
//import com.luckyl.wargame.Color;
//import com.luckyl.wargame.GameService;
//import com.luckyl.wargame.data.LocationRepository;
//import com.luckyl.wargame.data.PieceRepository;
//import com.luckyl.wargame.exception.MoveException;
//import com.luckyl.wargame.provider.LocationProvider;
//import com.luckyl.wargame.provider.PieceProvider;
//import com.luckyl.wargame.provider.RemainingProvider;
//import com.luckyl.wargame.provider.TwoLocationProvider;
//import com.luckyl.wargame.service.model.Location;
//import com.luckyl.wargame.service.model.Piece;
//import lombok.NonNull;
//import lombok.val;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ArgumentsSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.ComponentScans;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.data.util.Pair;
//import org.springframework.stereotype.Repository;
//import org.springframework.test.annotation.DirtiesContext;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.containsInAnyOrder;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@DataJpaTest(
//    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class),
//    properties = {"logging.level.org.hibernate.SQL=DEBUG", "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//@ComponentScans({
//    @ComponentScan("com.luckyl.wargame.service"),
//    @ComponentScan("com.luckyl.wargame.provider")})
//class GameServiceImplTest {
//  @Autowired
//  PieceRepository pieceRepository;
//
//  @Autowired
//  LocationRepository locationRepository;
//
//  @Autowired
//  GameService service;
//
//  @ParameterizedTest
//  @ArgumentsSource(RemainingProvider.class)
//  public void reset(List<Piece> pieces) {
//      pieces.forEach(piece -> pieceRepository.save(piece));
//      service.reset();
//      pieces.forEach(piece -> piece.setCurrent_count(0));
//      List<Piece> actual = pieceRepository.getPieces();
//
//      assertEquals(pieces, actual);
//    }
//
//  @ParameterizedTest
//  @ArgumentsSource(TwoLocationProvider.class)
//  void attack_win(Pair<Location, Location> pair) {
//    val myLocation = pair.getFirst();
//    val opponentLocation = pair.getSecond();
//    val myPiece = myLocation.getPiece();
//    val opponent = opponentLocation.getPiece();
//
//    myPiece.setRank(opponent.getRank() - 1);
//
//    pieceRepository.save(myPiece);
//    pieceRepository.save(opponent);
//    locationRepository.save(myLocation);
//    locationRepository.save(opponentLocation);
//
//    try {
//      service.attack(myLocation, opponentLocation);
//
//      val opponentLocations = locationRepository.getLocationsByPiece(opponent);
//      val locationsAfter = locationRepository.getLocationsByPiece(myPiece);
//      val myLocationExists = locationRepository.getLocations().contains(myLocation);
//
//      assertTrue(opponentLocations.isEmpty());
//      assertFalse(myLocationExists);
//      assertThat(locationsAfter, containsInAnyOrder(opponentLocation));
//    }
//    catch (MoveException e) {
//      // Do nothing on exception since we're testing happy path only
//      return;
//    }
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(LocationProvider.class)
//  void placePiece(Location location) {
//    val piece = location.getPiece();
//
//    locationRepository.save(location);
//    service.placePiece(location, piece);
//
//    val actualLocations = locationRepository.getLocationsByPiece(piece);
//    val rank = piece.getRank();
//    val color = Color.fromRgb(piece.getColor());
//    val actualPiece = pieceRepository.getPiece(color, rank);
//
//    assertThat(actualLocations, containsInAnyOrder(location));
//    assertTrue(actualPiece.isPresent());
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(PieceProvider.class)
//  void addPiece(Piece piece) {
//    service.addPiece(piece);
//    val pieceSaved = pieceRepository.getPieces().contains(piece);
//
//    assertTrue(pieceSaved);
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(PieceProvider.class)
//  void removePiece(Piece piece) {
//    pieceRepository.save(piece);
//    service.removePiece(piece);
//    val rank = piece.getRank();
//    val color = Color.fromRgb(piece.getColor());
//    val actual = pieceRepository.getPiece(color, rank);
//
//    assertTrue(actual.isEmpty());
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(LocationProvider.class)
//  void addLocation(Location location) {
//    service.addLocation(location);
//    val locationFound = locationRepository.getLocations().contains(location);
//
//    assertTrue(locationFound);
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(LocationProvider.class)
//  void removeLocation(Location location) {
//    locationRepository.save(location);
//    service.removeLocation(location);
//    val locationFound = locationRepository.getLocations().contains(location);
//
//    assertFalse(locationFound);
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(RemainingProvider.class)
//  void isGameOver_blue(List<Piece> pieces) {
//    makeListOneColor(pieces, Color.BLUE);
//    pieces.get(pieces.size() - 1).setColor(Color.RED.getRgb());
//    pieces.stream().dropWhile(p-> Color.fromRgb(p.getColor()).isBlue());
//
//    assertTrue(service.isGameOver());
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(RemainingProvider.class)
//  void isGameOver_red(List<Piece> pieces) {
//    makeListOneColor(pieces, Color.RED);
//    pieces.get(pieces.size() - 1).setColor(Color.BLUE.getRgb());
//    pieces.stream().dropWhile(p-> Color.fromRgb(p.getColor()).isRed());
//
//    assertTrue(service.isGameOver());
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(RemainingProvider.class)
//  void isGameOver_notOver(List<Piece> pieces) {
//    makeListOneColor(pieces, Color.RED);
//    pieces.get(pieces.size() - 1).setColor(Color.BLUE.getRgb());
//
//    assertTrue(service.isGameOver());
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(TwoLocationProvider.class)
//  void cantLocatePiece(Pair<Location, Location> pair) {
//    Location first = pair.getFirst();
//    Location second = pair.getSecond();
//    first.setPiece(null);
//
//    assertThatExceptionOfType(MoveException.class).isThrownBy(() -> {
//      service.attack(first, second);
//    });
//  }
//
//  @ParameterizedTest
//  @ArgumentsSource(TwoLocationProvider.class)
//  void attackSameTeam(Pair<Location, Location> pair) {
//    final Location first = pair.getFirst();
//    final Location second = pair.getSecond();
//    final Piece pFirst = first.getPiece();
//    final Piece pSecond = second.getPiece();
//    final Piece pEnemy = new Piece();
//
//    pFirst.setColor(Color.BLUE.toString());
//    pSecond.setColor(Color.BLUE.toString());
//    pEnemy.setColor(Color.RED.toString());
//    pEnemy.setRank(1);
//    pEnemy.setMax_count(20);
//    pEnemy.setCurrent_count(1);
//
//    // Make sure there are pieces remaining not to trigger game over
//    pieceRepository.save(pFirst);
//    pieceRepository.save(pSecond);
//    pieceRepository.save(pEnemy);
//
//    assertThatExceptionOfType(MoveException.class).isThrownBy(() -> {
//      service.attack(first, second);
//    });
//
//    // Empty repository again
//    pieceRepository.delete(pFirst);
//    pieceRepository.delete(pSecond);
//    pieceRepository.delete(pEnemy);
//  }
//
//
//  private void makeListOneColor(@NonNull List<Piece> pieces, @NonNull Color color) {
//    pieces.forEach(piece -> piece.setColor(color.getRgb()));
//    val flippedColor = color.flip().getRgb();
//    pieces.get(0).setColor(flippedColor);
//    pieces.get(1).setColor(flippedColor);
//  }
//}