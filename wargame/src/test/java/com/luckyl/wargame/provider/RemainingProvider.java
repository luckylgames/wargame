package com.luckyl.wargame.provider;

import com.luckyl.wargame.Color;
import com.luckyl.wargame.service.model.Piece;
import lombok.val;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class RemainingProvider implements ArgumentsProvider {

  final List<List<Piece>> pieces = createPieces();

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
    return pieces.stream().map(Arguments::of);
  }

  List<List<Piece>> createPieces() {
    final List<List<Piece>> list = new ArrayList<>();

    for (int i=1; i<=10; i++)
      list.add(createList(i, getRanks(i)));

    return list;
  }

  List<Piece> createList(int seed, Map<Integer, Integer> ranks) {
    val colorIdx = ThreadLocalRandom.current().nextInt(0, Color.values().length-1);
    val color = Color.values()[colorIdx];
    final List<Piece> list = new ArrayList<>();

    for(int i=1; i<=5; i++) {
      list.add(createPiece(color, seed, ranks));
    }

    return list;
  }

  Piece createPiece(Color color, int seed, Map<Integer, Integer> ranks) {
    val piece = new Piece();
    val max = ThreadLocalRandom.current().nextInt(1, 11);
    val current = max;

    piece.setMax_count(max);
    piece.setCurrent_count(current);
    piece.setColor(color.getRgb());
    setRank(piece, ranks);

    return piece;
  }

  HashMap<Integer, Integer> getRanks(int seed) {
    val map = new HashMap<Integer, Integer>();
    val upperBound = seed * 11;
    val lowerBound = (seed - 1) * 11;

    while (map.size() < 5) {
      val random = ThreadLocalRandom.current().nextInt(lowerBound, upperBound);
      map.put(random, random);
    }

    return map;
  }

  void setRank(Piece piece, Map<Integer, Integer> ranks) {
    val rank = ranks.values().toArray()[0];
    ranks.remove(rank);

    piece.setRank((Integer) rank);
  }
}