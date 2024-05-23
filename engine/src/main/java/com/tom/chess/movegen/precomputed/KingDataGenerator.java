package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.FILE_A;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.FILE_H;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_1;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_8;

import com.tom.chess.model.BitBoard;
import java.util.HashMap;
import java.util.Map;

public class KingDataGenerator {
  public static void createKingsLookupTable() {
    Map<Identifier, Long> lookup = new HashMap<>();
    for (int i = 0; i < 64; i++) {
      long movementMask = createKingMovementMask(i);
      new BitBoard(movementMask).print();
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
      kingMask &= ~FILE_A;
    }
    if (file == 7) {
      kingMask &= ~FILE_H;
    }

    return kingMask;
  }
}
