package com.tom.middleware;

import com.tom.chess.model.Fen;
import com.tom.chess.model.Move;
import com.tom.chess.model.Moves;

public interface Mediator {
  Fen getFen();
  int[] getSquares();
  void makeMove(Move move);
  Moves getMoves();
  void undoMove();
  void redoMove();
}
