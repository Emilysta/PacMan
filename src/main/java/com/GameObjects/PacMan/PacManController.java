package com.GameObjects.PacMan;

import com.GameLoop.InputManager;
import com.Utility.MoveDirection;
import javafx.scene.input.KeyCode;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Pacman helper class, running on separate thread
 */
public class PacManController implements Runnable {
    public MoveDirection moveDirection = MoveDirection.None;
    public AtomicBoolean hasDirectionChanged = new AtomicBoolean();
    public AtomicBoolean shouldThreadExit = new AtomicBoolean();

    /**
     * Thread runs all the time, checking for key press of any of the arrows.
     * Thread stops when the shouldThreadExit variable is set to true.
     */
    @Override
    public void run() {
        while (true){
            checkKeyPressed();
            if(shouldThreadExit.get())
                return;
        }
    }
    /**
     * Method checks whether any of the arrow keys was pressed. If so, it sets
     * the variable hasDirectionChanged to true and moves direction accordingly
     */
    private void checkKeyPressed() {
        if (InputManager.getInstance().isKeyPressed(KeyCode.UP)) {
            moveDirection = MoveDirection.Up;
            hasDirectionChanged.set(true);
        } else if (InputManager.getInstance().isKeyPressed(KeyCode.DOWN)) {
            moveDirection = MoveDirection.Down;
            hasDirectionChanged.set(true);
        } else if (InputManager.getInstance().isKeyPressed(KeyCode.RIGHT)) {
            moveDirection = MoveDirection.Right;
            hasDirectionChanged.set(true);
        } else if (InputManager.getInstance().isKeyPressed(KeyCode.LEFT)) {
            moveDirection = MoveDirection.Left;
            hasDirectionChanged.set(true);
        }
    }

}
