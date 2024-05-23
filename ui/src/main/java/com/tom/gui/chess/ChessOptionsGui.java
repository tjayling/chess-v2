package com.tom.gui.chess;

import static com.tom.gui.model.Constants.BACKGROUND_COLOUR;
import static com.tom.gui.model.GuiPages.LANDING_PAGE;

import com.tom.gui.perft.PerftGuiController;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ChessOptionsGui extends JPanel implements ActionListener {
  private final PerftGuiController controller;

  public ChessOptionsGui() {
    this.controller = PerftGuiController.getInstance();

    setFocusable(true);
    setBackground(BACKGROUND_COLOUR);

    add(new UndoRedoButtons());

    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }

  private class UndoRedoButtons extends JPanel {

    public UndoRedoButtons() {
      setBackground(BACKGROUND_COLOUR);

      JButton undoButton = new JButton();
      JButton redoButton = new JButton();

      styleButton(undoButton, "Undo");
      styleButton(redoButton, "Redo");

      addButton(undoButton);
      addButton(redoButton);

      addActionListener(undoButton, _ -> controller.undo());
      addActionListener(redoButton, _ -> controller.redo());

      setVisible(true);
    }

    private void styleButton(JButton button, String text) {
      //      homeButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
//      button.setPreferredSize(new Dimension(45, 45));
      button.setMargin(new Insets(0, 0, 0, 0)); // Set margins to zero
      button.setText(text);
      button.setFont(new Font("Arial", Font.PLAIN, 15));
    }

    private void addButton(JButton button) {
      add(button);
    }


    private void addActionListener(JButton button, ActionListener actionListener) {
      button.addActionListener(actionListener);
    }
  }
}
