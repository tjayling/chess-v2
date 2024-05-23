package com.tom.gui.util;

import com.tom.chess.model.Fen;
import com.tom.gui.exception.InvalidFenFileException;
import java.io.File;
import java.util.Scanner;

public class LoadUtil {
  public static Fen loadFenFromFile(String url) throws InvalidFenFileException {
    String fenString;
    try {
      File fenFile = new File(url);
      Scanner scanner = new Scanner(fenFile);
      fenString = scanner.nextLine();
      Fen.parse(fenString);
      return Fen.parse(fenString);
    } catch (Exception e) {
      throw new InvalidFenFileException("Invalid FEN string encountered in the file: " + url, e);
    }
  }

  public static Fen loadFenFromFile() {
    try {
      return loadFenFromFile("src/main/resources/boards/initial-board.fen");
    } catch (InvalidFenFileException ex) {
      throw new InvalidFenFileException("Invalid FEN string encountered in the file: src/main/resources/boards/initial-board.fen", ex);
    }
  }

  public static boolean isValidFen(String fen) {
    try {
      Fen.parse(fen);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}