package tictactoe;

import java.util.function.Consumer;

public enum Player {
    X,
    O;

    public Consumer<String> modeChanged;
    int turnCount;
    GameMode mode;

    Player next() {
        turnCount++;
        return this.equals(X) ? O : X;
    }

    Player() {
        turnCount = 0;
        mode = GameMode.HUMAN;
    }

    public CellState toCellState() {
        return this.equals(X) ? CellState.FILLED_X : CellState.FILLED_O;
    }

    public GameMode getMode() {
        GameMode result;
        synchronized (TicTacToe.class) {
            result = mode;
        }
        return result;
    }

    public void setMode(GameMode mode) {
        synchronized (TicTacToe.class) {
            this.mode = mode;
            if (modeChanged != null) {
                modeChanged.accept(mode.toString());
            }
        }
    }

    public void switchMode() {
        synchronized (TicTacToe.class) {
            mode = mode.equals(GameMode.HUMAN) ? GameMode.ROBOT : GameMode.HUMAN;
            if (modeChanged != null) {
                modeChanged.accept(mode.toString());
            }
        }
    }
}
