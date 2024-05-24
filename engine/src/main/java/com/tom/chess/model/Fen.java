package com.tom.chess.model;

import static com.tom.chess.piece.PieceConstants.*;
import static com.tom.chess.piece.PieceUtil.getType;
import static com.tom.chess.piece.PieceUtil.isBlack;
import static com.tom.chess.piece.PieceUtil.isWhite;

import com.tom.chess.util.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.tom.exception.InvalidFenException;
import lombok.Getter;

@Getter
public class Fen {
//  private static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
  private static final String STARTING_POSITION = "rbBRrrrr/pp4pp/8/8/b7/7b/PP4PP/R5RR w KQkq - 0 1";

  private final String fenString;
  private final int[] squares;
  private final int friendlyColour;
  private final String enPassantMove;
  private final boolean[] castlingRights;

  private Fen(String fenString, int[] squares, int friendlyColour, String enPassantMove, boolean[] castlingRights) {
    this.fenString = fenString;
    this.squares = squares;
    this.friendlyColour = friendlyColour;
    this.enPassantMove = enPassantMove;
    this.castlingRights = castlingRights;
  }

  public Fen(int[] squares, int friendlyColour, String enPassantMove, boolean[] castlingRights) {
    this.squares = squares;
    this.friendlyColour = friendlyColour;
    this.enPassantMove = enPassantMove;
    this.castlingRights = castlingRights;

    try {
      var newSquaresString = getFenStringFromSquares(squares);
      var newFriendlyColour = isWhite(friendlyColour) ? "w" : "b";
      var newEnPassantMove = StringUtil.isNotEmpty(enPassantMove) ? enPassantMove : "-";
      var newCastlingRightsString = getCastlingRightsStringFromBooleanArray(castlingRights);

      this.fenString = String.join(" ", newSquaresString, newFriendlyColour, newCastlingRightsString, newEnPassantMove);
    } catch (Exception e) {
      throw new InvalidFenException(e);
    }
  }

  public static Fen parse(String fenString) throws InvalidFenException {
    try {
      var fenBoardString = getFenBoardString(fenString);
      var squares = getSquaresFromString(fenBoardString);
      var friendlyColour = getColourToPlayFromString(fenString);
      var enPassantMove = getEnPassantTargetFromString(fenString);
      var castlingRights = getCastlingRightsFromString(fenString);
      return new Fen(fenString, squares, friendlyColour, enPassantMove, castlingRights);
    } catch (Exception e) {
      throw new InvalidFenException(e);
    }
  }

  public static Fen defaultFen() {
    return parse(STARTING_POSITION);
  }

  public static Fen fromFen(String fen) {
    return parse(fen);
  }

  public static String getFenFromSquares(int[] board) {
    StringBuilder encodedBord = new StringBuilder();
    StringBuilder currentRank = new StringBuilder();
    int i = 0;
    for (int piece : board) {
      i++;
      if (piece == 0) {
        currentRank.append('1');
      }
      if (piece != 0) {
        switch (getType(piece)) {
          case KING -> currentRank.append(isBlack(piece) ? "k" : "K");
          case PAWN -> currentRank.append(isBlack(piece) ? "p" : "P");
          case KNIGHT -> currentRank.append(isBlack(piece) ? "n" : "N");
          case BISHOP -> currentRank.append(isBlack(piece) ? "b" : "B");
          case ROOK -> currentRank.append(isBlack(piece) ? "r" : "R");
          case QUEEN -> currentRank.append(isBlack(piece) ? "q" : "Q");
        }
      }
      if (i % 8 == 0) {
        encodedBord.append(currentRank.reverse());
        currentRank = new StringBuilder();

        if (i < 63) {
          encodedBord.append('/');
        }
      }
    }
    return encodedBord.reverse().toString();
  }

  private static int[] getSquaresFromString(String fenBoardString) {
    if (fenBoardString.length() != 64) {
      throw new InvalidFenException("Built fen string is not of length 64");
    }

    Map<Character, Integer> pieceTypeFromSymbol = new HashMap<>() {{
      put('k', KING);
      put('p', PAWN);
      put('n', KNIGHT);
      put('b', BISHOP);
      put('r', ROOK);
      put('q', QUEEN);
    }};

    int[] squares = new int[64];

    for (int i = 0; i < 64; i++) {
      // work from top of board down
      int rank = 7 - (Math.floorDivExact(i, 8));
      int file = i % 8;
      int index = (rank * 8) + file;

      char c = fenBoardString.charAt(i);
      if (Character.isAlphabetic(c)) {
        int colour = (Character.isUpperCase(c) ? WHITE : BLACK);
        int type = pieceTypeFromSymbol.get(Character.toLowerCase(c));
        squares[index] = type | colour;
      }
    }

    return squares;
  }

  public static boolean[] getCastlingRightsFromString(String fen) {
    String castlingRightString = fen.split(" ")[2];
    if (Objects.equals(castlingRightString, "-")) {
      return new boolean[] {false, false, false, false};
    }
    boolean[] castlingRights = new boolean[4];
    castlingRights[0] = castlingRightString.contains("K");
    castlingRights[1] = castlingRightString.contains("Q");
    castlingRights[2] = castlingRightString.contains("k");
    castlingRights[3] = castlingRightString.contains("q");
    return castlingRights;
  }

  private static int getColourToPlayFromString(String fen) {
    String colourToPlay = fen.split(" ")[1];
    return switch (colourToPlay) {
      case "w" -> WHITE;
      case "b" -> BLACK;
      default -> throw new InvalidFenException(new Throwable("Could not parse colour to play from String"));
    };
  }

  private static String getEnPassantTargetFromString(String fen) {
    return fen.split(" ")[3];
  }

  private static String getCastlingRightsStringFromBooleanArray(boolean[] castlingRights) {
    StringBuilder castlingRightsString = new StringBuilder();
    //White King side rights
    if (castlingRights[0]) {
      castlingRightsString.append('K');
    }
    //White Queen side rights
    if (castlingRights[1]) {
      castlingRightsString.append("Q");
    }
    //Black Queen side rights
    if (castlingRights[2]) {
      castlingRightsString.append("k");
    }
    //Black King side rights
    if (castlingRights[3]) {
      castlingRightsString.append("q");
    }

    if (castlingRightsString.isEmpty()) {
      castlingRightsString.append("-");
    }
    return castlingRightsString.toString();
  }

  public String getFenBoardString() {
    return getFenBoardString(fenString);
  }

  public static String getFenBoardString(String s) {
    var rawFenString = s.split("\\s+")[0].replaceAll("/", "");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < rawFenString.length(); i++) {
      var charArray = rawFenString.toCharArray();
      var c = charArray[i];
      if (Character.isDigit(c)) {
        sb.append("0".repeat(Character.getNumericValue(c)));
      } else if (Character.isAlphabetic(charArray[i])) {
        sb.append(charArray[i]);
      }
    }
    return sb.toString();
  }

  public static String getFenStringFromSquares(int[] board) {
    StringBuilder encodedBord = new StringBuilder();
    StringBuilder currentRank = new StringBuilder();
    int i = 0;
    for (int piece : board) {
      i++;
      if (piece == 0) {
        currentRank.append('1');
      }
      if (piece != 0) {
        switch (getType(piece)) {
          case PAWN -> currentRank.append(isBlack(piece) ? "p" : "P");
          case KNIGHT -> currentRank.append(isBlack(piece) ? "n" : "N");
          case BISHOP -> currentRank.append(isBlack(piece) ? "b" : "B");
          case ROOK -> currentRank.append(isBlack(piece) ? "r" : "R");
          case QUEEN -> currentRank.append(isBlack(piece) ? "q" : "Q");
          case KING -> currentRank.append(isBlack(piece) ? "k" : "K");
        }
      }
      if (i % 8 == 0) {
        encodedBord.append(currentRank.reverse());
        currentRank = new StringBuilder();

        if (i < 63) {
          encodedBord.append('/');
        }
      }
    }
    return encodedBord.reverse().toString();
  }
}

