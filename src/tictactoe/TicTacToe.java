package tictactoe;

import javax.swing.*;
import java.awt.*;

public class TicTacToe extends JFrame {
    public TicTacToe() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setTitle("Tic Tac Toe");

        setLayout(new GridLayout(3, 3));
        JButton[][] cells = new JButton[3][3];
        for (int i = 3; i > 0; i--) {
            for (int j = 0; j < 3; j++) {
                String coordinates = String.format("%c%d", (char)('A' + j), i);
                cells[i - 1][j] = new JButton(coordinates);
                cells[i - 1][j].setName("Button" + coordinates);
                add(cells[i - 1][j]);
            }
        }
        setVisible(true);
    }
}