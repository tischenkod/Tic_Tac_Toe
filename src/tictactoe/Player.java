package tictactoe;

public enum Player {
    X,
    O;

    Player next() {
        return this.ordinal() == 0 ? O : X;
    }

    public CellState toCellState() {
        return this.equals(X) ? CellState.FILLED_X : CellState.FILLED_O;
    }
}
