package com.tom.chess.movegen.precomputed;

import static org.junit.jupiter.api.Assertions.*;

import com.tom.chess.model.BitBoard;
import org.junit.jupiter.api.Test;

class PrecomputedMoveDataTest {
  @Test
  void testLegalMoves1() {
    var startSquare = 20;
    var blockerBitboard = 0b0000000000010000000000000001000000010000011011100001000000000000L;
    var expected =        0b0000000000000000000100000001000000010000111011110001000000010000L;
    var actual = PrecomputedMoveData.calculateLegalMoves(startSquare, blockerBitboard, PrecomputedMoveData.ROOK_DIRECTIONS);

    System.out.println("Blockers:");
    new BitBoard(blockerBitboard).print();
    System.out.println("Expected:");
    new BitBoard(expected).print();
    System.out.println("Actual:");
    new BitBoard(actual).print();

    assertEquals(expected, actual);
  }

  @Test
  void testLegalMoves2() {
    var startSquare = 20;
    var blockerBitboard = 0b1111111110010000000000000001000000000000011011000001000000000000L;
    var expected =        0b0000000000000000000000000000000000010000111011100001000000010000L;
    var actual = PrecomputedMoveData.calculateLegalMoves(startSquare, blockerBitboard, PrecomputedMoveData.ROOK_DIRECTIONS);

    System.out.println("Blockers:");
    new BitBoard(blockerBitboard).print();
    System.out.println("Expected:");
    new BitBoard(expected).print();
    System.out.println("Actual:");
    new BitBoard(actual).print();

    assertEquals(expected, actual);
  }

  @Test
  void testLegalMoves3() {
    var startSquare = 7;
    var blockerBitboard = 0b1000000010000000100000001000000010000000100000001000000001111111L;
    var expected =        0b1000000010000000100000001000000010000000100000001000000001111111L;
    var actual = PrecomputedMoveData.calculateLegalMoves(startSquare, blockerBitboard, PrecomputedMoveData.ROOK_DIRECTIONS);

    System.out.println("Blockers:");
    new BitBoard(blockerBitboard).print();
    System.out.println("Expected:");
    new BitBoard(expected).print();
    System.out.println("Actual:");
    new BitBoard(actual).print();

    assertEquals(expected, actual);
  }

  @Test
  void testLegalMoves4() {
    var startSquare = 7;
    var blockerBitboard = 0b1000000010000000100000001000000000000000100000001000000001111111L;
    var expected =        0b0000000000000000000000000000000010000000100000001000000001111111L;
    var actual = PrecomputedMoveData.calculateLegalMoves(startSquare, blockerBitboard, PrecomputedMoveData.ROOK_DIRECTIONS);

    System.out.println("Blockers:");
    new BitBoard(blockerBitboard).print();
    System.out.println("Expected:");
    new BitBoard(expected).print();
    System.out.println("Actual:");
    new BitBoard(actual).print();

    assertEquals(expected, actual);
  }
}