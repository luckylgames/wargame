package com.luckyl.wargame.provider;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.service.model.Location;
import com.luckyl.wargame.service.model.Piece;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class TwoLocationProvider implements ArgumentsProvider {
  final List<Integer> ranksRed = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
  final List<Integer> ranksBlue = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
  final List<Pair<Location, Location>> pieces = createLocations();

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
    return pieces.stream().map(Arguments::of);
  }

  List<Pair<Location, Location>> createLocations() {
    List<Pair<Location, Location>> list = new ArrayList<>();

    for(int i=0; i< 10; i++) {
      val location1 = createLocation();
      val location2 = createLocation();
      val location1Piece = location1.getPiece();
      val location2Piece = location2.getPiece();

      if(StringUtils.equalsIgnoreCase(location1Piece.getColor(), location2Piece.getColor())) {
        val oppositeColor =
            Color.fromRgb(location1Piece.getColor())
                .opposite()
                .getRgb();
        location2Piece.setColor(oppositeColor);
      }

      val pair = Pair.of(location1, location2);
      list.add(pair);
    }

    return list;
  }

  Location createLocation() {
    val location = new Location();
    val col = ThreadLocalRandom.current().nextInt(0, 20);
    val row = ThreadLocalRandom.current().nextInt(0, 20);

    location.setPiece(createPiece());
    location.setPiece_col(col);
    location.setPiece_row(row);

    return location;
  }

  Piece createPiece() {
    val piece = new Piece();
    val max = 100;
    val current = ThreadLocalRandom.current().nextInt(0, 11);
    final int colorIdx = ThreadLocalRandom.current().nextInt(0, 4) / 2;
    val color = Color.values()[colorIdx];
    Integer rank = 0;

    if (color.isBlue() && !ranksBlue.isEmpty()) {
        rank = ranksBlue.get(0);
        ranksBlue.remove(0);
    } else if(!ranksRed.isEmpty()){
        rank = ranksRed.get(0);
        ranksRed.remove(0);
    }

    piece.setRank(rank);
    piece.setMax_count(max);
    piece.setCurrent_count(current);
    piece.setColor(color.getRgb());

    return piece;
  }
}