package com.GameObjects.Ghosts;

import com.GameLoop.GameLoop;
import com.Utility.Debug;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
/**
 * Kontroller zachowania ducha Clyde'a, rozszerza kontroller ogólnego zachowania ducha
 */
public class ClydeController extends GhostController {
    private int randMin = 4;
    private int randMax = 6;
    private Vector2 m_startPoint = new Vector2(30, 29 * 30);
    private List<Vector2> m_stepsInScatter;

    public ClydeController(GhostModeController ghostModeController, Ghost ghost) {
        super(ghostModeController, ghost);
        m_stepsInScatter = new ArrayList<Vector2>();
    }
    /**
     * Nadpisana metoda, określa zachowanie Clyde'a w trybie pościgu
     */
    @Override
    public void chaseMode() {
        if(shouldThreadExit.get())
            return;
        if (System.nanoTime() - m_lastUpdate > 1000000000) {
            m_steps.clear();
            moveDirection = MoveDirection.None;
            Vector2 randomPoint = randPointInRange();
            m_steps = findPathToPoint(randomPoint);
            //Debug.Log("Clyde x:" + randomPoint.x + " y:" + randomPoint.y);
            //Debug.Log("Clyde size: "+ m_steps.size());
            m_lastUpdate = System.nanoTime();
        }
    }
}
