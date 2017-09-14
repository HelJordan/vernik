package ru.pflb.perft;

/**
 * @author <a href="mailto:8445322@gmail.com">Ivan Bonkin</a>.
 */
public class Square {

    public static final Square
            A8 = new Square(98), B8 = new Square(97), C8 = new Square(96), D8 = new Square(95), E8 = new Square(94), F8 = new Square(93), G8 = new Square(92), H8 = new Square(91),
            A7 = new Square(88), B7 = new Square(87), C7 = new Square(86), D7 = new Square(85), E7 = new Square(84), F7 = new Square(83), G7 = new Square(82), H7 = new Square(81),
            A6 = new Square(78), B6 = new Square(77), C6 = new Square(76), D6 = new Square(75), E6 = new Square(74), F6 = new Square(73), G6 = new Square(72), H6 = new Square(71),
            A5 = new Square(68), B5 = new Square(67), C5 = new Square(66), D5 = new Square(65), E5 = new Square(64), F5 = new Square(63), G5 = new Square(62), H5 = new Square(61),
            A4 = new Square(58), B4 = new Square(57), C4 = new Square(56), D4 = new Square(55), E4 = new Square(54), F4 = new Square(53), G4 = new Square(52), H4 = new Square(51),
            A3 = new Square(48), B3 = new Square(47), C3 = new Square(46), D3 = new Square(45), E3 = new Square(44), F3 = new Square(43), G3 = new Square(42), H3 = new Square(41),
            A2 = new Square(38), B2 = new Square(37), C2 = new Square(36), D2 = new Square(35), E2 = new Square(34), F2 = new Square(33), G2 = new Square(32), H2 = new Square(31),
            A1 = new Square(28), B1 = new Square(27), C1 = new Square(26), D1 = new Square(25), E1 = new Square(24), F1 = new Square(23), G1 = new Square(22), H1 = new Square(21);


    public final byte value;

    public Square(int value) {
        this.value = (byte) value;
    }

    @Override
    public String toString() {
        char
                // вертикаль
                file = 'H',
                // горизонталь
                rank = '1';

        file -= (value - 21) % 10;
        rank += (value - 21) / 10;
        return "" + file + rank;
    }

    public byte getValue() {
        return value;
    }
}
