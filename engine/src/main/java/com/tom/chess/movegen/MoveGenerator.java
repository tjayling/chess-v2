package com.tom.chess.movegen;

import static com.tom.chess.movegen.precomputed.BishopDataGenerator.BISHOP_LOOKUP;
import static com.tom.chess.movegen.precomputed.BishopDataGenerator.BISHOP_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.BLACK_PAWN_ATTACK_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.BLACK_PAWN_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.WHITE_PAWN_ATTACK_MASKS;
import static com.tom.chess.movegen.precomputed.PawnDataGenerator.WHITE_PAWN_MASKS;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_3;
import static com.tom.chess.movegen.precomputed.PrecomputedMoveData.RANK_6;
import static com.tom.chess.movegen.precomputed.RookDataGenerator.ROOK_LOOKUP;
import static com.tom.chess.movegen.precomputed.RookDataGenerator.ROOK_MASKS;
import static com.tom.chess.piece.PieceConstants.BLACK;
import static com.tom.chess.piece.PieceConstants.WHITE;

import com.tom.chess.model.BitBoard;
import com.tom.chess.model.GameState;
import com.tom.chess.model.Move;
import com.tom.chess.model.Moves;
import com.tom.chess.movegen.precomputed.Identifier;
import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

  public Moves generateMoves(final GameState gameState) {
    var taboo = generateTaboo(gameState);
    Moves moves = new Moves();
    moves.addAll(getPawnMoves(gameState));
    moves.addAll(getBishopMoves(gameState));
    moves.addAll(getRookMoves(gameState));
    return moves;
  }

  private BitBoard generateTaboo(GameState gameState) {
    return null;
  }

//  @SuppressWarnings("DuplicatedCode")
  private List<Move> getPawnMoves(GameState gameState) {
    List<Move> moves = new ArrayList<>();
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

  private List<Move> getPawnMoves(BitBoard empty, BitBoard friendlyPawns, BitBoard opponentPieces, long[] whitePawnMasks, long rank3, long[] whitePawnAttackMasks) {
    List<Move> moves = new ArrayList<>();
    for (int pawnPosition : friendlyPawns.getPositions()) {
      var mask = whitePawnMasks[pawnPosition];
      var m = empty.and(whitePawnMasks[pawnPosition]);

      // not sure which orifice this bit of logic came from, but it seems to work for filtering start moves
      var normalMoves = m.xor(mask).and(rank3).hasPositions() ? BitBoard.empty() : m;
      var attacks = opponentPieces.and(whitePawnAttackMasks[pawnPosition]);

      var targetPositions = normalMoves.or(attacks).getPositions();

      for (var targetPosition : targetPositions) {
        moves.add(new Move(pawnPosition, targetPosition));
      }
    }
    return moves;
  }

  private List<Move> getBishopMoves(GameState gameState) {
    List<Move> moves = new ArrayList<>();

    BitBoard bishops = switch (gameState.getFriendlyColour()) {
      case WHITE -> gameState.getBitboards().getWhiteBishops();
      case BLACK -> gameState.getBitboards().getBlackBishops();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    System.out.println("BISHOPS");
    bishops.print();

    for (int bishopPosition : bishops.getPositions()) {
      var bishopMask = BISHOP_MASKS[bishopPosition];
      var blockerMask = blockers.and(bishopMask).getBoard();

      var identifier = new Identifier(bishopPosition, blockerMask);

      var validMoves = gameState.getFriendlyBitBoard().not().and(BISHOP_LOOKUP.get(identifier));

      System.out.println("Valid Moves");
      validMoves.print();
      System.out.println("Bishop Mask");
      new BitBoard(bishopMask).print();

      var targetPositions = validMoves.getPositions();

      for (var targetPosition : targetPositions) {
        moves.add(new Move(bishopPosition, targetPosition));
      }
    }
    return moves;
  }

  private List<Move> getRookMoves(GameState gameState) {
    List<Move> moves = new ArrayList<>();

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

      var validMoves = gameState.getFriendlyBitBoard().not().and(ROOK_LOOKUP.get(identifier));

      var targetPositions = validMoves.getPositions();

      for (var targetPosition : targetPositions) {
        moves.add(new Move(rookPosition, targetPosition));
      }
    }
    return moves;
  }
}
