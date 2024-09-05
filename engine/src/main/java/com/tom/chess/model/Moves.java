package com.tom.chess.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import lombok.Getter;

@Getter
public class Moves implements Collection<Move> {
  private final List<Move> moves;

  private Moves(List<Move> moves) {
    this.moves = new ArrayList<>(moves);
  }

  private Moves() {
    this.moves = new ArrayList<>();
  }

  public static Moves empty() {
    return new Moves();
  }

  public static Moves from(int startPosition, BitBoard targetBitBoard) {
    List<Move> moves = new ArrayList<>();
    var targetPositions = targetBitBoard.getPositions();
    for (var targetPosition : targetPositions) {
      moves.add(new Move(startPosition, targetPosition));
    }
    return new Moves(moves);
  }

  public static Moves from(BitBoard startBitBoard, int targetPosition) {
    List<Move> moves = new ArrayList<>();
    var startPositions = startBitBoard.getPositions();
    for (var startPosition : startPositions) {
      moves.add(new Move(startPosition, targetPosition));
    }
    return new Moves(moves);
  }

  public static Moves from(BitBoard startBitBoard, BitBoard targetBitBoard) {
    List<Move> moves = new ArrayList<>();
    var startPositions = startBitBoard.getPositions();
    for (var startPosition : startPositions) {
      moves.addAll(Moves.from(startPosition, targetBitBoard));
    }
    return new Moves(moves);
  }

  @Override
  public Iterator<Move> iterator() {
    return this.moves.iterator();
  }

  @Override
  public Object[] toArray() {
    return this.moves.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return this.moves.toArray(a);
  }

  @Override
  public boolean add(Move move) {
    return this.moves.add(move);
  }

  @Override
  public boolean remove(Object move) {
    return this.moves.remove(move);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return new HashSet<>(this.moves).containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends Move> moves) {
    return this.moves.addAll(moves);
  }

  public boolean addAll(Moves moves) {
    return this.moves.addAll(moves.getMoves());
  }

  @Override
  public boolean removeAll(Collection<?> moves) {
    return this.moves.removeAll(moves);
  }

  @Override
  public boolean retainAll(Collection<?> moves) {
    return this.moves.retainAll(moves);
  }

  @Override
  public void clear() {
    moves.clear();
  }

  @Override
  public boolean isEmpty() {
    return this.moves.isEmpty();
  }

  @Override
  public boolean contains(Object move) {
    return this.moves.contains(move);
  }

  @Override
  public int size() {
    return this.moves.size();
  }
}
