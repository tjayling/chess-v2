package com.tom.gui.perft;

import com.tom.chess.model.Fen;
import com.tom.chess.model.Move;
import com.tom.chess.model.Moves;
import com.tom.engine.StockfishEngine;
import com.tom.gui.chess.ChessAreaGui;
import com.tom.middleware.Mediator;
import com.tom.middleware.StandardMediator;
import java.awt.EventQueue;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PerftGuiController {
  private static PerftGuiController INSTANCE;
  private PerftPanel perftPanel;
  private ChessAreaGui chessAreaGui;
  private Mediator mediator;

  private PerftGuiController(Mediator mediator) {
    this.mediator = mediator;
  }

  public static PerftGuiController instantiate(Mediator mediator) {
    if (INSTANCE != null) {
      throw new RuntimeException("PerftGuiController already initialized");
    }
    INSTANCE = new PerftGuiController(mediator);
    return INSTANCE;
  }

  public static PerftGuiController getInstance() {
    if (INSTANCE == null) {
      throw new RuntimeException("PerftGuiController not initialized");
    }
    return INSTANCE;
  }

  public Moves getMoves() {
    return mediator.getMoves();
  }

  public void makeMove(Move move) {
    mediator.makeMove(move);
  }

  public void undo() {
    mediator.undoMove();
  }

  public void redo() {
    mediator.redoMove();
  }

  public void registerViews(PerftPanel perftPanel, ChessAreaGui chessAreaGui) {
    this.perftPanel = perftPanel;
    this.chessAreaGui = chessAreaGui;

    this.chessAreaGui.setVisible(true);
    this.perftPanel.setVisible(true);
  }

  public void getPerftTiming(int depth) {
    Fen activeFen = mediator.getFen();

    int iterations = 5;

    double[] times = new double[iterations - 1];

    for (int i = 0; i < iterations; i++) {
      long startTime = System.nanoTime();
      Perft.runPerformanceTest(activeFen, depth, this);
      long endTime = System.nanoTime();
      if (i == 0) {
        continue;
      }
      double totalTime = (endTime - startTime) / 1000000.0;
      EventQueue.invokeLater(() -> perftPanel.addStringToPerftDiffPane(String.format("Time taken: %s ms\n", totalTime)));
      times[i - 1] = totalTime;
    }

    double sum = 0;
    for (double time : times) {
      sum += time;
    }
    double averageTime = sum / (iterations - 1);

    EventQueue.invokeLater(() -> perftPanel.addStringToPerftDiffPane(String.format("\nAverage time taken for depth of %s:\n %s ms\n", depth, averageTime)));
  }

  public void runPerftFromCurrentState(int depth) {
    Fen currentFen = mediator.getFen();

    AtomicReference<List<String>> localPerft = new AtomicReference<>();
    AtomicReference<List<String>> stockfishPerft = new AtomicReference<>();

    Thread localPerftThread = new Thread(() -> localPerft.set(Perft.runPerformanceTest(currentFen, depth, this)));

    Thread stockfishPerftThread = new Thread(() -> stockfishPerft.set(StockfishEngine.runPerftFromFen(currentFen, depth, this)));

    localPerftThread.start();
    stockfishPerftThread.start();

    try {
      localPerftThread.join();
      stockfishPerftThread.join();

      List<String> tempLocalPerft = List.copyOf(localPerft.get());
      List<String> finalLocalPerft = localPerft.get();
      List<String> finalStockfishPerft = stockfishPerft.get();

      finalLocalPerft.removeAll(finalStockfishPerft);
      finalStockfishPerft.removeAll(tempLocalPerft);

      Collections.sort(localPerft.get());
      Collections.sort(stockfishPerft.get());

      for (String s : localPerft.get()) {
        addStringToPerftDiffPane(s + "\n");
      }
      for (String s : stockfishPerft.get()) {
        addStringToStockfishDiffPane(s + "\n");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public Fen getCurrentFen() {
    return mediator.getFen();
  }

  public int[] getSquares() {
    return mediator.getSquares();
  }

  public void loadFen(Fen fen) {
    mediator = new StandardMediator(fen);
    //    chessAreaGui.setSquares(mediator.getSquares());
    //    chessAreaGui.setMoves(mediator.getMoves());
  }

  public void addStringToPerftPane(String string) {
    perftPanel.addStringToPerftPane(string);
  }

  public void addStringToStockfishPane(String string) {
    perftPanel.addStringToStockfishPane(string);
  }

  public void addStringToPerftDiffPane(String string) {
    perftPanel.addStringToPerftDiffPane(string);
  }

  public void addStringToStockfishDiffPane(String string) {
    perftPanel.addStringToStockfishDiffPane(string);
  }
}
