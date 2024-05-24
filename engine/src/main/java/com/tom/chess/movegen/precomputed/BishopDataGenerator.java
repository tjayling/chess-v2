package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.BISHOP_DIRECTIONS;

import java.util.HashMap;
import java.util.Map;

public class BishopDataGenerator {
  public static final long[] BISHOP_MASKS;
  public static final Map<Identifier, Long> BISHOP_LOOKUP;

  static {
    BISHOP_MASKS = new long[64];
    BISHOP_LOOKUP = new HashMap<>();
    for (int i = 0; i < 64; i++) {
      long movementMask = createBishopMovementMask(i);
      long[] blockers = PrecomputedMoveData.calculateBlockers(movementMask);
      for (long blocker : blockers) {
        var identifier = new Identifier(i, blocker);
        var legalMoves = PrecomputedMoveData.calculateLegalMoves(i, blocker, BISHOP_DIRECTIONS);

        BISHOP_LOOKUP.put(identifier, legalMoves);
      }
      BISHOP_MASKS[i] = movementMask;
    }
  }

  protected static long createBishopMovementMask(int startSquare) {
    long bishopMask = 0L;
    int rank = Math.floorDivExact(startSquare, 8);
    int file = startSquare % 8;

    // make top left line
    var topLeftShortest = Math.min(7 - rank, 7 - file);
    for (int j = 0; j < topLeftShortest; j++) {
      var pos = (rank + j) * 8 + (file + j);
      bishopMask |= 1L << pos;
    }

    // make top right line
    var topRightShortest = Math.min(7 - rank, file);
    for (int j = 0; j < topRightShortest; j++) {
      var pos = (rank + j) * 8 + (file - j);
      bishopMask |= 1L << pos;
    }

    // make bottom left shortest
    var bottomLeftShortest = Math.min(rank, 7 - file);
    for (int j = 0; j < bottomLeftShortest; j++) {
      var pos = (rank - j) * 8 + (file + j);
      bishopMask |= 1L << pos;
    }

    // make bottom right shortest
    var bottomRightShortest = Math.min(rank, file);
    for (int j = 0; j < bottomRightShortest; j++) {
      var pos = (rank - j) * 8 + (file - j);
      bishopMask |= 1L << pos;
    }

    // remove current position
    bishopMask &= ~(1L << startSquare);
    //remove edges
//    bishopMask &= (~RANK_1 & ~RANK_8 & ~FILE_A & ~FILE_H);
    return bishopMask;
  }

  public static void initialise() {}
}