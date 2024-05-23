package com.tom.chess.model;

public class StaticData {
  public static final String[] SQUARE_MAP = new String[64];

  static {
    // Compute squareMap values
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        SQUARE_MAP[i*8+j] = String.valueOf((char) (97 + j)) + (i + 1);
      }
    }
  }
}
