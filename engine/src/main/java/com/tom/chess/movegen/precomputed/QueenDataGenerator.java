package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.BishopDataGenerator.createBishopMovementMask;
import static com.tom.chess.movegen.precomputed.RookDataGenerator.createRookMasks;

import com.tom.chess.model.BitBoard;
import java.util.HashMap;
import java.util.Map;

public class QueenDataGenerator {
  public static void createQueensLookupTable() {
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
}