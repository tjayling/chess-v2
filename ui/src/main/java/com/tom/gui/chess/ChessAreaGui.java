package com.tom.gui.chess;

import static com.tom.gui.model.Constants.BACKGROUND_COLOUR;
import static com.tom.gui.model.Constants.DEFAULT_BORDER;

import com.tom.chess.model.Moves;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPanel;

public class ChessAreaGui extends JPanel implements ActionListener {
  private final ChessBoardGui chessBoardGui;
  private final ChessOptionsGui chessOptionsGui;

  public ChessAreaGui() {
    this.chessBoardGui = new ChessBoardGui();
    this.chessOptionsGui = new ChessOptionsGui();

    setLayout(new BorderLayout());
    setFocusable(true);
    setBackground(BACKGROUND_COLOUR);
    setBorder(DEFAULT_BORDER);
    add(chessBoardGui, BorderLayout.NORTH);
    add(chessOptionsGui, BorderLayout.SOUTH);
  }

//  public void setSquares(int[] squares) {
//    chessBoardGui.setSquares(squares);
//  }
//
//  public void setMoves(Moves moves) {
//    chessBoardGui.setMoves(moves);
//  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }
}
