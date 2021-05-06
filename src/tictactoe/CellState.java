package tictactoe;

public enum CellState {
    CLEAR(" "),
    FILLED_X("X"),
    FILLED_O("O");

    private final String caption;

    CellState(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return caption;
    }
}
