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
import static com.tom.chess.piece.PieceConstants.BLACK;
import static com.tom.chess.piece.PieceConstants.WHITE;

import com.tom.chess.model.BitBoard;
import com.tom.chess.model.GameState;
import com.tom.chess.movegen.precomputed.Identifier;

public class ThreatMapGenerator {

  public BitBoard generateThreatMap(final GameState gameState) {
    return BitBoard.from(
        getPawnThreatMap(gameState),
        getKnightThreatMap(gameState),
        getBishopThreatMap(gameState),
        getRookThreatMap(gameState),
        getQueenThreatMap(gameState),
        getKingThreatMap(gameState)
    );
  }

  private BitBoard getPawnThreatMap(GameState gameState) {
    BitBoard threatMap = BitBoard.empty();
    switch (gameState.getFriendlyColour()) {
      case BLACK -> {
        var opponentPawns = gameState.getBitboards().getWhitePawns();
        for (int position : opponentPawns.getPositions()) {
          threatMap = threatMap.or(WHITE_PAWN_ATTACK_MASKS[position]);
        }
      }
      case WHITE -> {
        var opponentPawns = gameState.getBitboards().getBlackPawns();
        for (int position : opponentPawns.getPositions()) {
          threatMap = threatMap.or(BLACK_PAWN_ATTACK_MASKS[position]);
        }
      }
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    }

    System.out.println("PAWNS");
    threatMap.print();
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
      threatMap = threatMap.or(KNIGHT_MASKS[position]);
    }

    System.out.println("KNGIHT");
    threatMap.print();
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

    for (int bishopPosition : bishops.getPositions()) {
      var bishopMask = BISHOP_MASKS[bishopPosition];
      var blockerMask = blockers.and(bishopMask).getBoard();
      var identifier = new Identifier(bishopPosition, blockerMask);
      threatMap = threatMap.or(gameState.getFriendlyBitBoard().not().and(BISHOP_LOOKUP.get(identifier)));
    }

    System.out.println("BSHOP");
    threatMap.print();
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

    for (int rookPosition : rooks.getPositions()) {
      var rookMask = ROOK_MASKS[rookPosition];
      var blockerMask = blockers.and(rookMask).getBoard();
      var identifier = new Identifier(rookPosition, blockerMask);
      threatMap = threatMap.or(gameState.getFriendlyBitBoard().not().and(ROOK_LOOKUP.get(identifier)));
    }

    System.out.println("RUK");
    threatMap.print();
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

    for (int queenPosition : queens.getPositions()) {
      var queenMask = QUEEN_MASKS[queenPosition];
      var blockerMask = blockers.and(queenMask).getBoard();
      var identifier = new Identifier(queenPosition, blockerMask);
      threatMap = threatMap.or(gameState.getFriendlyBitBoard().not().and(QUEEN_LOOKUP.get(identifier)));
    }

    System.out.println("QUEEN");
    threatMap.print();
    return threatMap;
  }

  private BitBoard getKingThreatMap(GameState gameState) {
    var threatMap = new BitBoard(KING_MASKS[gameState.getOpponentKingPosition()]);

    System.out.println("KING");
    threatMap.print();
    return threatMap;
  }
}
