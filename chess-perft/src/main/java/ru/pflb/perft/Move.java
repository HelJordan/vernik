package ru.pflb.perft;

import java.util.Optional;

/**
 * @author <a href="mailto:8445322@gmail.com">Ivan Bonkin</a>.
 */
public class Move {

    public final Square from, to;

    public final Piece piece;

    public final Optional<Piece> capture;

    public Move(Square from, Square to, Piece piece) {
        this(from, to, piece, null);
    }

    public Move(Square from, Square to, Piece piece, Piece capture) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.capture = Optional.ofNullable(capture);
    }

    public boolean isCapture() {
        return capture.map(p -> true).orElse(false);
    }

    /**
     * @return <a href="полная запись ходов">https://ru.wikipedia.org/wiki/%D0%A8%D0%B0%D1%85%D0%BC%D0%B0%D1%82%D0%BD%D0%B0%D1%8F_%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F</a>
     */
    @Override
    public String toString() {
        return piece.toString() + " " + from + "-" + to;
    }
}
