package com.tom.chess.movegen;

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
    moves.addAll(getRookMoves(gameState));
    return moves;
  }

  private BitBoard generateTaboo(GameState gameState) {
    return null;
  }

  private List<Move> getRookMoves(GameState gameState) {
    List<Move> moves = new ArrayList<>();

    BitBoard rooks = switch(gameState.getFriendlyColour()) {
      case WHITE -> gameState.getBitboards().getWhiteRooks();
      case BLACK -> gameState.getBitboards().getBlackRooks();
      default -> throw new IllegalStateException("Unexpected value: %S".formatted(gameState.getFriendlyColour()));
    };

    BitBoard blockers = gameState.getBitboards().getEmpty().not();

    System.out.println("ROOKS");
    rooks.print();

    for (int rookPosition : rooks.getPositions()) {
      var rookMask = ROOK_MASKS[rookPosition];
      var blockerMask = blockers.and(rookMask).getBoard();

      var identifier = new Identifier(rookPosition, blockerMask);

      var validMoves = gameState.getFriendlyBitBoard().not().and(ROOK_LOOKUP.get(identifier));

      System.out.println("Valid moves");
      validMoves.print();

      var targetPositions = validMoves.getPositions();

      for (var targetPosition : targetPositions) {
        moves.add(new Move(rookPosition, targetPosition));
      }
    }
    return moves;
  }
}
