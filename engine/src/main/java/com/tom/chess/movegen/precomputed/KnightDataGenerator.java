package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.FILE_A_B;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.FILE_G_H;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_1_2;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_7_8;

public class KnightDataGenerator {
  public static final long[] KNIGHT_MASKS;

  static {
    KNIGHT_MASKS = new long[64];

    for (int startSquare = 0; startSquare < 64; startSquare++) {
      KNIGHT_MASKS[startSquare] = createKnightMovementMask(startSquare);
    }
  }

  private static long createKnightMovementMask(int startSquare) {
    long knightMask = 0L;
    int[] neighbors = new int[8];
    int rank = Math.floorDivExact(startSquare, 8);
    int file = startSquare % 8;

    neighbors[0] = ((rank + 2) * 8) + (file + 1);  // TL
    neighbors[1] = ((rank + 2) * 8) + (file - 1);  // TR

    neighbors[2] = ((rank + 1) * 8) + (file - 2);  // RT
    neighbors[3] = ((rank - 1) * 8) + (file - 2);  // RB

    neighbors[4] = ((rank - 2) * 8) + (file - 1);  // BR
    neighbors[5] = ((rank - 2) * 8) + (file + 1);  // BL

    neighbors[6] = ((rank - 1) * 8) + (file + 2);  // LB
    neighbors[7] = ((rank + 1) * 8) + (file + 2);  // LT

    for (int j : neighbors) {
      knightMask |= 1L << j;
    }

    if (rank == 0 || rank == 1) {
      knightMask &= ~RANK_7_8;
    }
    if (rank == 6 || rank == 7) {
      knightMask &= ~RANK_1_2;
    }

    if (file == 0 || file == 1) {
      knightMask &= ~FILE_G_H;
    }

    if (file == 6 || file == 7) {
      knightMask &= ~FILE_A_B;
    }

    return knightMask;
  }

  public static void initialise() {}
}