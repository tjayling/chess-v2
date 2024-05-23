package com.tom.gui.model;

import static com.tom.gui.model.PieceConstants.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

public class Constants {
  public static final int BOARD_OFFSET = 25;
  public static final int SCREEN_WIDTH = 600 + (BOARD_OFFSET * 2);
  public static final int SCREEN_HEIGHT = 650 + (BOARD_OFFSET * 2);
  public static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
  public static final Dimension HEADER_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT / 15);
  public static final int BOARD_WIDTH = 600;
  public static final int BOARD_HEIGHT = 600;
  public static final int CELLS = 8;
  public static final int CELL_SIZE = BOARD_WIDTH / CELLS;
  public static final Color BACKGROUND_COLOUR = Color.lightGray;
  public static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(0, BOARD_OFFSET, BOARD_OFFSET, BOARD_OFFSET);
  public static final Map<Integer, Image> PIECE_MAP;

  static {

    try {
      PIECE_MAP = new HashMap<>();
      PIECE_MAP.put(KING | WHITE, getImage("assets/white-king.png"));
      PIECE_MAP.put(PAWN | WHITE, getImage("assets/white-pawn.png"));
      PIECE_MAP.put(KNIGHT | WHITE, getImage("assets/white-knight.png"));
      PIECE_MAP.put(BISHOP | WHITE, getImage("assets/white-bishop.png"));
      PIECE_MAP.put(ROOK | WHITE, getImage("assets/white-rook.png"));
      PIECE_MAP.put(QUEEN | WHITE, getImage("assets/white-queen.png"));
      PIECE_MAP.put(KING | BLACK, getImage("assets/black-king.png"));
      PIECE_MAP.put(PAWN | BLACK,getImage("assets/black-pawn.png"));
      PIECE_MAP.put(KNIGHT | BLACK, getImage("assets/black-knight.png"));
      PIECE_MAP.put(BISHOP | BLACK, getImage("assets/black-bishop.png"));
      PIECE_MAP.put(ROOK | BLACK, getImage("assets/black-rook.png"));
      PIECE_MAP.put(QUEEN | BLACK, getImage("assets/black-queen.png"));
    } catch (IOException e) {
      throw new RuntimeException("Could not get piece images");
    }
  }

  private static Image getImage(String location) throws IOException {
    return ImageIO.read(ClassLoader.getSystemResource(location));
  }
}
