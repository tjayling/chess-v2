package com.tom.chess.engine;

import com.tom.chess.model.Fen;
import com.tom.chess.model.GameState;
import com.tom.chess.model.Move;
import com.tom.chess.model.Moves;
import java.util.List;

public interface Engine {
  void go(List<String> commands, Fen fen);
  GameState makeMove(Move move);
  Moves getMoves();
  Fen getFen();
  void undo();
  void redo();
}
