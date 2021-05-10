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
                        (game.getCurrentPlayer().equals(Player.X) && game.getPlayer1Mode().equals(GameMode.ROBOT) ||
                                game.getCurrentPlayer().equals(Player.O) && game.getPlayer2Mode().equals(GameMode.ROBOT))) {
                    makeTurn();
                }
                sleep(1000);
            }
        } catch (InterruptedException ignored) {
        }

    }

    protected abstract void makeTurn();
}
