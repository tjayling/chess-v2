package com.tom.chess.movegen;

import static com.tom.chess.movegen.precomputed.BishopDataGenerator.BISHOP_LOOKUP;
import static com.tom.chess.movegen.precomputed.BishopDataGenerator.BISHOP_MASKS;
import static com.tom.chess.movegen.precomputed.KingDataGenerator.KING_MASKS;
import static com.tom.chess.movegen.precomputed.KnightDataGenerator.KNIGHT_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.BLACK_PAWN_ATTACK_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.WHITE_PAWN_ATTACK_MASKS;
import static com.tom.chess.movegen.precomputed.QueenDataGenerator.QUEEN_LOOKUP;
import static com.tom.chess.movegen.precomputed.QueenDataGenerator.QUEEN_MASKS;
import static com.tom.chess.movegen.precomputed.RookDataGenerator.ROOK_LOOKUP;
import static com.tom.chess.movegen.precomputed.RookDataGenerator.ROOK_MASKS;
import static com.tom.chess.piece.PieceConstants.BISHOP;
import static com.tom.chess.piece.PieceConstants.BLACK;
import static com.tom.chess.piece.PieceConstants.KNIGHT;
import static com.tom.chess.piece.PieceConstants.PAWN;
import static com.tom.chess.piece.PieceConstants.QUEEN;
import static com.tom.chess.piece.PieceConstants.ROOK;
import static com.tom.chess.piece.PieceConstants.WHITE;

import com.tom.chess.model.BitBoard;
import com.tom.chess.model.GameState;
import com.tom.chess.movegen.precomputed.Identifier;
import com.tom.chess.util.Coord;

public class ThreatMapGenerator {
  // @formatter:off
  public void generateThreatMapAndChecks(final GameState gameState) {
    gameState.setThreatMap(
        BitBoard.from(getPawnThreatMap(gameState),
            getKnightThreatMap(gameState),
            getBishopThreatMap(gameState),
            getRookThreatMap(gameState),
            getQueenThreatMap(gameState),
            getKingThreatMap(gameState)
        )
    );
    generatePinMask(gameState);
  }
  // @formatter:on

  private BitBoard getPawnThreatMap(GameState gameState) {
    BitBoard threatMap = BitBoard.empty();
    BitBoard opponentPawns;
    long[] masks;
    switch (gameState.getFriendlyColour()) {
      case BLACK -> {
        opponentPawns = gameState.getBitboards().getWhitePawns();
        masks = WHITE_PAWN_ATTACK_MASKS;
      }
      case WHITE -> {
        opponentPawns = gameState.getBitboards().getBlackPawns();
        masks = BLACK_PAWN_ATTACK_MASKS;
      }
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    }


    for (int position : opponentPawns.getPositions()) {
      var threat = BitBoard.from(masks[position]);

      threatMap = threatMap.or(threat);
      checkForCheck(gameState, threat, BitBoard.fromPosition(position), PAWN);
    }

    return threatMap;
  }

  private BitBoard getKnightThreatMap(GameState gameState) {
    BitBoard threatMap = BitBoard.empty();

    BitBoard knights = switch (gameState.getFriendlyColour()) {
      case BLACK -> gameState.getBitboards().getWhiteKnights();
      case WHITE -> gameState.getBitboards().getBlackKnights();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    for (int position : knights.getPositions()) {
      var threat = BitBoard.from(KNIGHT_MASKS[position]);

      threatMap = threatMap.or(threat);
      checkForCheck(gameState, threat, BitBoard.fromPosition(position), KNIGHT);
    }


    return threatMap;
  }

  private BitBoard getBishopThreatMap(GameState gameState) {
    BitBoard threatMap = BitBoard.empty();

    BitBoard bishops = switch (gameState.getFriendlyColour()) {
      case BLACK -> gameState.getBitboards().getWhiteBishops();
      case WHITE -> gameState.getBitboards().getBlackBishops();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    for (int position : bishops.getPositions()) {
      var bishopMask = BISHOP_MASKS[position];
      var blockerMask = blockers.and(bishopMask).getBoard();
      var identifier = new Identifier(position, blockerMask);
      var threat = gameState.getFriendlyBitBoard().not().and(BISHOP_LOOKUP.get(identifier));

      threatMap = threatMap.or(threat);
      checkForCheck(gameState, threat, BitBoard.fromPosition(position), BISHOP);
    }

    return threatMap;
  }

  private BitBoard getRookThreatMap(GameState gameState) {
    BitBoard threatMap = BitBoard.empty();

    BitBoard rooks = switch (gameState.getFriendlyColour()) {
      case BLACK -> gameState.getBitboards().getWhiteRooks();
      case WHITE -> gameState.getBitboards().getBlackRooks();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    for (int position : rooks.getPositions()) {
      var rookMask = ROOK_MASKS[position];
      var blockerMask = blockers.and(rookMask).getBoard();
      var identifier = new Identifier(position, blockerMask);
      var threat = gameState.getFriendlyBitBoard().not().and(ROOK_LOOKUP.get(identifier));

      threatMap = threatMap.or(threat);
      checkForCheck(gameState, threat, BitBoard.fromPosition(position), ROOK);
    }

    return threatMap;
  }

  private BitBoard getQueenThreatMap(GameState gameState) {
    BitBoard threatMap = BitBoard.empty();

    BitBoard queens = switch (gameState.getFriendlyColour()) {
      case BLACK -> gameState.getBitboards().getWhiteQueens();
      case WHITE -> gameState.getBitboards().getBlackQueens();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    for (int position : queens.getPositions()) {
      var queenMask = QUEEN_MASKS[position];
      var blockerMask = blockers.and(queenMask).getBoard();
      var identifier = new Identifier(position, blockerMask);
      var threat = gameState.getFriendlyBitBoard().not().and(QUEEN_LOOKUP.get(identifier));

      threatMap = threatMap.or(threat);
      checkForCheck(gameState, threat, BitBoard.fromPosition(position), QUEEN);
    }

    return threatMap;
  }

  private BitBoard getKingThreatMap(GameState gameState) {
    return new BitBoard(KING_MASKS[gameState.getOpponentKingPosition()]);
  }

  private void checkForCheck(GameState gameState, BitBoard threatMap, BitBoard attacker, int pieceType) {
    boolean check = threatMap.not().and(BitBoard.fromPosition(gameState.getFriendlyKingPosition())).hasPositions();
    gameState.setCheck(check);
    if (check) {
      gameState.addCheckingPieceType(pieceType);
      gameState.setCheckingPieces(gameState.getCheckingPieces().or(attacker.getLastPosition()));
    }
  }

  private void generatePinMask(GameState gameState) {
    var friendlyKingPosition = gameState.getFriendlyKingPosition();
    var kingRays = QUEEN_MASKS[friendlyKingPosition];

    BitBoard opponentSliders;

    switch (gameState.getFriendlyColour()) {
      case WHITE -> opponentSliders = gameState.getBitboards().getBlackBishops().or(gameState.getBitboards().getBlackRooks()).or(gameState.getBitboards().getBlackQueens());
      case BLACK -> opponentSliders = gameState.getBitboards().getWhiteBishops().or(gameState.getBitboards().getWhiteRooks()).or(gameState.getBitboards().getWhiteQueens());
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    }

    var overlaps = opponentSliders.and(kingRays);

    var result = BitBoard.empty();
    if (overlaps.hasPositions()) {
      for (var position : overlaps.getPositions()) {
        result = result.or(drawLine(Coord.fromPosition(position), Coord.fromPosition(friendlyKingPosition)));
      }
    } else {
      result = BitBoard.empty().not();
    }

    result = result.or(gameState.getCheckingPieces());

    gameState.setPinMask(result);
  }

  private BitBoard drawLine(Coord start, Coord end) {
    BitBoard result = BitBoard.empty();

    int dx = Math.abs(end.getX() - start.getX());
    int dy = Math.abs(end.getY() - start.getY());
    int sx = start.getX() < end.getX() ? 1 : -1;
    int sy = start.getY() < end.getY() ? 1 : -1;
    int err = dx - dy;

    int x = start.getX();
    int y = start.getY();

    while (true) {
      int pos = (y * 8) + x;
      result = result.addBitAtIndex(pos);

      if (x == end.getX() && y == end.getY()) {break;}
      int e2 = 2 * err;
      if (e2 > -dy) {
        err -= dy;
        x += sx;
      }
      if (e2 < dx) {
        err += dx;
        y += sy;
      }
    }
    return result;
  }
}
