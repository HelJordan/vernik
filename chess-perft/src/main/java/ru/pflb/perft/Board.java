package ru.pflb.perft;

import ru.pflb.perft.exception.NotImplementedException;

import java.util.ArrayList;
import java.util.List;


import static ru.pflb.perft.Color.BLACK;
import static ru.pflb.perft.Color.WHITE;
import static ru.pflb.perft.Piece.*;
import static ru.pflb.perft.Square.*;
import static ru.pflb.perft.Value.val;

/**
 * @author <a href="mailto:8445322@gmail.com">Ivan Bonkin</a>.
 */
public class Board {
    // Для каждой фигуры на доске создадим массив, который хранит сведения о том, как ходит фигура.
    // Деление на белые и черные фигуры в данном случае отсутствует.
    private static final byte[] KING_OFFSETS = {+11, +10, +9, +1, -1, -9, -10, -11};
    private static final byte[] BISHOP_OFFSETS = {+11, +9, -9, -11};
    private static final byte[] ROOK_OFFSETS = {+10, +1, -1, -10};
    private static final byte[] KNIGHT_OFFSETS = {+21, +19, +12, +8, -8, -12, -19, -21};

    /*
    Для каждой фигуры на доске создадим массив, который будет хранить ее местоположение.
    Так, например, массив для позиций короля будет одномерным и иметь размер 2, так как у нас всего два короля.
    Для всех остальных фигур массив будет двумерным потому что бог его знает.
    */
    private byte[] kingPos = {0,0};
    private byte[][] rookPos = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    private byte[][] bishopPos = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    private byte[][] knightPos = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    private byte[][] queenPos = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    // Решение о том, кто ходит будет приниматься на основе переменной sideToMove, которая будет равна
    // либо WHITE, либо BLACK.
    private Color sideToMove;
    /*
    Доска представляет собой одномерный массив из клеток, т.е. вся доска как бы размотана на линии,
    где EMP - означает незанятое поле доски, а OUT - поле за пределами доски.
    Так как фигура сначала будет делать ход, а затем этот ход будет проверяться на валидность,
    то поле OUT поможет определить, что ход невалидный, так как фигура вышла за пределы доски.
    */

    private Piece[] mailbox120 = {
            OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, // 0-9
            OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, // 10-19
            OUT, EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP, OUT, // 20-29
            OUT, EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP, OUT, // 30-39
            OUT, EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP, OUT, // 40-49
            OUT, EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP, OUT, // 50-59
            OUT, EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP, OUT, // 60-69
            OUT, EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP, OUT, // 70-79
            OUT, EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP, OUT, // 80-89
            OUT, EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP, OUT, // 90-99
            OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, // 100-109
            OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT, OUT  // 110-119
    };

    public Board(String fen) {
        /*
        Доска инициализируется с помощью FEN раскладки, в которой в качестве разделителя используется
        пробел.
        Пробел разделяет четыре параметра:
        1. Расположение фигур на доске;
        2. То, чей ход;
        3. Чо-то еще;
        4. И чо-то еще.
         */
        String[] fenParts = fen.split("\\s");
        /*
        Используя второй параметр инициализации доски установим чей ход.
         */
        sideToMove = fenParts[1].startsWith("w") ? WHITE : BLACK;
        /*
        Пробегаемся по FEN-нотации и расставляем по позициям все фигуры.
         */
        for (byte square = 98, fenIndex = 0; fenIndex < fenParts[0].length(); square--, fenIndex++) {
            char c = fenParts[0].charAt(fenIndex);
            switch (c) {
                case 'K':
                    mailbox120[square] = W_KING;
                    kingPos[WHITE.code] = square;
                    break;
                case 'R':
                    mailbox120[square] = W_ROOK;
                    for (int i = 0; i < rookPos[WHITE.code].length; i++) {
                        if (rookPos[WHITE.code][i] == 0) {
                            rookPos[WHITE.code][i] = square;
                            break;
                        }
                    }
                    break;
                case 'B':
                    mailbox120[square] = W_BISHOP;
                    for (int i = 0; i < bishopPos[WHITE.code].length; i++) {
                        if (bishopPos[WHITE.code][i] == 0) {
                            bishopPos[WHITE.code][i] = square;
                            break;
                        }
                    }
                    break;
                case 'Q':
                    mailbox120[square] = W_QUEEN;
                    for (int i = 0; i < queenPos[WHITE.code].length; i++) {
                        if (queenPos[WHITE.code][i] == 0) {
                            queenPos[WHITE.code][i] = square;
                            break;
                        }
                    }
                    break;
                case 'N':
                    mailbox120[square] = W_KNIGHT;
                    for (int i = 0; i < knightPos[WHITE.code].length; i++) {
                        if (knightPos[WHITE.code][i] == 0) {
                            knightPos[WHITE.code][i] = square;
                            break;
                        }
                    }
                    break;
                case 'k':
                    mailbox120[square] = B_KING;
                    kingPos[BLACK.code] = square;
                    break;
                case 'r':
                    mailbox120[square] = B_ROOK;
                    for (int i = 0; i < rookPos[BLACK.code].length; i++) {
                        if (rookPos[BLACK.code][i] == 0) {
                            rookPos[BLACK.code][i] = square;
                            break;
                        }
                    }
                    break;
                case 'b':
                    mailbox120[square] = B_BISHOP;
                    for (int i = 0; i < bishopPos[BLACK.code].length; i++) {
                        if (bishopPos[BLACK.code][i] == 0) {
                            bishopPos[BLACK.code][i] = square;
                            break;
                        }
                    }
                    break;
                case 'q':
                    mailbox120[square] = B_QUEEN;
                    for (int i = 0; i < queenPos[BLACK.code].length; i++) {
                        if (queenPos[BLACK.code][i] == 0) {
                            queenPos[BLACK.code][i] = square;
                            break;
                        }
                    }
                    break;
                case 'n':
                    mailbox120[square] = B_KNIGHT;
                    for (int i = 0; i < knightPos[BLACK.code].length; i++) {
                        if (knightPos[BLACK.code][i] == 0) {
                            knightPos[BLACK.code][i] = square;
                            break;
                        }
                    }
                    break;
                case '/':
                    square -= 1;
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                    /*
                    Пофиксил.
                     */
                    square -= c - '1';
//                    square -= Character.getNumericValue(c);
                    break;
                default:
                    throw new IllegalStateException("Недопустимый символ - " + c);
            }
        }
    }
    /*
    Данный метод генерирует ходы для КОРОЛЯ. На выходе получаем лист из всех ВАЛИДНЫХ ходов КОРОЛЯ.
    Подробнее: мы проходим по КАЖДОМУ возможному ходу короля, совершаем его и смотрим на результат.
    Какие могут быть результаты?
    Мы либо попадаем на пустое поле и спокойно записываем данный ход в коллекцию.
    Либо мы попадаем на клетку за пределами доски, тогда не записываем ход.
    Либо мы попадаем на занятую клетку. Но вопрос вопрос: кем занятую? Если нашей фигурой, тогда просто
    пропускаем данный ход как инвалидный, так как мы не можем встать на место нашей фигуры.
    Однако если мы попадем на вражескую фигуру, то ход валидный и мы опять же записываем его в коллекцию.
     */
    public List<Move> genKingMoves() {
        List<Move> moves = new ArrayList<>();
        for (byte offset : KING_OFFSETS) {
            byte from = kingPos[sideToMove.code];
            byte to = (byte) (from + offset);
            Piece toPiece = mailbox120[to];
            if ((EMP).equals(toPiece)) {
                moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_KING : B_KING));
            } else if ((OUT).equals(toPiece)) {
                continue;
            } else if (toPiece.getColor() != getOpponentColor()) {
                continue;
            } else {
                // взятие
                moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_KING : B_KING, mailbox120[to]));
            }
        }
        return moves;
    }

    public List<Move> genBishopMoves() {
        List<Move> moves = new ArrayList<>();

        // проходим по всем слонам цвета ходящей стороны
        for (int i = 0; i < bishopPos[sideToMove.code].length && bishopPos[sideToMove.code][i] != 0; i++) {
            byte from = bishopPos[sideToMove.code][i];
            // для каждого слона проходим по всем направлениям
            for (byte offset : BISHOP_OFFSETS) {

                byte to = (byte) (from + offset);
                for (;; to+= offset) {
                    if (mailbox120[to] == EMP) {
                        moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_BISHOP : B_BISHOP));
                    } else if (mailbox120[to] == OUT) {
                        break;
                    } else if (mailbox120[to].getColor() == getOpponentColor()) {
                        moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_BISHOP : B_BISHOP, mailbox120[to]));
                    } else {
                        continue;
                    }
                }
//                for (; mailbox120[to] == EMP; to += offset) {
//                    // генерируем все ходы по пустым клеткам
//                    moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_BISHOP : B_BISHOP));
//                }
//                // генерируем взятие, если наткнулись на чужую фигуру
//                if (mailbox120[to].getColor() == getOpponentColor()) {
//                    moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_BISHOP : B_BISHOP, mailbox120[to]));
//                }
            }
        }

        return moves;
    }

    public List<Move> genRookMoves() {
        List<Move> moves = new ArrayList<>();

        // проходим по всем слонам цвета ходящей стороны
        for (int i = 0; i < rookPos[sideToMove.code].length && rookPos[sideToMove.code][i] != 0; i++) {
            byte from = rookPos[sideToMove.code][i];
            // для каждого слона проходим по всем направлениям
            for (byte offset : ROOK_OFFSETS) {

                byte to = (byte) (from + offset);
                for (;; to += offset) {
                    if (mailbox120[to] == EMP) {
                        moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_ROOK : B_ROOK));
                    } else if (mailbox120[to] == OUT) {
                        break;
                    } else if (mailbox120[to].getColor() == getOpponentColor()) {
                        moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_ROOK : B_ROOK, mailbox120[to]));
                    } else {
                        continue;
                    }
                }
//                for (; mailbox120[to] == EMP ; to += offset) {
//                    // генерируем все ходы по пустым клеткам
//                    moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_ROOK : B_ROOK));
//                }
//                // генерируем взятие, если наткнулись на чужую фигуру
//                if (mailbox120[to].getColor() == getOpponentColor()) {
//                    moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_ROOK : B_ROOK, mailbox120[to]));
//                }
            }
        }

        return moves;
    }

    public List<Move> genQueenMoves() {
        List<Move> moves = new ArrayList<>();

        // проходим по всем ферзям цвета ходящей стороны
        for (int i = 0; i < queenPos[sideToMove.code].length && queenPos[sideToMove.code][i] != 0; i++) {
            byte from = queenPos[sideToMove.code][i];
            // для каждого ферзя проходим по всем направлениям
            for (byte offset : KING_OFFSETS) {

                byte to = (byte) (from + offset);
                for (; mailbox120[to] == EMP ; to += offset) {
                    // генерируем все ходы по пустым клеткам
                    moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_QUEEN : B_QUEEN));
                }
                // генерируем взятие, если наткнулись на чужую фигуру
                if (mailbox120[to].getColor() == getOpponentColor()) {
                    moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_QUEEN : B_QUEEN, mailbox120[to]));
                }
            }
        }

        return moves;
    }

    public List<Move> genKnightMoves() {
        List<Move> moves = new ArrayList<>();

        // проходим по всем коням цвета ходящей стороны
        for (int i = 0; i < knightPos[sideToMove.code].length && knightPos[sideToMove.code][i] != 0; i++) {
            byte from = knightPos[sideToMove.code][i];

            for (byte offset : KNIGHT_OFFSETS) {
                byte to = (byte) (from + offset);
                Piece toPiece = mailbox120[to];
                if ((EMP).equals(toPiece)) {
                    moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_KNIGHT : B_KNIGHT));
                } else if ((OUT).equals(toPiece)) {
                    continue;
                } else if (toPiece.getColor() != getOpponentColor()) {
                    continue;
                } else {
                    // взятие
                    moves.add(new Move(new Square(from), new Square(to), sideToMove == WHITE ? W_KNIGHT : B_KNIGHT, mailbox120[to]));
                }
            }
        }
        return moves;
    }

    public List<Move> genAllMoves() {
        List<Move> moves = new ArrayList<>();
        moves.addAll(genKingMoves());
        moves.addAll(genBishopMoves());
        moves.addAll(genRookMoves());
        moves.addAll(genQueenMoves());
        moves.addAll(genKnightMoves());
        return moves;
    }

    /**
     * @param kingColor цвет короля, которому детектируется шах
     */
    public boolean isCheck(Color kingColor) {
        List<Move> moves = genAllMoves();
        for (Move move : moves)
            if (move.isCapture() && (move.capture.get() == (kingColor == WHITE ? W_KING : B_KING))) {
                this.sideToMove = this.sideToMove == WHITE ? BLACK : WHITE;
                return true;
            }
        return false;
    }

    private void makeNonKingMove(Move move, byte[][] piecePos, Color color) {
        for (int i = 0; i < piecePos[color.code].length; i++) {
            if (piecePos[color.code][i] == val(move.from)) {
                piecePos[color.code][i] = val(move.to);
                break;
            }
        }
    }

    private void makeCapture(Move move, byte[][] piecePos, Color color) {
        for (int i = 0; i < piecePos[color.code].length; i++) {
            if (piecePos[color.code][i] == val(move.to)) {
//                piecePos[color.code][i] = 0;
                // сдвигаем все остальные значения на единицу, заполняя выбывшую фигуру, пока не встретим 0
                // таким образом массив будет всегда содержать нулевые значения в конце
                for (int j = i + 1; j < piecePos[color.code].length && piecePos[color.code][j] != 0; j++) {
                    piecePos[color.code][j - 1] = piecePos[color.code][j];
                }
                break;
            }
        }
    }

    private void takeBackCapture(Move move, byte[][] piecePos, Color color) {
        for (int i = 0; i < piecePos[color.code].length; i++) {
            if (piecePos[color.code][i] == 0) {
                // выставляем вернувшуюся фигуру в первое ненулевое окно
                piecePos[color.code][i] = val(move.to);
                break;
            }
        }
    }

    public void makeMove(Move move) {
        mailbox120[val(move.from)] = EMP;
        mailbox120[val(move.to)] = move.piece;

        switch (move.piece) {
            case W_KING:
                kingPos[WHITE.code] = val(move.to);
                break;
            case B_KING:
                kingPos[BLACK.code] = val(move.to);
                break;
            case W_BISHOP:
                makeNonKingMove(move, bishopPos, WHITE);
                break;
            case W_ROOK:
                makeNonKingMove(move, rookPos, WHITE);
                break;
            case W_QUEEN:
                makeNonKingMove(move, queenPos, WHITE);
                break;
            case W_KNIGHT:
                makeNonKingMove(move, knightPos, WHITE);
                break;
            case B_BISHOP:
                makeNonKingMove(move, bishopPos, BLACK);
                break;
            case B_ROOK:
                makeNonKingMove(move, rookPos, BLACK);
                break;
            case B_QUEEN:
                makeNonKingMove(move, queenPos, BLACK);
                break;
            case B_KNIGHT:
                makeNonKingMove(move, knightPos, BLACK);
                break;
            default:
                throw new IllegalStateException("Неправильное состояние");
        }

        if (move.isCapture()) {
            switch (move.capture.get()) {
                case W_BISHOP:
                    makeCapture(move, bishopPos, WHITE);
                    break;
                case W_ROOK:
                    makeCapture(move, rookPos, WHITE);
                    break;
                case W_QUEEN:
                    makeCapture(move, queenPos, WHITE);
                    break;
                case W_KNIGHT:
                    makeCapture(move, knightPos, WHITE);
                    break;
                case B_BISHOP:
                    makeCapture(move, bishopPos, BLACK);
                    break;
                case B_ROOK:
                    makeCapture(move, rookPos, BLACK);
                    break;
                case B_QUEEN:
                    makeCapture(move, queenPos, BLACK);
                    break;
                case B_KNIGHT:
                    makeCapture(move, knightPos, BLACK);
                    break;
                case OUT:
                    throw new IllegalStateException("Ты зачем за край доски вышел, дурень");
                case EMP:
                    throw new IllegalStateException("Ты зачем пустую клетку сожрал, дурень");
                case W_KING:
                    throw new IllegalStateException("Ну класс, на короля позарился. Да еще и белого");
                case B_KING:
                    throw new IllegalStateException("Ну класс, теперь на нигера полез.");
                default:
                    System.out.println(this);
                    throw new IllegalStateException("Неправильное взятие, " + move.capture.get());
            }
        }

        // обновляем очередь хода
        this.sideToMove = this.sideToMove == WHITE ? BLACK : WHITE;
    }

    public void takeBack(Move move) {
        mailbox120[val(move.from)] = move.piece;

        byte[] piecePos;
        switch (move.piece) {
            case W_KING:
            case B_KING:
                piecePos = kingPos;
                break;
            case W_BISHOP:
                piecePos = bishopPos[WHITE.code];
                break;
            case W_ROOK:
                piecePos = rookPos[WHITE.code];
                break;
            case W_QUEEN:
                piecePos = queenPos[WHITE.code];
                break;
            case W_KNIGHT:
                piecePos = knightPos[WHITE.code];
                break;
            case B_BISHOP:
                piecePos = bishopPos[BLACK.code];
                break;
            case B_ROOK:
                piecePos = rookPos[BLACK.code];
                break;
            case B_QUEEN:
                piecePos = queenPos[BLACK.code];
                break;
            case B_KNIGHT:
                piecePos = knightPos[BLACK.code];
                break;
            default:
                throw new IllegalStateException("Неправильное состояние");
        }
        for(int i = 0; i < piecePos.length;i++){
            if(piecePos[i] == val(move.to)){
                piecePos[i] = val(move.from);
            }
        }


        if (move.isCapture()) {
            mailbox120[val(move.to)] = move.capture.get();

            switch (move.capture.get()) {
                case W_BISHOP:
                    takeBackCapture(move, bishopPos, WHITE);
                    break;
                case W_ROOK:
                    takeBackCapture(move, rookPos, WHITE);
                    break;
                case W_QUEEN:
                    takeBackCapture(move, queenPos, WHITE);
                    break;
                case W_KNIGHT:
                    takeBackCapture(move, knightPos, WHITE);
                    break;
                case B_BISHOP:
                    takeBackCapture(move, bishopPos, BLACK);
                    break;
                case B_ROOK:
                    takeBackCapture(move, rookPos, BLACK);
                    break;
                case B_QUEEN:
                    takeBackCapture(move, queenPos, BLACK);
                    break;
                case B_KNIGHT:
                    takeBackCapture(move, knightPos, BLACK);
                    break;
                default:
                    throw new IllegalStateException("Неправильное взятие, " + move.capture.get());
            }

        } else {
            mailbox120[val(move.to)] = EMP;
        }
        // возвращаем очередь хода
        this.sideToMove = this.sideToMove == WHITE ? BLACK : WHITE;
    }


    public Color getOpponentColor() {
        return sideToMove == WHITE ? BLACK : WHITE;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        for (int s = A8.value; s >= H8.value; s--) {
            sb.append(mailbox120[s]).append(" ");
        }
        sb.append("\n");
        for (int s = A7.value; s >= H7.value; s--) {
            sb.append(mailbox120[s]).append(" ");
        }
        sb.append("\n");
        for (int s = A6.value; s >= H6.value; s--) {
            sb.append(mailbox120[s]).append(" ");
        }
        sb.append("\n");
        for (int s = A5.value; s >= H5.value; s--) {
            sb.append(mailbox120[s]).append(" ");
        }
        sb.append("\n");
        for (int s = A4.value; s >= H4.value; s--) {
            sb.append(mailbox120[s]).append(" ");
        }
        sb.append("\n");
        for (int s = A3.value; s >= H3.value; s--) {
            sb.append(mailbox120[s]).append(" ");
        }
        sb.append("\n");
        for (int s = A2.value; s >= H2.value; s--) {
            sb.append(mailbox120[s]).append(" ");
        }
        sb.append("\n");
        for (int s = A1.value; s >= H1.value; s--) {
            sb.append(mailbox120[s]).append(" ");
        }

        return sb.toString();
    }
}
