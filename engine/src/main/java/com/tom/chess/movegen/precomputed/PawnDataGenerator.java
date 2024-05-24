package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.FILES;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANKS;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_1;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_3;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_4;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_5;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_6;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_8;

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
//        new BitBoard(WHITE_PAWN_ATTACK_MASKS[i]).print();
      }

      if (rank == 1) { // actually rank 2, indexing
        // if rank 2, add 2 moves forward for white pawns and 1 move backward for black pawns
        WHITE_PAWN_MASKS[i] = (RANK_3 | RANK_4) & FILES[file];
        BLACK_PAWN_MASKS[i] = (RANK_1) & FILES[file];
      } else if (rank == 6) { // actually rank 7, indexing
        // if rank 6, add 2 moves forwards for black pawns and 1 move backward for white pawns
        WHITE_PAWN_MASKS[i] = (RANK_8) & FILES[file];
        BLACK_PAWN_MASKS[i] = (RANK_6 | RANK_5) & FILES[file];
      } else if (rank != 0 && rank != 7) {
        // otherwise, just add 1 move forward for white pawns and one more forward for black pawns
        WHITE_PAWN_MASKS[i] = (RANKS[rank + 1] & FILES[file]);
        BLACK_PAWN_MASKS[i] = (RANKS[rank - 1] & FILES[file]);
      }
    }
  }

  public static void initialise() {
  }
}