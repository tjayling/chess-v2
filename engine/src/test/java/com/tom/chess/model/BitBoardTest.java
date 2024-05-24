package com.tom.chess.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class BitBoardTest {
  @Test
  void testGetPosition1() {
    var board = 0b1;
    var bb = new BitBoard(board);
    var expected = List.of(0);
    var actual = bb.getPositions();
    assertEquals(expected, actual);
  }

  @Test
  void testGetPosition2() {
    var board = 0b1000000000000000000000000000000000000000000000000000000000000000L;
    var bb = new BitBoard(board);
    var expected = List.of(63);
    var actual = bb.getPositions();
    assertEquals(expected, actual);
  }

}