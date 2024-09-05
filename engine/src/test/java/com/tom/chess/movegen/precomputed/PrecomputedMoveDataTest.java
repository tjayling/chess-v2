package com.tom.chess.movegen.precomputed;

import static org.junit.jupiter.api.Assertions.*;

import com.tom.chess.model.BitBoard;
import org.junit.jupiter.api.Test;

class PrecomputedMoveDataTest {
  @Test
  void testLegalMoves1() {
    var startSquare = 20;
    var blockerBitboard = 0b0000000000000000000100000000000000000000100000010000000000010000L;
    var expected =        0b0000000000000000000100000001000000010000111011110001000000010000L;
    var actual = PrecomputedMoveData.calculateLegalMoves(startSquare, blockerBitboard, PrecomputedMoveData.ROOK_DIRECTIONS);

    System.out.println("Blockers:");
    BitBoard.from(blockerBitboard).print();
    System.out.println("Expected:");
    BitBoard.from(expected).print();
    System.out.println("Actual:");
    BitBoard.from(actual).print();

    assertEquals(expected, actual);
  }

  @Test
  void testLegalMoves2() {
    var startSquare = 20;
    var blockerBitboard = 0b0000000000000000000000000000000000010000100000100000000000010000L;
    var expected =        0b0000000000000000000000000000000000010000111011100001000000010000L;
    var actual = PrecomputedMoveData.calculateLegalMoves(startSquare, blockerBitboard, PrecomputedMoveData.ROOK_DIRECTIONS);

    System.out.println("Blockers:");
    BitBoard.from(blockerBitboard).print();
    System.out.println("Expected:");
    BitBoard.from(expected).print();
    System.out.println("Actual:");
    BitBoard.from(actual).print();

    assertEquals(expected, actual);
  }

  @Test
  void testLegalMoves3() {
    var startSquare = 7;
    var blockerBitboard = 0b1000000000000000000000000000000000000000000000000000000001000001L;
    var expected =        0b1000000010000000100000001000000010000000100000001000000001000000L;
    var actual = PrecomputedMoveData.calculateLegalMoves(startSquare, blockerBitboard, PrecomputedMoveData.ROOK_DIRECTIONS);

    System.out.println("Blockers:");
    BitBoard.from(blockerBitboard).print();
    System.out.println("Expected:");
    BitBoard.from(expected).print();
    System.out.println("Actual:");
    BitBoard.from(actual).print();

    assertEquals(expected, actual);
  }

  @Test
  void testLegalMoves4() {
    var startSquare = 7;
    var blockerBitboard = 0b0000000000000000000000000000000010000000000000000000000000000001L;
    var expected =        0b0000000000000000000000000000000010000000100000001000000001111111L;
    var actual = PrecomputedMoveData.calculateLegalMoves(startSquare, blockerBitboard, PrecomputedMoveData.ROOK_DIRECTIONS);

    System.out.println("Blockers:");
    BitBoard.from(blockerBitboard).print();
    System.out.println("Expected:");
    BitBoard.from(expected).print();
    System.out.println("Actual:");
    BitBoard.from(actual).print();

    assertEquals(expected, actual);
  }
}