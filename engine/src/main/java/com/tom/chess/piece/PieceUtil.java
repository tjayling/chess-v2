package com.tom.chess.piece;

import static com.tom.chess.piece.PieceConstants.*;

import com.tom.chess.util.BinUtil;

public class PieceUtil {
  private PieceUtil() {
  }

  public static int getType(int piece) {
    return BinUtil.getLsb(piece);
  }
  public static int getOppositeColour(int colour) {
    if (isWhite(colour)) {
      return BLACK;
    }
    if (isBlack(colour)) {
      return WHITE;
    }
    throw new RuntimeException("Cannot get opposite colour of %s".formatted(colour));
  }

  public static boolean isBlack(int piece) {
    return isColour(piece, PieceConstants.BLACK);
  }

  public static boolean isWhite(int piece) {
    return isColour(piece, PieceConstants.WHITE);
  }

  public static boolean isColour(int piece, int colour) {
    return BinUtil.getMsb(piece) == colour;
  }

  public static boolean isEmpty(int piece) {
    return getType(piece) == EMPTY;
  }

  public static boolean isPawn(int piece) {
    return getType(piece) == PAWN;
  }

  public static boolean isKnight(int piece) {
    return getType(piece) == KNIGHT;
  }

  public static boolean isBishop(int piece) {
    return getType(piece) == BISHOP;
  }

  public static boolean isRook(int piece) {
    return getType(piece) == ROOK;
  }

  public static boolean isQueen(int piece) {
    return getType(piece) == QUEEN;
  }

  public static boolean isKing(int piece) {
    return getType(piece) == KING;
  }
}
