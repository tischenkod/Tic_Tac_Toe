package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TicTacToe extends JFrame  implements ActionListener {
    private GameState gameState;
    private Player currentPlayer;
    private final GameButton[][] cells;
    private final Logger logger;
    JLabel gameStateLabel;

    public TicTacToe() {
        currentPlayer = Player.X;
        gameState = GameState.NOT_STARTED;
        logger = Logger.getLogger(getClass().getName());
        logger.setFilter(record -> false);
        try {
            Handler handler = new FileHandler("TicTacToe.log");
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 380);
        setTitle("Tic Tac Toe");
        setLayout(new BorderLayout());

        JPanel cellsPanel = new JPanel();

        cellsPanel.setLayout(new GridLayout(3, 3));
        cellsPanel.setPreferredSize(new Dimension(300, 300));
        cells = new GameButton[3][3];
        for (int i = 3; i > 0; i--) {
            for (int j = 0; j < 3; j++) {
                String coordinates = String.format("%c%d", (char)('A' + j), i);
                cells[i - 1][j] = new GameButton(coordinates);
                cells[i - 1][j].setName("Button" + coordinates);
                cells[i - 1][j].addActionListener(this);
                cells[i - 1][j].setFocusPainted(false);
                cellsPanel.add(cells[i - 1][j]);
            }
        }
        add(cellsPanel, BorderLayout.NORTH);

        JPanel statusBar = new JPanel();
        statusBar.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 15));
        statusBar.setPreferredSize(new Dimension(300, 50));

        gameStateLabel = new JLabel(gameState.toString());
        gameStateLabel.setName("LabelStatus");
        gameStateLabel.setPreferredSize(new Dimension(170, 20));
        statusBar.add(gameStateLabel);

        JButton resetButton = new JButton("Reset");
        resetButton.setName("ButtonReset");
        resetButton.addActionListener(this::reset);
        statusBar.add(resetButton);

        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
        logger.info("Game started!");
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        gameStateLabel.setText(gameState.toString());
        logger.info("Setting state: " + gameState);
    }

    void reset(ActionEvent e) {
        logger.info("Resetting game");
        setGameState(GameState.NOT_STARTED);
        currentPlayer = Player.X;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setState(CellState.CLEAR);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameButton sender = (GameButton) e.getSource();
        if ((gameState.equals(GameState.IN_PROGRESS) || gameState.equals(GameState.NOT_STARTED)) &&
                sender.getState().equals(CellState.CLEAR)) {

            sender.setState(currentPlayer.equals(Player.X) ? CellState.FILLED_X : CellState.FILLED_O);

            Boolean[] stateColumn = new Boolean[3];
            Arrays.fill(stateColumn, true);
            Boolean[] stateRow = new Boolean[3];
            Arrays.fill(stateRow, true);
            boolean ascendingDiagonal = true;
            boolean descendingDiagonal = true;

            int clearCount = 0;
            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 3; column++) {
                    if (cells[row][column].getState().equals(CellState.CLEAR)) {
                        clearCount++;
                    }
                    boolean equals = cells[row][column].getState().equals(currentPlayer.toCellState());
                    stateColumn[row] &= equals;
                    stateRow[column] &= equals;
                    if (row == column) {
                        ascendingDiagonal &= equals;
                    }
                    if (row == 2 - column) {
                        descendingDiagonal &= equals;
                    }
                }
            }
            if (ascendingDiagonal ||
                    descendingDiagonal ||
                    Arrays.stream(stateColumn).anyMatch(b -> b) ||
                    Arrays.stream(stateRow).anyMatch(b -> b)) {
                setGameState(currentPlayer.equals(Player.X) ? GameState.X_WINS : GameState.O_WINS);
            } else if (clearCount == 0) {
                setGameState(GameState.DRAW);
            } else {
                setGameState(GameState.IN_PROGRESS);
            }

            currentPlayer = currentPlayer.next();
        }
    }
}