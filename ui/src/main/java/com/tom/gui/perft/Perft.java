package com.tom.gui.perft;

import com.tom.chess.model.Fen;
import com.tom.chess.model.Move;
import com.tom.chess.model.Moves;
import com.tom.middleware.Mediator;
import com.tom.middleware.StandardMediator;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

public class Perft {
    private static Mediator mediator;

    private static List<String> output;

    public static List<String> runPerformanceTest(Fen fen, int depth, PerftGuiController controller) {
        output = new ArrayList<>();
        mediator = new StandardMediator(fen);
        int totalPermutations = search(depth, depth, Move.empty(), controller);

        String nodesSearched = String.format("Nodes searched: %s", totalPermutations);
        EventQueue.invokeLater(() -> controller.addStringToPerftPane("\n"));

        output(nodesSearched, controller);
        return output;
    }

    public static int search(int depth, int targetDepth, Move previousMove, PerftGuiController controller) {
        Moves moves = mediator.getMoves();
        if (depth == 1) {
            int possibleMoves = moves.size();
            if (targetDepth == 2) {
                String moveString = String.format("%s: %s", previousMove, possibleMoves);
                output(moveString, controller);
                return possibleMoves;
            }
            if (targetDepth == 1) {
                for (Move move : moves) {
                    String moveString = String.format("%s: %s", move, 1);
                    output(moveString, controller);
                }
            }
            return possibleMoves;
        }

        int possibleMoves = 0;
        for (Move move : moves) {
            mediator.makeMove(move);
            possibleMoves += search(depth - 1, targetDepth, move, controller);
            mediator.undoMove();
        }
        if (depth == targetDepth - 1) {
            String moveString = String.format("%s: %s", previousMove, possibleMoves);
            output(moveString, controller);
        }

        return possibleMoves;
    }

    private static void output(String str, PerftGuiController controller) {
        EventQueue.invokeLater(() -> controller.addStringToPerftPane(str +"\n"));
        output.add(str);
    }
}
