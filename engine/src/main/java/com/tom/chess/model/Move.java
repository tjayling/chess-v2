package com.tom.chess.model;

import static com.tom.chess.model.StaticData.SQUARE_MAP;

import java.util.Arrays;
import lombok.Value;

@Value
public class Move {
  String move;

  public Move(String move) {
    this.move = move;
  }

  public Move(int startSquare, int targetSquare) {
    var startSquareString = SQUARE_MAP[startSquare];
    var targetSquareString = SQUARE_MAP[targetSquare];
    this.move = startSquareString + targetSquareString;
  }

  private static final Move EMPTY = new Move("");

  public int getStartSquare() {
    return Arrays.asList(SQUARE_MAP).indexOf(move.substring(0, 2));
  }

  public int getTargetSquare() {
    return Arrays.asList(SQUARE_MAP).indexOf(move.substring(2, 4));
  }

  public static Move empty() {
    return EMPTY;
  }

  @Override
  public String toString() {
    return "move";
  }
}
