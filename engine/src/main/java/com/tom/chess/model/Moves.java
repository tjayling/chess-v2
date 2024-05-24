package com.tom.chess.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Getter;

@Getter
public class Moves implements Iterable<Move> {
  private final List<Move> moves;

  public Moves(List<Move> moves) {
    this.moves = new ArrayList<>(moves);
  }

  public Moves() {
    this.moves = new ArrayList<>();
  }

  public int size() {
    return moves.size();
  }

  @Override
  public Iterator<Move> iterator() {
    return moves.iterator();
  }

  public void add(Move move) {
    moves.add(move);
  }

  public static Moves empty() {
    return new Moves(new ArrayList<>());
  }

  public void addAll(List<Move> moves) {
    this.moves.addAll(moves);
  }

  public void addAll(Moves moves) {
    this.moves.addAll(moves.getMoves());
  }

  public static Moves from(int startPosition, BitBoard bitBoard) {
    List<Move> moves = new ArrayList<>();
    var targetPositions = bitBoard.getPositions();
    for (var targetPosition : targetPositions) {
      moves.add(new Move(startPosition, targetPosition));
    }
    return new Moves(moves);
  }
}
