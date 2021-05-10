package tictactoe;

public enum GameMode {
    HUMAN("Human"),
    ROBOT("Robot");

    private final String caption;

    GameMode(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return caption;
    }
}
