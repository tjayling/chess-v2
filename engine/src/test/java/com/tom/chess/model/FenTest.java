package com.tom.chess.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FenTest {

  @Test
  void squaresFromDefaultFenString() {
    var expected =
        new int[] {
            21, 19, 20, 22, 17, 20, 19, 21,
            18, 18, 18, 18, 18, 18, 18, 18,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            34, 34, 34, 34, 34, 34, 34, 34,
            37, 35, 36, 38, 33, 36, 35, 37};
    var actual = Fen.defaultFen().getSquares();
    assertArrayEquals(expected, actual);
  }

  @Test
  void squaresFromDefaultRookString() {
    var expected =
        new int[] {
            21, 0, 0, 0, 0, 0, 21, 21,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            37, 37, 37, 21, 37, 37, 37, 37};
    var actual = Fen.defaultFen().getSquares();
    assertArrayEquals(expected, actual);
  }

}