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

  public Moves generateMoves(GameState gameState) {
    //    var startTime = System.nanoTime();
    gameState = threatMapGenerator.generateThreatMapAndChecks(gameState);

//    gameState.getThreatMap().print();
//    gameState.getPinMask().print();

    var moves = getMoves(gameState);

    //    var endTime = System.nanoTime();
    //    long startupTime = endTime - startTime;
    //
    //    System.out.println("Moves generated in " + startupTime + "nanoseconds.");
    return moves;
  }

  private Moves getMoves(GameState gameState) {
    Moves moves = Moves.empty();
    moves.addAll(getPawnMoves(gameState));
    moves.addAll(getKnightMoves(gameState));
    moves.addAll(getBishopMoves(gameState));
    moves.addAll(getRookMoves(gameState));
    moves.addAll(getQueenMoves(gameState));
    moves.addAll(getKingMoves(gameState));
    moves.addAll(getEnPassantMoves(gameState));
    return moves;
  }

  private Moves getPawnMoves(GameState gameState) {
    var moves = Moves.empty();
    var empty = gameState.getBitboards().getEmpty();
    BitBoard opponentPieces;
    BitBoard friendlyPawns;
    long[] friendlyPawnMasks;
    long[] friendlyAttackMasks;
    long rankMask;

    switch (gameState.getFriendlyColour()) {
      case WHITE -> {
        friendlyPawns = gameState.getBitboards().getWhitePawns();
        opponentPieces = gameState.getBitboards().getBlackPieces();
        friendlyPawnMasks = WHITE_PAWN_MASKS;
        rankMask = RANK_3;
        friendlyAttackMasks = WHITE_PAWN_ATTACK_MASKS;
      }
      case BLACK -> {
        friendlyPawns = gameState.getBitboards().getBlackPawns();
        opponentPieces = gameState.getBitboards().getWhitePieces();
        friendlyPawnMasks = BLACK_PAWN_MASKS;
        rankMask = RANK_6;
        friendlyAttackMasks = BLACK_PAWN_ATTACK_MASKS;
      }
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    }

    for (int position : friendlyPawns.getPositions()) {
      var mask = friendlyPawnMasks[position];
      var m = empty.and(friendlyPawnMasks[position]);

      // not sure which orifice this bit of logic came from, but it seems to work for calculating start moves
      var normalMoves = m.xor(mask).and(rankMask).hasPositions() ? BitBoard.empty() : m;
      var attacks = opponentPieces.and(friendlyAttackMasks[position]);

      var targetsBitboard = normalMoves.or(attacks);
      targetsBitboard = targetsBitboard.and(gameState.getPinMask());

      moves.addAll(Moves.from(position, targetsBitboard));
    }

    return moves;
  }

  private Moves getKnightMoves(GameState gameState) {
    Moves moves = Moves.empty();

    BitBoard knights = switch (gameState.getFriendlyColour()) {
      case WHITE -> gameState.getBitboards().getWhiteKnights();
      case BLACK -> gameState.getBitboards().getBlackKnights();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    for (int position : knights.getPositions()) {
      var targetsBitboard = gameState.getFriendlyBitBoard().not().and(KNIGHT_MASKS[position]);
      targetsBitboard = targetsBitboard.and(gameState.getPinMask());
      moves.addAll(Moves.from(position, targetsBitboard));
    }
    return moves;
  }

  private Moves getBishopMoves(GameState gameState) {
    Moves moves = Moves.empty();

    BitBoard bishops = switch (gameState.getFriendlyColour()) {
      case WHITE -> gameState.getBitboards().getWhiteBishops();
      case BLACK -> gameState.getBitboards().getBlackBishops();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    for (int position : bishops.getPositions()) {
      var bishopMask = BISHOP_MASKS[position];
      var blockerMask = blockers.and(bishopMask).getBoard();
      var identifier = new Identifier(position, blockerMask);

      var targetsBitboard = gameState.getFriendlyBitBoard().not().and(BISHOP_LOOKUP.get(identifier));
      targetsBitboard = targetsBitboard.and(gameState.getPinMask());
      moves.addAll(Moves.from(position, targetsBitboard));
    }
    return moves;
  }

  private Moves getRookMoves(GameState gameState) {
    Moves moves = Moves.empty();

    BitBoard rooks = switch (gameState.getFriendlyColour()) {
      case WHITE -> gameState.getBitboards().getWhiteRooks();
      case BLACK -> gameState.getBitboards().getBlackRooks();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    for (int position : rooks.getPositions()) {
      var rookMask = ROOK_MASKS[position];
      var blockerMask = blockers.and(rookMask).getBoard();
      var identifier = new Identifier(position, blockerMask);

      var targetsBitboard = gameState.getFriendlyBitBoard().not().and(ROOK_LOOKUP.get(identifier));
      targetsBitboard = targetsBitboard.and(gameState.getPinMask());
      moves.addAll(Moves.from(position, targetsBitboard));
    }
    return moves;
  }

  private Moves getQueenMoves(GameState gameState) {
    Moves moves = Moves.empty();

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
      targetsBitboard = targetsBitboard.and(gameState.getPinMask());
      moves.addAll(Moves.from(queenPosition, targetsBitboard));
    }
    return moves;
  }

  private Moves getKingMoves(GameState gameState) {
    int kingPosition = gameState.getFriendlyKingPosition();
    var threatMap = gameState.getThreatMap();
    var targetsBitboard = gameState.getFriendlyBitBoard().not().and(KING_MASKS[kingPosition]);

    // mask out threat map
    var legalTargets = threatMap.not().and(targetsBitboard);
    legalTargets = legalTargets.and(gameState.getPinMask());

    return Moves.from(kingPosition, legalTargets);
  }

  private Moves getEnPassantMoves(GameState gameState) {
    if (!gameState.getFen().getEnPassantTarget().equals("-")) {
      var enPassantTarget = BitBoard.fromPosition(gameState.getFen().getEnPassantTarget());
      var direction = gameState.getFriendlyColour() == WHITE ? -1 : 1;
      var friendlyPawns = gameState.getFriendlyColour() == WHITE ? gameState.getBitboards().getWhitePawns() : gameState.getBitboards().getBlackPawns();
      var possibleStartPositions = enPassantTarget.leftShift(direction * 7).or(enPassantTarget.leftShift(direction * 9));
      var actualStartPositions = possibleStartPositions.and(friendlyPawns);

      if (actualStartPositions.isNotEmpty()) {
        return Moves.from(actualStartPositions, enPassantTarget);
      }
    }
    return Moves.empty();
  }
}
