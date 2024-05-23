package com.tom.gui.shared;

import static com.tom.gui.model.Constants.BACKGROUND_COLOUR;
import static com.tom.gui.model.Constants.HEADER_SIZE;
import static com.tom.gui.model.GuiPages.LANDING_PAGE;

import com.tom.gui.GuiController;
import com.tom.gui.model.Constants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HeaderPanel extends JPanel {
  private final GuiController controller;

  public HeaderPanel() {
    this.controller = GuiController.getInstance();

    setLayout(new BorderLayout());
    setBackground(BACKGROUND_COLOUR);
    setPreferredSize(HEADER_SIZE);
    setBorder(new EmptyBorder(0, Constants.BOARD_OFFSET, 0, 0));
    add(new ButtonPanel(), BorderLayout.CENTER);
    setVisible(true);
  }

  private class ButtonPanel extends JPanel {
    JButton homeButton;

    public ButtonPanel() {
      setLayout(new FlowLayout(FlowLayout.LEFT, -2, 0));
      setBackground(BACKGROUND_COLOUR);

      homeButton = new JButton();

      styleButtons();
      addButtons();
      addActionListeners();

      setVisible(true);
    }

    private void styleButtons() {
//      homeButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
      homeButton.setPreferredSize(new Dimension(45, 45));
      homeButton.setMargin(new Insets(0, 0, 0, 0)); // Set margins to zero
      homeButton.setText("\uD83C\uDFE0");
      homeButton.setFont(new Font("Arial", Font.PLAIN, 30));
    }

    private void addButtons() {
      add(homeButton);
    }

    private void addActionListeners() {
      homeButton.addActionListener(e -> controller.setActiveGui(LANDING_PAGE));
    }
  }
}
