package com.luckyl.wargame.provider;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.service.model.Location;
import com.luckyl.wargame.service.model.Piece;
import lombok.val;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class LocationProvider implements ArgumentsProvider {

  final List<Piece> pieces = createPieces();
  final List<Location> locations = createLocations();

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
    return locations.stream().map(Arguments::of);
  }

  List<Piece> createPieces() {
    final List<Piece> list = new ArrayList<>();

    for(int i=0; i<10; i++)
      list.add(createPiece(i));

    return list;
  }

  List<Location> createLocations() {
    final List<Location> list = new ArrayList<>();

    for(int i=0; i<10; i++)
      list.add(createLocation());

    return list;
  }


  Piece createPiece(int rank) {
    val piece = new Piece();
    val max = ThreadLocalRandom.current().nextInt(1, 11);
    val current = ThreadLocalRandom.current().nextInt(0, 11);
    val colorIdx = ThreadLocalRandom.current().nextInt(0, Color.values().length-1);
    val color = Color.values()[colorIdx];

    piece.setRank(rank);
    piece.setMax_count(max);
    piece.setCurrent_count(current);
    piece.setColor(color.getRgb());

    return piece;
  }

  Location createLocation() {
    val location = new Location();
    val row = ThreadLocalRandom.current().nextInt(1, 11);
    val col = ThreadLocalRandom.current().nextInt(1, 11);
    val pieceIdx = ThreadLocalRandom.current().nextInt(0, pieces.size());
    val piece = pieces.get(pieceIdx);

    location.setPiece_row(row);
    location.setPiece_col(col);
    location.setPiece(piece);

    return location;
  }

}