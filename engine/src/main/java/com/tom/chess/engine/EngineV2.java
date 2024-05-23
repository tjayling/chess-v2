package com.tom.chess.engine;

import com.tom.chess.model.Fen;
import com.tom.chess.model.GameState;
import com.tom.chess.model.Move;
import com.tom.chess.model.Moves;
import com.tom.chess.movegen.MoveGenerator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class EngineV2 implements Engine {
  private final MoveGenerator moveGenerator;
  private final Deque<GameState> state;
  private final Deque<GameState> redoState;

  public EngineV2(Fen fen) {
    this.moveGenerator = new MoveGenerator();
    state = new ArrayDeque<>();
    redoState = new ArrayDeque<>();
    state.push(new GameState(fen));
  }

  @Override
  public void go(List<String> commands, Fen fen) {
    state.push(new GameState(fen));
  }

  @Override
  public GameState makeMove(Move move) {
    assert state.peek() != null;
    var currentState = state.peek();
    var newGameState = currentState.move(move);
    state.push(newGameState);
    return state.peek();
  }

  @Override
  public Moves getMoves() {
    return moveGenerator.generateMoves(state.peek());
  }

  @Override
  public Fen getFen() {
    assert state.peek() != null;
    return state.peek().getFen();
  }

  @Override
  public void undo() {
    if (!state.isEmpty()) {
      redoState.push(state.pop());
    }
  }

  @Override
  public void redo() {
    if (!redoState.isEmpty()) {
      state.push(redoState.pop());
    }
  }
}
