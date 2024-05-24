package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.BishopDataGenerator.createBishopMovementMask;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.*;
import static com.tom.chess.movegen.precomputed.RookDataGenerator.createRookMasks;

import com.tom.chess.model.BitBoard;
import java.util.HashMap;
import java.util.Map;

public class PawnDataGenerator {
  public static final long[] WHITE_PAWN_MASKS;
  public static final long[] WHITE_PAWN_ATTACK_MASKS;
  public static final long[] BLACK_PAWN_MASKS;
  public static final long[] BLACK_PAWN_ATTACK_MASKS;

  static {
    WHITE_PAWN_MASKS = new long[64];
    WHITE_PAWN_ATTACK_MASKS = new long[64];
    BLACK_PAWN_MASKS = new long[64];
    BLACK_PAWN_ATTACK_MASKS = new long[64];

    for (int i = 8; i < 64; i++) {
      int rank = Math.floorDivExact(i, 8);
      int file = i % 8;


      if (rank != 0 && rank != 7) {
        if (file > 0) {
          WHITE_PAWN_ATTACK_MASKS[i] |= (RANKS[rank + 1] & FILES[file - 1]);
          BLACK_PAWN_ATTACK_MASKS[i] |= (RANKS[rank - 1] & FILES[file - 1]);
        }
        if (file < 7) {
          WHITE_PAWN_ATTACK_MASKS[i] |= (RANKS[rank + 1] & FILES[file + 1]);
          BLACK_PAWN_ATTACK_MASKS[i] |= (RANKS[rank - 1] & FILES[file + 1]);
        }
      }

      if (rank == 1) { // actually rank 2, indexing
        WHITE_PAWN_MASKS[i] = (RANK_3 | RANK_4) & FILES[file];
        BLACK_PAWN_MASKS[i] = (RANK_1) & FILES[file];
      } else if (rank == 6) { // actually rank 7, indexing
        WHITE_PAWN_MASKS[i] = (RANK_8) & FILES[file];
        BLACK_PAWN_MASKS[i] = (RANK_6 | RANK_5) & FILES[file];
      } else if (rank != 0 && rank != 7) {
        WHITE_PAWN_MASKS[i] = (RANKS[rank + 1] & FILES[file]);
        BLACK_PAWN_MASKS[i] = (RANKS[rank - 1] & FILES[file]);
      }
      System.out.println("PAWN");
      new BitBoard(WHITE_PAWN_MASKS[i]).print();
      new BitBoard(BLACK_PAWN_MASKS[i]).print();
    }
  }

  public static void createPawnsLookupTable() {
    Map<Identifier, Long> lookup = new HashMap<>();
    for (int i = 0; i < 64; i++) {
      long movementMask = createQueensMovementMask(i);
      new BitBoard(movementMask).print();
    }
  }

  public static long createQueensMovementMask(int startSquare) {
    long queenMask = 0L;
    queenMask |= createBishopMovementMask(startSquare) | createRookMasks(startSquare);
    return queenMask;
  }

  public static void initialise() {
  }
}