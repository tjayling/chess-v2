package com.tom.gui.perft;

import static com.tom.gui.model.Constants.BACKGROUND_COLOUR;
import static com.tom.gui.model.Constants.DEFAULT_BORDER;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class PerftPanel extends JPanel implements ActionListener {
  private final PerftGuiController controller;
  private final JTextArea localOutput;
  private final JTextArea stockfishOutput;
  private final JTextArea localDiffOutput;
  private final JTextArea stockfishDiffOutput;

  public PerftPanel() {
    controller = PerftGuiController.getInstance();
    setLayout(new BorderLayout());
    setFocusable(true);
    setBackground(BACKGROUND_COLOUR);

    setBorder(DEFAULT_BORDER);

    localOutput = new JTextArea();
    localOutput.setEditable(false);
    JScrollPane localScrollPane = new JScrollPane(localOutput);

    stockfishOutput = new JTextArea();
    stockfishOutput.setEditable(false);
    JScrollPane stockfishScrollPane = new JScrollPane(stockfishOutput);

    localDiffOutput = new JTextArea();
    localDiffOutput.setEditable(false);
    JScrollPane localDiffPane = new JScrollPane(localDiffOutput);

    stockfishDiffOutput = new JTextArea();
    stockfishDiffOutput.setEditable(false);
    JScrollPane stockfishDiffPane = new JScrollPane(stockfishDiffOutput);

    // Add margins to
    int scrollPaneMargin = 10;
    localScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(scrollPaneMargin, scrollPaneMargin, scrollPaneMargin, scrollPaneMargin));
    stockfishScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(scrollPaneMargin, scrollPaneMargin, scrollPaneMargin, scrollPaneMargin));

    // Create output panels
    JPanel perftOutputPanel = new JPanel(new GridLayout(1, 2, 10, 0));
    JPanel diffOutputPanel = new JPanel(new GridLayout(1, 2, 10, 0));
    final Dimension DIFF_OUTPUT_DIMENSION = new Dimension(600, 150);
    diffOutputPanel.setPreferredSize(DIFF_OUTPUT_DIMENSION);

    //Set backgrounds of panels
    perftOutputPanel.setBackground(BACKGROUND_COLOUR);
    diffOutputPanel.setBackground(BACKGROUND_COLOUR);

    // Add perft outputs to the perft output panel
    perftOutputPanel.add(localScrollPane);
    perftOutputPanel.add(stockfishScrollPane);

    // Add diff outputs to the diff output panel
    diffOutputPanel.add(localDiffPane);
    diffOutputPanel.add(stockfishDiffPane);

    JPanel outputPanel = new JPanel(new BorderLayout());
    Border border = BorderFactory.createEmptyBorder(0, 0, 10, 0);
    perftOutputPanel.setBorder(border);

    outputPanel.add(perftOutputPanel, BorderLayout.CENTER);
    outputPanel.add(diffOutputPanel, BorderLayout.SOUTH);

    add(outputPanel, BorderLayout.CENTER);

    JPanel buttonPanel = new ButtonPanel();

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private class ButtonPanel extends JPanel {
    PerftOptionsPanel optionsPanel;
    FenPanel fenPanel;

    public ButtonPanel() {
      setLayout(new BorderLayout());
      setBackground(BACKGROUND_COLOUR);
      this.optionsPanel = new PerftOptionsPanel();
      this.fenPanel = new FenPanel(controller);
      addPanels();
    }

    private void addPanels() {
      add(optionsPanel, BorderLayout.NORTH);
      add(fenPanel, BorderLayout.SOUTH);
    }
  }

  private class PerftOptionsPanel extends JPanel {
    private final JComboBox<Integer> comboBox;
    private final JButton doPerftButton;
    private final JButton doEfficiencyTestButton;
    private final JButton clearOutputButton;

    public PerftOptionsPanel() {
      setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
      setBackground(BACKGROUND_COLOUR);
      setVisible(true);

      Integer[] depths = {1, 2, 3, 4, 5, 6, 7};
      comboBox = new JComboBox<>(depths);
      doPerftButton = new JButton("Do perft");
      doEfficiencyTestButton = new JButton("Do efficiency test");
      clearOutputButton = new JButton("Clear output");

      addButtons();
      addActionListeners();
    }

    private void addButtons() {
      add(comboBox);
      add(doPerftButton);
      add(doEfficiencyTestButton);
      add(clearOutputButton);
    }

    private void addActionListeners() {
      final String LINE_BREAK = "---------------------------\n\n";

      // Attach an ActionListener to the doPerftButton
      doPerftButton.addActionListener(_ -> {
        if (!localOutput.getText().isEmpty()) {
          localOutput.append(LINE_BREAK);
        }
        if (!stockfishOutput.getText().isEmpty()) {
          stockfishOutput.append(LINE_BREAK);
        }
        if (!localDiffOutput.getText().isEmpty()) {
          localDiffOutput.append(LINE_BREAK);
        }
        if (!stockfishDiffOutput.getText().isEmpty()) {
          stockfishDiffOutput.append(LINE_BREAK);
        }
        Integer depth = (Integer) comboBox.getSelectedItem();
        if (depth != null) {
          String message = String.format("Starting perft test to a depth of %s...\n", depth);
          localOutput.append(message);
          stockfishOutput.append(message);

          new Thread(() -> controller.runPerftFromCurrentState(depth)).start();
        }
      });

      doEfficiencyTestButton.addActionListener(_ -> {
        if (!localOutput.getText().isEmpty()) {
          localOutput.append(LINE_BREAK);
        }
        if (!localDiffOutput.getText().isEmpty()) {
          localDiffOutput.append(LINE_BREAK);
        }
        Integer depth = (Integer) comboBox.getSelectedItem();
        if (depth != null) {
          localOutput.append(String.format("Starting efficiency test to a depth of %s...\n", depth));

          new Thread(() -> controller.getPerftTiming(depth)).start();
        }
      });

      clearOutputButton.addActionListener(_ -> {
        localOutput.setText("");
        stockfishOutput.setText("");
        localDiffOutput.setText("");
        stockfishDiffOutput.setText("");
      });
    }
  }


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    repaint();
  }

  public void addStringToPerftPane(String string) {
    localOutput.append(string);
  }

  public void addStringToStockfishPane(String string) {
    stockfishOutput.append(string);
  }

  public void addStringToPerftDiffPane(String string) {
    localDiffOutput.append(string);
  }

  public void addStringToStockfishDiffPane(String string) {
    stockfishDiffOutput.append(string);
  }
}
