package com.tom.chess.movegen.precomputed;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tom.chess.model.BitBoard;
import org.junit.jupiter.api.Test;

class RookDataGeneratorTest {

  @Test
  void testGen1() {
    var expected = 0b0000000000000010000000100111110000000010000000100000001000000000L;
    var actual = RookDataGenerator.createRookMasks(33);
    new BitBoard(expected).print();
    new BitBoard(actual).print();
    assertEquals(expected, actual);
  }

  @Test
  void testGen2() {
    var expected = 0b0000000010000000100000001000000010000000100000001000000001111110L;
    var actual = RookDataGenerator.createRookMasks(7);
    new BitBoard(expected).print();
    new BitBoard(actual).print();
    assertEquals(expected, actual);
  }

  @Test
  void testGen3() {
    var expected =    0b0000000010000000100000001000000010000000100000001000000001110000L;
    var blockerMask = 0b0000000010000000000000000000000000000000000000000000000000010000L;
    var startSquare = 7;
    var identifier = new Identifier(startSquare, blockerMask);

    var lookupTable = RookDataGenerator.ROOK_LOOKUP;

    var actual = lookupTable.get(identifier);

//    lookupTable.values().stream().map(BitBoard::new).forEach(BitBoard::print);

    System.out.println("Blocker mask:");
    new BitBoard(blockerMask).print();
    System.out.println("Expected:");
    new BitBoard(expected).print();
    System.out.println("Actual:");
    new BitBoard(actual).print();

    assertEquals(expected, actual);
  }
}