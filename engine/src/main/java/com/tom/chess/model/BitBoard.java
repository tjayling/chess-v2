package com.tom.chess.model;

import static com.tom.commandline.AnsiConstants.ANSI_GREEN_BACKGROUND;
import static com.tom.commandline.AnsiConstants.ANSI_RED_BACKGROUND;
import static com.tom.commandline.AnsiConstants.ANSI_RESET;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
public class BitBoard {
  private static final int LENGTH = 64;
  private final Long board;

  public BitBoard(long board) {
    this.board = board;
  }

  public static BitBoard empty() {
    return new BitBoard(0L);
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
      for (String s : line) {
        result.append(s);
      }
      result.append('\n');
    }
    System.out.println(result);
    System.out.println(input);
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

  public BitBoard not() {
    return new BitBoard(~this.board);
  }

  public int getLastPosition() {
    return Long.numberOfTrailingZeros(board);
  }

  public List<Integer> getPositions() {
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < 64; i++) {
      if (((board >>> i) & 1) == 1) {
        result.add(i);
      }
    }
    return result;
  }

  // static methods
  public static BitBoard fromIndex(int i) {
    return BitBoard.empty().addBitAtIndex(i);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    BitBoard bitBoard = (BitBoard) o;
    return Objects.equals(board, bitBoard.board);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(board);
  }
}