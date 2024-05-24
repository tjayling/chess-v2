package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.FILE_A;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.FILE_H;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_1;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_8;

public class KingDataGenerator {
  public static final long[] KING_MASKS;

  static {
    KING_MASKS = new long[64];
    for (int i = 0; i < 64; i++) {
      KING_MASKS[i] = createKingMovementMask(i);
    }
  }

  private static long createKingMovementMask(int startSquare) {
    long kingMask = 0L;
    int[] neighbors = new int[8];
    int rank = Math.floorDivExact(startSquare, 8);
    int file = startSquare % 8;

    neighbors[0] = ((rank - 1) * 8) + (file - 1);  // TL
    neighbors[1] = ((rank - 1) * 8) + (file);      // T
    neighbors[2] = ((rank - 1) * 8) + (file + 1);  // TR

    neighbors[3] = (rank * 8) + (file - 1);  // L
    neighbors[4] = (rank * 8) + (file + 1);  // R

    neighbors[5] = ((rank + 1) * 8) + (file - 1);  // BL
    neighbors[6] = ((rank + 1) * 8) + (file);      // B
    neighbors[7] = ((rank + 1) * 8) + (file + 1);  // BR

    for (int j : neighbors) {
      kingMask |= 1L << j;
    }

    if (rank == 0) {
      kingMask &= ~RANK_8;
    }
    if (rank == 7) {
      kingMask &= ~RANK_1;
    }
    if (file == 0) {
      kingMask &= ~FILE_H;
    }
    if (file == 7) {
      kingMask &= ~FILE_A;
    }

    return kingMask;
  }

  public static void initialise() {}
}
