package com.tom.gui;

import static com.tom.gui.model.Constants.BACKGROUND_COLOUR;
import static com.tom.gui.model.Constants.SCREEN_SIZE;
import static com.tom.gui.model.GuiPages.PERFT_GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LandingPage extends JPanel {
  private final GuiController controller;

  public LandingPage() {
    this.controller = GuiController.getInstance();

    setLayout(new BorderLayout());
    setBackground(BACKGROUND_COLOUR);
    setPreferredSize(SCREEN_SIZE);
    add(new ButtonPanel(), BorderLayout.CENTER);
    add(new TitlePanel(), BorderLayout.NORTH);
    setVisible(true);
  }

  private static class TitlePanel extends JPanel {
    public TitlePanel() {
      setLayout(new FlowLayout(FlowLayout.CENTER));
      setBackground(BACKGROUND_COLOUR);
      JLabel title = new JLabel("Chess");
      title.setFont(new Font("Sans-serif", Font.PLAIN, 100));
      add(title);
      setVisible(true);
    }
  }

  private class ButtonPanel extends JPanel {
    JButton perftGuiButton;

    public ButtonPanel() {
      setLayout(new FlowLayout(FlowLayout.CENTER));
      setBackground(BACKGROUND_COLOUR);
      perftGuiButton = new JButton("Perft");

      addButtons();
      addActionListeners();

      setVisible(true);
    }

    private void addButtons() {
      add(perftGuiButton);
    }

    private void addActionListeners() {
      perftGuiButton.addActionListener(e -> controller.setActiveGui(PERFT_GUI));
    }
  }
}
