package com.tom.chess.movegen.precomputed;

import static com.tom.chess.movegen.precomputed.BishopDataGenerator.createBishopsLookupTable;
import static com.tom.chess.movegen.precomputed.KingDataGenerator.createKingsLookupTable;
import static com.tom.chess.movegen.precomputed.KnightDataGenerator.createKnightsLookupTable;

import com.tom.chess.util.Coord;
import java.util.ArrayList;
import java.util.List;

public class PrecomputedMoveData {
  public static final long RANK_1 = 0X00000000000000FFL;
  public static final long RANK_2 = 0X000000000000FF00L;
  public static final long RANK_7 = 0X00FF000000000000L;
  public static final long RANK_8 = 0XFF00000000000000L;
  public static final long FILE_A = 0X8080808080808080L;
  public static final long FILE_B = 0XC0C0C0C0C0C0C0C0L;
  public static final long FILE_G = 0X0303030303030303L;
  public static final long FILE_H = 0X0101010101010101L;

  public static final long RANK_1_2 = RANK_1 | RANK_2;
  public static final long RANK_7_8 = RANK_7 | RANK_8;
  public static final long FILE_A_B = FILE_A | FILE_B;
  public static final long FILE_G_H = FILE_G | FILE_H;

  public static final Coord[] ROOK_DIRECTIONS;
  public static final Coord[] BISHOP_DIRECTIONS;

  static {
    ROOK_DIRECTIONS = new Coord[] {new Coord(1, 0), new Coord(0, 1), new Coord(-1, 0), new Coord(0, -1)};
    BISHOP_DIRECTIONS = new Coord[] {new Coord(1, 1), new Coord(1, -1), new Coord(-1, -1), new Coord(-1, 0)};
  }

  public static long[] calculateBlockers(long movementMask) {
    List<Integer> movementIndices = new ArrayList<>();

    for (int i = 0; i < 64; i++) {
      if (((movementMask >>> i) & 1) == 1) {
        movementIndices.add(i);
      }
    }

    int numPatterns = 1 << movementIndices.size();
    long[] blockerBitboards = new long[numPatterns];

    for (int patternIndex = 0; patternIndex < numPatterns; patternIndex++) {
      for (int bitIndex = 0; bitIndex < movementIndices.size(); bitIndex++) {
        int bit = (patternIndex >> bitIndex) & 1;
        blockerBitboards[patternIndex] |= ((long) bit << movementIndices.get(bitIndex));
      }
    }

    return blockerBitboards;
  }

  public static long calculateLegalMoves(int startSquare, long blockerBitboard, Coord[] directions) {
    long bitboard = 0L;

    Coord startCoord = new Coord(startSquare);

    for (Coord dir : directions) {
      for (int distance = 1; distance < 8; distance++) {
        Coord coord = startCoord.add(dir.mult(distance));
        if (coord.isValidSquare()) {
          bitboard |= 1L << coord.getSquareIndex();
          if ((blockerBitboard >> coord.getSquareIndex() & 1) == 1) {
            break;
          }
        } else {
          break;
        }
      }
    }
    return bitboard;
  }

  public static void initialise() {
//    createBishopsLookupTable();
//    createKnightsLookupTable();
//    createRooksLookupTable();
//    createKingsLookupTable();
    RookDataGenerator.initialise();
  }
}
