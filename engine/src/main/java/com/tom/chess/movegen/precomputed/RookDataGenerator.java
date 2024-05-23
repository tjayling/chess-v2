package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.ROOK_DIRECTIONS;

import java.util.HashMap;
import java.util.Map;

public class RookDataGenerator {
  public static final long[] ROOK_MASKS;
  public static final Map<Identifier, Long> ROOK_LOOKUP;

  static {
    ROOK_MASKS = new long[64];
    ROOK_LOOKUP = new HashMap<>();
    for (int i = 0; i < 64; i++) {
      long movementMask = createRookMasks(i);
      long[] blockers = PrecomputedMoveData.calculateBlockers(movementMask);
      for (long blocker : blockers) {
        var identifier = new Identifier(i, blocker);
        var legalMoves = PrecomputedMoveData.calculateLegalMoves(i, blocker, ROOK_DIRECTIONS);

        ROOK_LOOKUP.put(identifier, legalMoves);
      }
      ROOK_MASKS[i] = movementMask;
    }
  }

  public static long createRookMasks(int startSquare) {
    long rookMask = 0L;
    int rank = Math.floorDivExact(startSquare, 8);
    int file = startSquare % 8;

    // make vertical line
    for (int y = 1; y < 7; y++) {
      var pos = y * 8 + file;
      rookMask |= 1L << pos;
    }

    // make horizontal line
    for (int x = 1; x < 7; x++) {
      var pos = rank * 8 + x;
      rookMask |= 1L << pos;
    }

    // remove current position
    rookMask &= ~(1L << startSquare);

    return rookMask;
  }

  public static void initialise() {}
}
