package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.BishopDataGenerator.BISHOP_MASKS;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.QUEEN_DIRECTIONS;
import static com.tom.chess.movegen.precomputed.RookDataGenerator.ROOK_MASKS;

import java.util.HashMap;
import java.util.Map;

public class QueenDataGenerator {
  public static final long[] QUEEN_MASKS;
  public static final Map<Identifier, Long> QUEEN_LOOKUP;

  static {
    QUEEN_MASKS = new long[64];
    QUEEN_LOOKUP = new HashMap<>();
    for (int i = 0; i < 64; i++) {
      long movementMask = createQueensMovementMask(i);
      long[] blockers = PrecomputedMoveData.calculateBlockers(movementMask);
      for (long blocker : blockers) {
        var identifier = new Identifier(i, blocker);
        var legalMoves = PrecomputedMoveData.calculateLegalMoves(i, blocker, QUEEN_DIRECTIONS);

        QUEEN_LOOKUP.put(identifier, legalMoves);
      }
      QUEEN_MASKS[i] = movementMask;
    }
  }

  private static long createQueensMovementMask(int startSquare) {
    long queenMask = 0L;
    queenMask |= BISHOP_MASKS[startSquare] | ROOK_MASKS[startSquare];
    return queenMask;
  }

  public static void initialise() {}
}