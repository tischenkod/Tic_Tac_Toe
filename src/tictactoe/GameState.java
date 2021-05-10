package tictactoe;

public enum GameState {
    NOT_STARTED("Game is not started"),
    IN_PROGRESS("The turn of %s Player (%s)"),
    WINS("The %s Player (%s) wins"),
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
