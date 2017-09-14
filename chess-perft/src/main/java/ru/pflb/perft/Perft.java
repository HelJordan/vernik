package ru.pflb.perft;

import java.util.List;

/**
 * @author <a href="mailto:8445322@gmail.com">Ivan Bonkin</a>.
 */
public class Perft {

    public static int calculate(Board board, int depth) {

        if (depth == 0) {
            return 1;
        }

        int positions = 0;
        List<Move> moves = board.genAllMoves();
        for (Move move : moves) {
            board.makeMove(move);

            if (board.isCheck(board.getOpponentColor())) {
                board.takeBack(move);
                continue;
            }

            positions += calculate(board, depth - 1);

            board.takeBack(move);
        }

        return positions;
    }
}
