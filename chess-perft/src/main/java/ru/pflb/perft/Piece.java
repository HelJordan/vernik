package ru.pflb.perft;

import static ru.pflb.perft.Color.BLACK;
import static ru.pflb.perft.Color.WHITE;

/**
 * @author <a href="mailto:8445322@gmail.com">Ivan Bonkin</a>.
 */
public enum Piece {
    W_KING((byte)0, WHITE),
    B_KING((byte)0, BLACK),
    W_BISHOP((byte)1, WHITE),
    B_BISHOP((byte)1, BLACK),
    W_ROOK((byte)2, WHITE),
    B_ROOK((byte)2, BLACK),
    W_QUEEN((byte)3, WHITE),
    B_QUEEN((byte)3, BLACK),
    W_KNIGHT((byte)4, WHITE),
    B_KNIGHT((byte)4, BLACK),
    OUT((byte)-1),
    EMP((byte)0);

    public final byte code;

    Piece(byte pieceType, Color color) {
        this((byte)(color.code * 6 + pieceType + 1));
    }

    Piece(byte code) {
        this.code = code;
    }

    public Color getColor() {
        return code <= 6 ? WHITE : BLACK;
    }

    @Override
    public String toString() {
        return this == OUT ? " " : this == EMP ? "." : this.name().charAt(0) == 'W' ? String.valueOf(this.name().charAt(2)) : String.valueOf(this.name().charAt(2)).toLowerCase();
    }

}
