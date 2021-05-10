package tictactoe;

public abstract class Robot extends Thread{
    TicTacToe game;

    public Robot(TicTacToe game) {
        super();
        this.game = game;
        this.setName("Robot");
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                if (game.getGameState().equals(GameState.IN_PROGRESS) &&
                        game.getCurrentPlayer().getMode().equals(GameMode.ROBOT)) {
                    makeTurn();
                }
                sleep(1000);
            }
        } catch (InterruptedException ignored) {
        }

    }

    protected abstract void makeTurn();
}
