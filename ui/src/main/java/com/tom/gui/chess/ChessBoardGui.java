package com.tom.gui.chess;

import static com.tom.gui.model.Constants.BACKGROUND_COLOUR;
import static com.tom.gui.model.Constants.BOARD_HEIGHT;
import static com.tom.gui.model.Constants.BOARD_OFFSET;
import static com.tom.gui.model.Constants.BOARD_WIDTH;
import static com.tom.gui.model.Constants.CELLS;
import static com.tom.gui.model.Constants.CELL_SIZE;
import static com.tom.gui.model.Constants.PIECE_MAP;

import com.tom.chess.model.Move;
import com.tom.chess.model.Moves;
import com.tom.gui.perft.PerftGuiController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
import lombok.Setter;

public class ChessBoardGui extends JPanel implements ActionListener {
  private final PerftGuiController controller;
//  @Setter
//  private Moves moves;
  @Setter
  private int selectedStartSquare = -1;
  private boolean pieceIsSelected = false;
  private Moves selectedMoves = Moves.empty();

  public ChessBoardGui() {
    this.controller = PerftGuiController.getInstance();

    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    setFocusable(true);
    setBackground(BACKGROUND_COLOUR);
    addMouseListener(new MouseAdapterImpl());

    Timer timer = new Timer(0, this);
    timer.start();
  }

  private class MouseAdapterImpl extends MouseAdapter {
    @Override
    public void mouseReleased(MouseEvent e) {
      int mouseX = e.getX();
      int mouseY = e.getY();
      if (mouseX < BOARD_WIDTH + BOARD_OFFSET && mouseY < BOARD_HEIGHT + BOARD_OFFSET) {
        int file = Math.floorDiv(mouseX, CELL_SIZE);
        int rank = 7 - Math.floorDiv(mouseY, CELL_SIZE);

        if (pieceIsSelected) {
          int selectedTargetSquare = (rank * 8) + file;
          for (Move move : controller.getMoves()) {
            int startSquare = move.getStartSquare();
            int targetSquare = move.getTargetSquare();
            if (startSquare == selectedStartSquare && targetSquare == selectedTargetSquare) {
              controller.makeMove(move);

              pieceIsSelected = false;
              selectedStartSquare = -1;
              selectedMoves = Moves.empty();
              return;
            }
          }
        }

        selectedStartSquare = (rank * 8) + file;
        selectedMoves = Moves.empty();
        for (Move move : controller.getMoves()) {
          if (move.getStartSquare() == selectedStartSquare) {
            selectedMoves.add(move);
          }
        }
        pieceIsSelected = true;
        return;

      }
      pieceIsSelected = false;
    }
  }

  public void drawBoard(Graphics2D g2, int[] squares) {
    if (squares == null) {
      return;
    }

    int pieceOffset = 12;
    int size = 50;

    // Draw board
    g2.setStroke(new java.awt.BasicStroke(3));
    g2.setColor(Color.black);
    for (int file = 0; file < CELLS; file++) {
      for (int rank = 0; rank < CELLS; rank++) {
        if ((file + rank) % 2 == 1) {
          g2.fillRect(BOARD_HEIGHT - CELL_SIZE - (file * CELL_SIZE), BOARD_HEIGHT - CELL_SIZE - (rank * CELL_SIZE), CELL_SIZE, CELL_SIZE);
        }
      }
    }

    // Draw selected square and pieces
    g2.setColor(Color.RED);
    for (int rank = 0; rank < CELLS; rank++) {
      for (int file = 0; file < CELLS; file++) {
        int currentPosition = file + (rank * 8);
        if (selectedStartSquare == currentPosition && squares[currentPosition] != 0) {
          g2.drawRect(file * CELL_SIZE, BOARD_HEIGHT - ((rank + 1) * CELL_SIZE), CELL_SIZE, CELL_SIZE);
        }
        if (squares[currentPosition] == 0) {
          continue;
        }
        g2.drawImage(PIECE_MAP.get(squares[currentPosition]), file * CELL_SIZE + pieceOffset, BOARD_HEIGHT - ((rank + 1) * CELL_SIZE) + pieceOffset, size, size, null);
      }
    }

    // Draw possible moves
    for (int rank = 0; rank < CELLS; rank++) {
      for (int file = 0; file < CELLS; file++) {
        int currentPosition = file + (rank * 8);
        g2.setColor(new Color(20, 167, 204, 200));
        for (Move move : selectedMoves) {
          if (move.getTargetSquare() == currentPosition) {
            g2.fillOval(file * CELL_SIZE + pieceOffset + 10, BOARD_HEIGHT - ((rank + 1) * CELL_SIZE) + pieceOffset + 10, 30, 30);
          }
        }
      }
    }
  }

  public void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(Color.WHITE);
    g2.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
    g2.setColor(Color.black);
    drawBoard(g2, controller.getSquares());
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    repaint();
  }
}
