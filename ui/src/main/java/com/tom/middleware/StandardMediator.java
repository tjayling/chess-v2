package com.tom.middleware;

import com.tom.chess.engine.Engine;
import com.tom.chess.engine.EngineV2;
import com.tom.chess.model.Fen;
import com.tom.chess.model.Move;
import com.tom.chess.model.Moves;

public class StandardMediator implements Mediator {
  private MediatorState state;
  private final Engine engine;

  public StandardMediator(Fen fen) {
    engine = new EngineV2(fen);
    var moves = engine.getMoves();
    this.state = new MediatorState(fen, moves);
  }

  @Override
  public Fen getFen() {
    return state.fen();
  }

  @Override
  public int[] getSquares() {
    return state.fen().getSquares();
  }

  @Override
  public void makeMove(Move move) {
    var gameState = engine.makeMove(move);
    var fen = gameState.getFen();
    var moves = engine.getMoves();
    state = new MediatorState(fen, moves);
  }

  @Override
  public Moves getMoves() {
    return state.moves();
  }

  @Override
  public void undoMove() {
    engine.undo();
    var fen = engine.getFen();
    var moves = engine.getMoves();
    state = new MediatorState(fen, moves);
  }

  @Override
  public void redoMove() {
    engine.redo();
    var fen = engine.getFen();
    var moves = engine.getMoves();
    state = new MediatorState(fen, moves);
  }
}
