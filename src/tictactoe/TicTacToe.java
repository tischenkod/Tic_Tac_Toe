package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.logging.Logger;

public class TicTacToe extends JFrame implements ActionListener{
    private GameState gameState;
    private Player currentPlayer;
    private GameMode player1Mode;
    private GameMode player2Mode;
    private final GameButton[][] cells;
    private final Logger logger;
    private final JButton player1ModeBtn;
    private final JButton resetButton;
    private final JButton player2ModeBtn;
    JPanel cellsPanel;
    private final JLabel gameStateLabel;
    private Robot robot;

    public TicTacToe() {
        currentPlayer = Player.X;
        gameState = GameState.NOT_STARTED;
        player1Mode = GameMode.HUMAN;
        player2Mode = GameMode.HUMAN;

        logger = Logger.getLogger(getClass().getName());
        logger.setFilter(record -> false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 380);
        setTitle("Tic Tac Toe");
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 3));
        controlPanel.setPreferredSize(new Dimension(300, 30));

        player1ModeBtn = new JButton("Human");
        player1ModeBtn.setName("ButtonPlayer1");
        player1ModeBtn.addActionListener(this::switchMode);
        controlPanel.add(player1ModeBtn);

        resetButton = new JButton("Start");
        resetButton.setName("ButtonStartReset");
        resetButton.addActionListener(this::reset);
        controlPanel.add(resetButton);

        player2ModeBtn = new JButton("Human");
        player2ModeBtn.setName("ButtonPlayer2");
        player2ModeBtn.addActionListener(this::switchMode);
        controlPanel.add(player2ModeBtn);
        add(controlPanel, BorderLayout.NORTH);

        cellsPanel = new JPanel();
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
        setCellsEnabled(false);
        add(cellsPanel, BorderLayout.CENTER);

        JPanel statusBar = new JPanel();
        statusBar.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 15));
        statusBar.setPreferredSize(new Dimension(300, 50));

        gameStateLabel = new JLabel(getGameState().toString());
        gameStateLabel.setName("LabelStatus");
        gameStateLabel.setPreferredSize(new Dimension(170, 20));
        statusBar.add(gameStateLabel);

        add(statusBar, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);

        robot = new RandomRobot(this);
        robot.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                robot.interrupt();
            }
        });
        logger.info("Game started!");

    }

    public synchronized GameMode getPlayer1Mode() {
        return player1Mode;
    }

    public synchronized GameMode getPlayer2Mode() {
        return player2Mode;
    }

    public synchronized Player getCurrentPlayer() {
        return currentPlayer;
    }

    public synchronized void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPlayer1Mode(GameMode player1Mode) {
        this.player1Mode = player1Mode;
        player1ModeBtn.setText(player1Mode.toString());
    }

    public void setPlayer2Mode(GameMode player2Mode) {
        this.player2Mode = player2Mode;
        player2ModeBtn.setText(player2Mode.toString());
    }

    private void switchMode(ActionEvent actionEvent) {
        if (getGameState() == GameState.IN_PROGRESS) {
            return;
        }
        JButton sender = (JButton) actionEvent.getSource();
        if (sender.getName().equals("ButtonPlayer1")) {
            setPlayer1Mode(player1Mode == GameMode.HUMAN ? GameMode.ROBOT : GameMode.HUMAN);
            return;
        }
        if (sender.getName().equals("ButtonPlayer2")) {
            setPlayer2Mode(player2Mode == GameMode.HUMAN ? GameMode.ROBOT : GameMode.HUMAN);
        }
    }

    public synchronized GameState getGameState() {
        return gameState;
    }

    public synchronized void setGameState(GameState gameState) {
        this.gameState = gameState;
        gameStateLabel.setText(gameState.toString());
        if (gameState == GameState.NOT_STARTED) {
            resetButton.setText("Start");
            player1ModeBtn.setEnabled(true);
            player2ModeBtn.setEnabled(true);
            setCellsEnabled(false);
        } else {
            resetButton.setText("Reset");
            player1ModeBtn.setEnabled(false);
            player2ModeBtn.setEnabled(false);
            setCellsEnabled(true);
        }
        logger.info("Setting state: " + gameState);
    }

    void reset(ActionEvent e) {
        if (getGameState() == GameState.NOT_STARTED) {
            setGameState(GameState.IN_PROGRESS);
        } else {
            logger.info("Resetting game");
            setGameState(GameState.NOT_STARTED);
            setCurrentPlayer(Player.X);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    cells[i][j].setState(CellState.CLEAR);
                }
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameButton sender = (GameButton) e.getSource();
        if (getGameState().equals(GameState.IN_PROGRESS) && sender.getState().equals(CellState.CLEAR)) {

            sender.setState(getCurrentPlayer().equals(Player.X) ? CellState.FILLED_X : CellState.FILLED_O);

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
                    boolean equals = cells[row][column].getState().equals(getCurrentPlayer().toCellState());
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
                setGameState(getCurrentPlayer().equals(Player.X) ? GameState.X_WINS : GameState.O_WINS);
            } else if (clearCount == 0) {
                setGameState(GameState.DRAW);
            } else {
                setGameState(GameState.IN_PROGRESS);
            }

            nextPlayer();
        }
    }

    private synchronized void nextPlayer() {
        currentPlayer = currentPlayer.next();
    }

    public CellState[][] filedState() {
        CellState[][] result = new CellState[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = cells[i][j].state;
            }
        }
        return result;
    }

    public void turn(int row, int column) {
        actionPerformed(new ActionEvent(cells[row][column], ActionEvent.ACTION_PERFORMED, null));
    }

    private void setCellsEnabled(boolean enabled) {
        for (GameButton[] row: cells) {
            for (GameButton cell: row) {
                cell.setEnabled(enabled);
            }
        }
    }
}