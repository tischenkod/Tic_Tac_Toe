package tictactoe;

import javax.swing.*;
import java.util.logging.*;

public class GameButton extends JButton{
    CellState state;
    Logger logger;

    public GameButton(String caption) {
        super(caption);
        logger = Logger.getLogger(getClass().getName());
        logger.setUseParentHandlers(false);
        logger.setFilter(record -> false);

        setState(CellState.CLEAR);
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
        logger.info("Setting state of " + getName() + " to: " + state);
        setText(state.toString());
    }

}
