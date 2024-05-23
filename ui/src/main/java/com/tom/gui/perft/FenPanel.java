package com.tom.gui.perft;

import static com.tom.gui.model.Constants.BACKGROUND_COLOUR;

import com.tom.chess.model.Fen;
import com.tom.gui.exception.InvalidFenFileException;
import com.tom.gui.util.LoadUtil;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FenPanel extends JPanel {

  public FenPanel(PerftGuiController controller) {
    setLayout(new GridLayout(1, 2));
    setBackground(BACKGROUND_COLOUR);

    JButton loadButton = new JButton("Load FEN");
    JButton pasteButton = new JButton("Paste FEN");

    JButton saveButton = new JButton("Save FEN");
    JButton copyButton = new JButton("Copy FEN");

    loadButton.addActionListener(_ -> {
      JFileChooser fileChooser = new JFileChooser();
      int returnValue = fileChooser.showOpenDialog(FenPanel.this);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        // Perform action with loaded file
        try {
          if (!file.getAbsolutePath().endsWith(".fen")) {
            throw new InvalidFenFileException("This is not a .fen file");
          }
          Fen fen = LoadUtil.loadFenFromFile(file.getAbsolutePath());
          controller.loadFen(fen);
          JOptionPane.showMessageDialog(FenPanel.this, "File loaded: " + file.getAbsolutePath());
        } catch (InvalidFenFileException ex) {
          JOptionPane.showMessageDialog(FenPanel.this, "Failed to load file. Check the content and try again.");
        }
      }
    });

    pasteButton.addActionListener(_ -> {
      String fenString = JOptionPane.showInputDialog(FenPanel.this, "Enter a string to paste:");
      Fen fen = Fen.parse(fenString);

      if (LoadUtil.isValidFen(fenString)) {
        controller.loadFen(fen);
        JOptionPane.showMessageDialog(FenPanel.this, "String pasted: " + fenString);
      } else {
        JOptionPane.showMessageDialog(FenPanel.this, "Failed to load FEN string. Check the string and try again.");
      }
    });

    saveButton.addActionListener(_ -> {
      JFileChooser fileChooser = new JFileChooser();
      int returnValue = fileChooser.showSaveDialog(FenPanel.this);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        Fen content = controller.getCurrentFen();
        if (!file.getName().endsWith(".fen")) {
          file = new File(file.getParentFile(), file.getName() + ".fen");
        }
        try {
          FileWriter fileWriter = new FileWriter(file);
          fileWriter.write(content.getFenString());
          fileWriter.close();
          JOptionPane.showMessageDialog(FenPanel.this, "File saved successfully.");
        } catch (IOException ex) {
          JOptionPane.showMessageDialog(FenPanel.this, "Failed to save file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    copyButton.addActionListener(_ -> {
      // Get the system clipboard
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      // Create a StringSelection object with the text to be copied
      StringSelection selection = new StringSelection(controller.getCurrentFen().getFenString());
      // Set the StringSelection object as the clipboard content
      clipboard.setContents(selection, null);
      JOptionPane.showMessageDialog(FenPanel.this, "FEN string copied to clipboard!");
    });


    add(createColumn(loadButton, pasteButton));
    add(createColumn(saveButton, copyButton));
  }

  // Helper method to create a panel for a column with label and buttons
  private static JPanel createColumn(JButton button1, JButton button2) {
    JPanel columnPanel = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column
    columnPanel.add(button1);
    columnPanel.add(button2);
    columnPanel.setBackground(BACKGROUND_COLOUR);

    return columnPanel;
  }
}
