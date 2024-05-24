package com.tom.chess.movegen;

import static com.tom.chess.movegen.precomputed.BishopDataGenerator.BISHOP_LOOKUP;
import static com.tom.chess.movegen.precomputed.BishopDataGenerator.BISHOP_MASKS;
import static com.tom.chess.movegen.precomputed.KingDataGenerator.KING_MASKS;
import static com.tom.chess.movegen.precomputed.KnightDataGenerator.KNIGHT_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.BLACK_PAWN_ATTACK_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.BLACK_PAWN_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.WHITE_PAWN_ATTACK_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.WHITE_PAWN_MASKS;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_3;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_6;
import static com.tom.chess.movegen.precomputed.QueenDataGenerator.QUEEN_LOOKUP;
import static com.tom.chess.movegen.precomputed.QueenDataGenerator.QUEEN_MASKS;
import static com.tom.chess.movegen.precomputed.RookDataGenerator.ROOK_LOOKUP;
import static com.tom.chess.movegen.precomputed.RookDataGenerator.ROOK_MASKS;
import static com.tom.chess.piece.PieceConstants.BLACK;
import static com.tom.chess.piece.PieceConstants.WHITE;

import com.tom.chess.model.BitBoard;
import com.tom.chess.model.GameState;
import com.tom.chess.model.Moves;
import com.tom.chess.movegen.precomputed.Identifier;

public class MoveGenerator {
  private final ThreatMapGenerator threatMapGenerator;

  public MoveGenerator() {
    threatMapGenerator = new ThreatMapGenerator();
  }

  public Moves generateMoves(final GameState gameState) {
    var threatMap = threatMapGenerator.generateThreatMap(gameState);
    var startTime = System.nanoTime();

    var moves = getMoves(gameState);

    var endTime = System.nanoTime();
    long startupTime = endTime - startTime;

    System.out.println("Moves generated in " + startupTime + "nanoseconds.");
    return moves;
  }

  private Moves getMoves(GameState gameState) {
    Moves moves = new Moves();
    moves.addAll(getPawnMoves(gameState));
    moves.addAll(getKnightMoves(gameState));
    moves.addAll(getBishopMoves(gameState));
    moves.addAll(getRookMoves(gameState));
    moves.addAll(getQueenMoves(gameState));
    moves.addAll(getKingMoves(gameState));
    return moves;
  }

  private Moves getPawnMoves(GameState gameState) {
    Moves moves = new Moves();
    var empty = gameState.getBitboards().getEmpty();

    switch (gameState.getFriendlyColour()) {
      case WHITE -> {
        var friendlyPawns = gameState.getBitboards().getWhitePawns();
        var opponentPieces = gameState.getBitboards().getBlackPieces();

        moves.addAll(getPawnMoves(empty, friendlyPawns, opponentPieces, WHITE_PAWN_MASKS, RANK_3, WHITE_PAWN_ATTACK_MASKS));
      }
      case BLACK -> {
        var friendlyPawns = gameState.getBitboards().getBlackPawns();
        var opponentPieces = gameState.getBitboards().getWhitePieces();

        moves.addAll(getPawnMoves(empty, friendlyPawns, opponentPieces, BLACK_PAWN_MASKS, RANK_6, BLACK_PAWN_ATTACK_MASKS));
      }
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    }

    return moves;
  }

  private Moves getPawnMoves(BitBoard empty, BitBoard friendlyPawns, BitBoard opponentPieces, long[] friendlyPawnMasks, long rankMask, long[] friendlyAttackMasks) {
    Moves moves = new Moves();
    for (int pawnPosition : friendlyPawns.getPositions()) {
      var mask = friendlyPawnMasks[pawnPosition];
      var m = empty.and(friendlyPawnMasks[pawnPosition]);

      // not sure which orifice this bit of logic came from, but it seems to work for calculating start moves
      var normalMoves = m.xor(mask).and(rankMask).hasPositions() ? BitBoard.empty() : m;
      var attacks = opponentPieces.and(friendlyAttackMasks[pawnPosition]);

      var targetsBitboard = normalMoves.or(attacks);

      moves.addAll(Moves.from(pawnPosition, targetsBitboard));
    }
    return moves;
  }

  private Moves getKnightMoves(GameState gameState) {
    Moves moves = new Moves();

    BitBoard knights = switch (gameState.getFriendlyColour()) {
      case WHITE -> gameState.getBitboards().getWhiteKnights();
      case BLACK -> gameState.getBitboards().getBlackKnights();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    for (int knightPosition : knights.getPositions()) {
      var targetsBitboard = gameState.getFriendlyBitBoard().not().and(KNIGHT_MASKS[knightPosition]);

      moves.addAll(Moves.from(knightPosition, targetsBitboard));
    }
    return moves;
  }

  private Moves getBishopMoves(GameState gameState) {
    Moves moves = new Moves();

    BitBoard bishops = switch (gameState.getFriendlyColour()) {
      case WHITE -> gameState.getBitboards().getWhiteBishops();
      case BLACK -> gameState.getBitboards().getBlackBishops();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    for (int bishopPosition : bishops.getPositions()) {
      var bishopMask = BISHOP_MASKS[bishopPosition];
      var blockerMask = blockers.and(bishopMask).getBoard();
      var identifier = new Identifier(bishopPosition, blockerMask);
      var targetsBitboard = gameState.getFriendlyBitBoard().not().and(BISHOP_LOOKUP.get(identifier));

      moves.addAll(Moves.from(bishopPosition, targetsBitboard));
    }
    return moves;
  }

  private Moves getRookMoves(GameState gameState) {
    Moves moves = new Moves();

    BitBoard rooks = switch (gameState.getFriendlyColour()) {
      case WHITE -> gameState.getBitboards().getWhiteRooks();
      case BLACK -> gameState.getBitboards().getBlackRooks();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    for (int rookPosition : rooks.getPositions()) {
      var rookMask = ROOK_MASKS[rookPosition];
      var blockerMask = blockers.and(rookMask).getBoard();
      var identifier = new Identifier(rookPosition, blockerMask);
      var targetsBitboard = gameState.getFriendlyBitBoard().not().and(ROOK_LOOKUP.get(identifier));
      moves.addAll(Moves.from(rookPosition, targetsBitboard));
    }
    return moves;
  }

  private Moves getQueenMoves(GameState gameState) {
    Moves moves = new Moves();

    BitBoard queens = switch (gameState.getFriendlyColour()) {
      case WHITE -> gameState.getBitboards().getWhiteQueens();
      case BLACK -> gameState.getBitboards().getBlackQueens();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    for (int queenPosition : queens.getPositions()) {
      var queenMask = QUEEN_MASKS[queenPosition];
      var blockerMask = blockers.and(queenMask).getBoard();
      var identifier = new Identifier(queenPosition, blockerMask);
      var targetsBitboard = gameState.getFriendlyBitBoard().not().and(QUEEN_LOOKUP.get(identifier));

      moves.addAll(Moves.from(queenPosition, targetsBitboard));
    }
    return moves;
  }

  private Moves getKingMoves(GameState gameState) {
    int kingPosition = gameState.getFriendlyKingPosition();
    var targetsBitboard = gameState.getFriendlyBitBoard().not().and(KING_MASKS[kingPosition]);

    return Moves.from(kingPosition, targetsBitboard);
  }
}
