package com.tom.chess.model;

import static com.tom.chess.model.StaticData.SQUARE_MAP;
import static com.tom.chess.piece.PieceConstants.BISHOP;
import static com.tom.chess.piece.PieceConstants.BLACK;
import static com.tom.chess.piece.PieceConstants.EMPTY;
import static com.tom.chess.piece.PieceConstants.KING;
import static com.tom.chess.piece.PieceConstants.KNIGHT;
import static com.tom.chess.piece.PieceConstants.QUEEN;
import static com.tom.chess.piece.PieceConstants.ROOK;
import static com.tom.chess.piece.PieceConstants.WHITE;
import static java.lang.Math.floor;

import com.tom.chess.piece.PieceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class GameState {
  private Fen fen;
  private BitBoards bitboards;
  private int friendlyKingPosition;

  private BitBoard threatMap;
  private BitBoard pinMask;
  private boolean check;
  // todo: following to may not be needed... to review
  private List<Integer> checkingPieceTypes;
  private BitBoard checkingPieces;

  public GameState(Fen fen) {
    this.fen = fen;
    this.bitboards = new BitBoards(fen);
    this.friendlyKingPosition = getFriendlyKingPosition();

    this.pinMask = BitBoard.empty();
    this.check = false;
    this.checkingPieceTypes = new ArrayList<>();
    this.checkingPieces = BitBoard.empty();
  }

  public GameState(GameState gameState, BitBoard threatMap, BitBoard pinMask) {
    this.fen = gameState.getFen();
    this.bitboards = gameState.getBitboards();
    this.friendlyKingPosition = gameState.getFriendlyKingPosition();

    this.threatMap = threatMap;
    this.pinMask = pinMask;
    this.check = gameState.isCheck();
    // todo: following to may not be needed... to review
    this.checkingPieceTypes = gameState.getCheckingPieceTypes();
    this.checkingPieces = gameState.getCheckingPieces();
  }

  public GameState move(Move move) {
    int startPos = move.getStartSquare();
    int targetPos = move.getTargetSquare();

    if (shouldCastle(startPos, targetPos)) {
      return castle(targetPos);
    }

    var pieceToMove = fen.getSquares()[startPos];
    var takenPiece = fen.getSquares()[targetPos];
    var newCastlingRights = fen.getCastlingRights().clone();
    var newEnPassantMoves = fen.getEnPassantTarget();
    var newSquares = fen.getSquares().clone();


    // Move pieces
    newSquares[startPos] = EMPTY;
    newSquares[targetPos] = pieceToMove;
    // Check castling rights
    if (PieceUtil.isRook(pieceToMove)) {
      newCastlingRights = getNewCastlingRights();
    }

    if (PieceUtil.isPawn(pieceToMove)) {
      int targetRank = (int) (floor(targetPos / 8f) + 1);
      // Play en passant move
      if (takenPiece == EMPTY) {
        newSquares = playEnPassantMoves(newSquares, startPos, targetPos);
      }
      // Then check for en passant moves on the next iteration
      newEnPassantMoves = getNewEnPassantMoves(move, targetRank);
      // We don't need to check what colour because pawns can't move backwards, so target will always be one of the edge ranks
      if (targetRank == 8 || targetRank == 1) {
        var promotingTo = move.getMove().substring(4, 5);
        newSquares = promote(newSquares, targetPos, promotingTo);
      }
    }

    if (takenPiece > 0) {
      // Check castling rights
      if (PieceUtil.isRook(takenPiece)) {
        newCastlingRights = getNewCastlingRights(targetPos);
      }
      //todo: remove piece
    }

    var newFriendlyColour = PieceUtil.getOppositeColour(fen.getFriendlyColour());

    return new GameState(new Fen(newSquares, newFriendlyColour, newEnPassantMoves, newCastlingRights));
  }

  private boolean[] getNewCastlingRights(int piece) {
    boolean[] newCastlingRights = fen.getCastlingRights().clone();
    if (PieceUtil.isRook(piece)) {
      newCastlingRights = getNewCastlingRights();
    } else if (PieceUtil.isKing(piece)) {
      newCastlingRights = getNewCastlingRights();
    }
    return newCastlingRights;
  }

  private boolean shouldCastle(int startPosition, int targetPosition) {
    var friendlyPositionIsValid = friendlyKingPosition == 4 || friendlyKingPosition == 60;
    var targetIsCastleMove = targetPosition == 2 || targetPosition == 6 || targetPosition == 58 || targetPosition == 62;

    return (startPosition == friendlyKingPosition) && (friendlyPositionIsValid && targetIsCastleMove);
  }

  private boolean[] getNewCastlingRights() {
    var newCastlingRights = fen.getCastlingRights().clone();
    switch (fen.getFriendlyColour()) {
      case WHITE -> {
        newCastlingRights[0] = false;
        newCastlingRights[1] = false;
      }
      case BLACK -> {
        newCastlingRights[2] = false;
        newCastlingRights[3] = false;
      }
    }
    return newCastlingRights;
  }

  private GameState castle(int targetPosition) {
    var newSquares = fen.getSquares().clone();

    switch (friendlyKingPosition) {
      // If white king start position
      case 4 -> {
        switch (targetPosition) {
          case 2 -> {
            newSquares[0] = 0;
            newSquares[4] = 0;
            newSquares[2] = WHITE | KING;
            newSquares[3] = WHITE | ROOK;
          }
          case 6 -> {
            newSquares[4] = 0;
            newSquares[7] = 0;
            newSquares[6] = WHITE | KING;
            newSquares[5] = WHITE | ROOK;
          }
        }
      }
      // If black king start position
      case 60 -> {
        switch (targetPosition) {
          case 58 -> {
            newSquares[56] = 0;
            newSquares[60] = 0;
            newSquares[58] = BLACK | KING;
            newSquares[59] = BLACK | ROOK;
          }
          case 62 -> {
            newSquares[60] = 0;
            newSquares[63] = 0;
            newSquares[62] = BLACK | KING;
            newSquares[61] = BLACK | ROOK;
          }
        }
      }
    }

    var newCastlingRights = getNewCastlingRights();
    var newFriendlyColour = PieceUtil.getOppositeColour(fen.getFriendlyColour());
    var newFen = new Fen(newSquares, newFriendlyColour, fen.getEnPassantTarget(), newCastlingRights);
    return new GameState(newFen);
  }

  private String getNewEnPassantMoves(Move move, int targetRank) {
    int startPos = move.getStartSquare();
    int targetPos = move.getTargetSquare();
    int startRank = (int) (floor(startPos / 8f) + 1);

    var result = "-";

    if ((PieceUtil.isWhite(fen.getFriendlyColour()) && startRank != 2) || (PieceUtil.isBlack(fen.getFriendlyColour()) && startRank != 7)) {
      return result;
    }

    int targetFile = targetPos % 8 + 1;
    if (PieceUtil.isWhite(fen.getFriendlyColour()) && targetRank == 4) {
      result = generateEnPassantMoves(targetPos, targetFile, BLACK);
    }
    if (PieceUtil.isBlack(fen.getFriendlyColour()) && targetRank == 5) {
      result = generateEnPassantMoves(targetPos, targetFile, WHITE);
    }
    return Objects.isNull(result) ? "-" : result;
  }

  private String generateEnPassantMoves(int targetPos, int targetFile, int opponentColour) {
    int targetPawnPos = opponentColour == BLACK ? targetPos - 8 : targetPos + 8;
    if (targetFile - 1 >= 1) {
      int leftPiece = fen.getSquares()[targetPos - 1];
      if (PieceUtil.isColour(leftPiece, opponentColour) && PieceUtil.isPawn(leftPiece)) {
        return SQUARE_MAP[targetPawnPos];
      }
    }
    if (targetFile + 1 <= 8) {
      int rightPiece = fen.getSquares()[targetPos + 1];
      if (PieceUtil.isColour(rightPiece, opponentColour) && PieceUtil.isPawn(rightPiece)) {
        return SQUARE_MAP[targetPawnPos];
      }
    }
    return null;
  }

  private int[] playEnPassantMoves(int[] squares, int startPos, int targetPos) {
    boolean isAttacking;
    int attackedPiece;
    boolean pieceUnderneathIsOpponentPawn;
    var newSquares = squares.clone();

    switch (fen.getFriendlyColour()) {
      case WHITE -> {
        isAttacking = startPos + 8 != targetPos;
        attackedPiece = squares[targetPos - 8];
        pieceUnderneathIsOpponentPawn = PieceUtil.isPawn(attackedPiece) && PieceUtil.isBlack(attackedPiece);
        if (isAttacking && pieceUnderneathIsOpponentPawn) {
          newSquares[targetPos - 8] = 0;
        }
      }
      case BLACK -> {
        isAttacking = startPos - 8 != targetPos;
        attackedPiece = squares[targetPos + 8];
        pieceUnderneathIsOpponentPawn = PieceUtil.isPawn(attackedPiece) && PieceUtil.isWhite(attackedPiece);
        if (isAttacking && pieceUnderneathIsOpponentPawn) {
          newSquares[targetPos + 8] = 0;
        }
      }
      //Todo: add taken piece to list
    }
    return newSquares;
  }

  private int[] promote(int[] squares, int targetPos, String promotingTo) {
    var newSquares = squares.clone();
    // Will take data from the request with the piece type to promote to.
    switch (promotingTo) {
      case "r" -> newSquares[targetPos] = fen.getFriendlyColour() | ROOK;
      case "n" -> newSquares[targetPos] = fen.getFriendlyColour() | KNIGHT;
      case "b" -> newSquares[targetPos] = fen.getFriendlyColour() | BISHOP;
      case "q" -> newSquares[targetPos] = fen.getFriendlyColour() | QUEEN;
    }
    return newSquares;
  }

  public int getFriendlyKingPosition() {
    return switch (fen.getFriendlyColour()) {
      case WHITE -> bitboards.getWhiteKing().getLastPosition();
      case BLACK -> bitboards.getBlackKing().getLastPosition();
      default -> throw new RuntimeException("Error finding friendly king position");
    };
  }

  public int getOpponentKingPosition() {
    return switch (fen.getFriendlyColour()) {
      case BLACK -> bitboards.getWhiteKing().getLastPosition();
      case WHITE -> bitboards.getBlackKing().getLastPosition();
      default -> throw new RuntimeException("Error finding friendly king position");
    };
  }

  public int getFriendlyColour() {
    return fen.getFriendlyColour();
  }

  public BitBoard getFriendlyBitBoard() {
    return switch (fen.getFriendlyColour()) {
      case WHITE -> bitboards.getWhitePieces();
      case BLACK -> bitboards.getBlackPieces();
      default -> throw new IllegalStateException("Unexpected value: %s" .formatted(fen.getFriendlyColour()));
    };
  }

  public void addCheckingPieceType(int pieceType) {
    this.checkingPieceTypes.add(pieceType);
  }
}
