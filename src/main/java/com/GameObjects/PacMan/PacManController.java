package com.GameObjects.PacMan;

import com.GameLoop.InputManager;
import com.Utility.MoveDirection;
import javafx.scene.input.KeyCode;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Klasa kontrolujaca pacmana. Obsluguje interakcje z uzytkownikiem i powiadamia
 * o potrzebie zmiany kierunku.
 */
public class PacManController implements Runnable {
    public MoveDirection moveDirection = MoveDirection.None;
    public AtomicBoolean hasDirectionChanged = new AtomicBoolean();
    public AtomicBoolean shouldThreadExit = new AtomicBoolean();

    /**
     * Watek sprawdza czy ktorys z klawiszy zmiany kierunk zostal wcisniety. Watek
     * konczy sie jesli zmienna shouldThreadExit zostanie ustawiona na true
     */
    @Override
    public void run() {
        while (true) {
            checkKeyPressed();
            if (shouldThreadExit.get())
                return;
        }
    }

    /**
     * Metoda sprawdza czy ktorys z klawiszy zmiany kierunku zostal wcisniety. Jesli
     * tak, ustawia odpowiednia zmienna w
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
