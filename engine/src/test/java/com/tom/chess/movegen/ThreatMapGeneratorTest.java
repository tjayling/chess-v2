package com.tom.chess.movegen;

import static org.junit.jupiter.api.Assertions.*;

import com.tom.chess.model.BitBoard;
import com.tom.chess.util.Coord;
import java.lang.reflect.Method;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThreatMapGeneratorTest {
  ThreatMapGenerator threatMapGenerator;
  Method drawLineMethod;

  @BeforeEach
  @SneakyThrows
  void setup() {
    threatMapGenerator = new ThreatMapGenerator();
    drawLineMethod = ThreatMapGenerator.class.getDeclaredMethod("drawLine", Coord.class, Coord.class);
    drawLineMethod.setAccessible(true);
  }

  @Test
  @SneakyThrows
  void testDrawLine1() {
    var start = new Coord(0, 7);
    var end = new Coord(7, 0);

    var expected = BitBoard.from(0b0000000100000010000001000000100000010000001000000100000010000000L);
    var actual = (BitBoard) drawLineMethod.invoke(threatMapGenerator, start, end);

    expected.print();
    actual.print();

    assertEquals(expected, actual);
  }

  @Test
  @SneakyThrows
  void testDrawLine2() {
    var start = new Coord(7, 0);
    var end = new Coord(0, 7);

    var expected = BitBoard.from(0b0000000100000010000001000000100000010000001000000100000010000000L);
    var actual = (BitBoard) drawLineMethod.invoke(threatMapGenerator, start, end);

    expected.print();
    actual.print();

    assertEquals(expected, actual);
  }

  @Test
  @SneakyThrows
  void testDrawLine3() {
    var start = new Coord(0, 0);
    var end = new Coord(7, 7);

    var expected = BitBoard.from(0b10000000_01000000_00100000_00010000_00001000_00000100_00000010_00000001L);
    var actual = (BitBoard) drawLineMethod.invoke(threatMapGenerator, start, end);

    expected.print();
    actual.print();

    assertEquals(expected, actual);
  }

  @Test
  @SneakyThrows
  void testDrawLine4() {
    var start = new Coord(2, 2);
    var end = new Coord(7, 7);

    var expected = BitBoard.from(0b10000000_01000000_00100000_00010000_00001000_00000100_00000000_00000000L);
    var actual = (BitBoard) drawLineMethod.invoke(threatMapGenerator, start, end);

    expected.print();
    actual.print();

    assertEquals(expected, actual);
  }

}