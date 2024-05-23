package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.FILE_A_B;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.FILE_G_H;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_1_2;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_7_8;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.calculateBlockers;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.calculateLegalMoves;

import com.tom.chess.model.BitBoard;
import java.util.HashMap;
import java.util.Map;

public class KnightDataGenerator {

  public static Map<Identifier, Long> createKnightsLookupTable() {
    Map<Identifier, Long> lookup = new HashMap<>();
    for (int startSquare = 0; startSquare < 64; startSquare++) {
      long movementMask = createKnightMovementMask(startSquare);
      long[] blockerMasks = calculateBlockers(movementMask);
      new BitBoard(movementMask).print();
      for (var blockerMask : blockerMasks) {
        var identifier = new Identifier(startSquare, blockerMask);
//        var legalMoves =  calculateLegalMoves(startSquare, blockerMask);
//        lookup.put(identifier, legalMoves);
      }
    }
    return lookup;
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
      knightMask &= ~FILE_A_B;
    }

    if (file == 6 || file == 7) {
      knightMask &= ~FILE_G_H;
    }

    return knightMask;
  }
}