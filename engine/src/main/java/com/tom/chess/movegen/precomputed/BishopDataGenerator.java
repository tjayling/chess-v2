package com.tom.chess.movegen.precomputed;

import com.tom.chess.model.BitBoard;
import java.util.HashMap;
import java.util.Map;

public class BishopDataGenerator {
  public static void createBishopsLookupTable() {
    Map<Identifier, Long> lookup = new HashMap<>();
    for (int i = 0; i < 64; i++) {
      long movementMask = createBishopMovementMask(i);
      new BitBoard(movementMask).print();
    }
  }

  protected static long createBishopMovementMask(int startSquare) {
    long bishopMask = 0L;
    int rank = Math.floorDivExact(startSquare, 8);
    int file = startSquare % 8;

    // make top left line
    var topLeftShortest = Math.min(8 - rank, 8 - file);
    for (int j = 0; j < topLeftShortest; j++) {
      var pos = (rank + j) * 8 + (file + j);
      bishopMask |= 1L << pos;
    }

    // make top right line
    var topRightShortest = Math.min(8 - rank, file + 1);
    for (int j = 0; j < topRightShortest; j++) {
      var pos = (rank + j) * 8 + (file - j);
      bishopMask |= 1L << pos;
    }

    // make bottom left shortest
    var bottomLeftShortest = Math.min(rank + 1, 8 - file);
    for (int j = 0; j < bottomLeftShortest; j++) {
      var pos = (rank - j) * 8 + (file + j);
      bishopMask |= 1L << pos;
    }

    // make bottom right shortest
    var bottomRightShortest = Math.min(rank + 1, file + 1);
    for (int j = 0; j < bottomRightShortest; j++) {
      var pos = (rank - j) * 8 + (file - j);
      bishopMask |= 1L << pos;
    }

    // remove current position
    bishopMask &= ~(1L << startSquare);
    return bishopMask;
  }
}