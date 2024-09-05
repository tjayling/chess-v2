package com.tom.chess.model;

import static com.tom.chess.model.StaticData.SQUARE_MAP;
import static com.tom.commandline.AnsiConstants.ANSI_GREEN_BACKGROUND;
import static com.tom.commandline.AnsiConstants.ANSI_RED_BACKGROUND;
import static com.tom.commandline.AnsiConstants.ANSI_RESET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
public class BitBoard {
  private static final int LENGTH = 64;
  private final Long board;

  private BitBoard(long board) {
    this.board = board;
  }

  public static BitBoard empty() {
    return new BitBoard(0L);
  }

  public BitBoard addBitAtIndex(int index) {
    return new BitBoard(board | (1L << index));
  }

  public BitBoard and(BitBoard that) {
    return new BitBoard(this.board & that.getBoard());
  }

  public BitBoard and(long that) {
    return new BitBoard(this.board & that);
  }

  public BitBoard or(BitBoard that) {
    return new BitBoard(this.board | that.getBoard());
  }

  public BitBoard or(long that) {
    return new BitBoard(this.board | that);
  }

  public boolean isEmpty() {
    return this.equals(empty());
  }

  public boolean isNotEmpty() {
    return !isEmpty();
  }

  /**
   * create a bitboard from an array of `long` values
   *
   * @param boards an array of bitboards in `long` form
   * @return a BitBoard with all values from the input array
   */
  public static BitBoard from(long... boards) {
    // using boring old for loop for real speed
    long result = 0L;
    for (long l : boards) {
      result |= l;
    }
    return new BitBoard(result);
  }

  /**
   * create a bitboard from an array of other BitBoards values
   *
   * @param boards an array of bitboards
   * @return a BitBoard with all values from the input array
   */
  public static BitBoard from(BitBoard... boards) {
    // using boring old for loop for real speed
    BitBoard result = BitBoard.empty();
    for (BitBoard l : boards) {
      result = result.or(l);
    }
    return result;
  }

  public BitBoard xor(BitBoard that) {
    return new BitBoard(this.board ^ that.getBoard());
  }

  public BitBoard xor(long that) {
    return new BitBoard(this.board ^ that);
  }

  public BitBoard not() {
    return new BitBoard(~this.board);
  }

  public BitBoard leftShift(int distance) {
    return distance >= 0 ? new BitBoard(this.board << distance) : rightShift(-distance);
  }

  public BitBoard rightShift(int distance) {
    return distance >= 0 ? new BitBoard(this.board >>> distance) : leftShift(-distance);
  }

  public int getLastPosition() {
    return Long.numberOfTrailingZeros(board);
  }

  public List<Integer> getPositions() {
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < 64; i++) {
      if (((board >>> i) & 1L) == 1) {
        result.add(i);
      }
    }
    return result;
  }

  // static methods
  public static BitBoard fromPosition(int i) {
    return BitBoard.empty().addBitAtIndex(i);
  }

  public static BitBoard fromPosition(String s) {
    var index = Arrays.asList(SQUARE_MAP).indexOf(s);
    return index == -1 ? BitBoard.empty() : BitBoard.fromPosition(index);
  }

  public static BitBoard parse(String bitBoard) {
    return new BitBoard(Long.parseUnsignedLong(bitBoard, 2));
  }

  public static BitBoard andAll(BitBoard... bitBoards) {
    var prevBitBoard = new BitBoard(0);
    for (var bitBoard : bitBoards) {
      prevBitBoard = prevBitBoard.and(bitBoard);
    }
    return prevBitBoard;
  }

  public static BitBoard orAll(BitBoard... bitBoards) {
    var prevBitBoard = new BitBoard(0);
    for (var bitBoard : bitBoards) {
      prevBitBoard = prevBitBoard.or(bitBoard);
    }
    return prevBitBoard;
  }

  public void print() {
    var result = new StringBuilder();
    var input = new StringBuilder(Long.toBinaryString(board));
    while (input.length() < LENGTH) {
      input.insert(0, '0');
    }

    for (int i = 0; i < 8; i++) {
      List<String> line = new ArrayList<>();
      for (char c : input.substring(i * 8, i * 8 + 8).toCharArray()) {
        if (c == '1') {
          line.add("%s %s %s".formatted(ANSI_GREEN_BACKGROUND, c, ANSI_RESET));
        } else {
          line.add("%s %s %s".formatted(ANSI_RED_BACKGROUND, c, ANSI_RESET));
        }
      }
      Collections.reverse(line);
      for (String s : line) {
        result.append(s);
      }
      result.append('\n');
    }
    System.out.println(result);
    System.out.println(input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    BitBoard bitBoard = (BitBoard) o;
    return Objects.equals(board, bitBoard.board);
  }

  public boolean equals(Long o) {
    return this.board.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(board);
  }

  public boolean hasPositions() {
    return this.board != 0;
  }
}
