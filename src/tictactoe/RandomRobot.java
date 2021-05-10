package tictactoe;

import java.util.Random;

public class RandomRobot extends Robot{
    Random random;

    public RandomRobot(TicTacToe game) {
        super(game);
        random = new Random();
    }

    @Override
    protected void makeTurn() {
        int emptyCount = 0;
        CellState[][] fieldState = game.filedState();
        for (CellState[] row: fieldState) {
            for (CellState cell: row) {
                if (cell.equals(CellState.CLEAR)) {
                    emptyCount++;
                }
            }
        }
        if (emptyCount > 0) {
            int target = random.nextInt(emptyCount);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (fieldState[i][j].equals(CellState.CLEAR)) {
                        if (target-- == 0) {
                            game.turn(i, j);
                            return;
                        }
                    }
                }
            }
        }
    }
}
