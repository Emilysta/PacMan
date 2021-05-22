package com.GameObjects.PacMan;

import com.GameLoop.InputManager;
import com.Utility.MoveDirection;
import javafx.scene.input.KeyCode;

import java.util.concurrent.atomic.AtomicBoolean;


public class PacManController implements Runnable {
    public MoveDirection moveDirection = MoveDirection.None;
    public AtomicBoolean hasDirectionChanged = new AtomicBoolean();
    public AtomicBoolean shouldThreadExit = new AtomicBoolean();

    @Override
    public void run() {
        while (true){
            checkKeyPressed();
            if(shouldThreadExit.get())
                return;
        }
    }

    public void checkKeyPressed() {
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
