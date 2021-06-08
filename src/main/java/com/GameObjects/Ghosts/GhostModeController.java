package com.GameObjects.Ghosts;

import com.GameLoop.CollisionManager;
import com.Utility.Debug;
import com.Utility.GlobalReferenceManager;

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

    /**
     * Konstruktor klasy, przypisuje odpowiedniego ducha do swojego pola
     * 
     * @param ghost - duch,dla którego wątek będzie Mode Controllerem
     */
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

    /**
     * Metoda sprawdzająca, w jakim trybie powinien znajdować się duszek
     */
    public void checkMode() {
        if (ghostMode != GhostMode.DeadMode) {
            ghostMode = GhostMode.ChaseMode;
            if (GlobalReferenceManager.pacMan.isPacmanPoweredUp()) {
                ghostMode = GhostMode.WanderingMode;
            }
            if (CollisionManager.checkIfCollisionWithPacMan(m_ghost.getPosition())) {
                if (GlobalReferenceManager.pacMan.isPacmanPoweredUp()) {
                    ghostMode = GhostMode.DeadMode;
                    Debug.Log(m_ghost.m_ghostType.toString() + " mode:" + ghostMode.toString());
                } else {
                    Platform.runLater(() -> GlobalReferenceManager.boardController.loseGame());
                }
            }
        } else {
            if (m_ghost.getPosition().equals(m_ghost.homePosition.multiply(30))) {
                ghostMode = GhostMode.ChaseMode;
                Debug.Log(m_ghost.m_ghostType.toString() + " mode:" + ghostMode.toString());
            }
        }
    }
}
