package tictactoe;

public enum GameState {
    NOT_STARTED("Game is not started"),
    IN_PROGRESS("Game in progress"),
    X_WINS("X wins"),
    O_WINS("O wins"),
    DRAW("Draw");

    private final String caption;

    GameState(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return  caption;
    }
}
