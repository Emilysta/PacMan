package com.GameObjects.Ghosts;

import com.GameLoop.CollisionManager;
import com.Utility.GlobalReferenceManager;
import com.Utility.MoveDirection;

import javafx.application.Platform;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Klasa kontrolująca tryb pracy ducha, sprawdza kolizję ducha z pac-manem,
 * określa czy występuje tryb po zjedzeniu specjalnego pieniążka itp.
 */

public class GhostModeController implements Runnable {

    public GhostMode ghostMode = GhostMode.ChaseMode;
    public AtomicBoolean shouldThreadExit = new AtomicBoolean();
    private Ghost m_ghost;

    public GhostModeController(Ghost ghost) {
        m_ghost = ghost;
    }

    @Override
    public void run() {
        while (true) {
            checkMode();
            if (shouldThreadExit.get())
                return;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // e.printStackTrace();
                shouldThreadExit.set(true);
            }
        }
    }

    public void checkMode() {
        ghostMode = GhostMode.ChaseMode;
        if (GlobalReferenceManager.pacMan.isPacmanPoweredUp()) {
            ghostMode = GhostMode.WanderingMode;
        }
        if (CollisionManager.checkIfCollisionWithPacMan(m_ghost.getPosition())) {
            if (GlobalReferenceManager.pacMan.isPacmanPoweredUp()) {
                ghostMode = GhostMode.DeadMode;
            } else {
                Platform.runLater(() -> GlobalReferenceManager.boardController.loseGame());

            }
        }
        // ToDo distract mode
    }
}
