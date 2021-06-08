package com.GameObjects.Ghosts;

import com.GameLoop.GameLoop;
import com.Utility.Debug;
import com.Utility.GlobalReferenceManager;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import java.util.ArrayList;
import java.util.List;
/**
 * Kontroller zachowania ducha Pinky'iego, rozszerza kontroller ogólnego zachowania ducha
 */
public class PinkyController extends GhostController {
    private long m_lastUpdate;
    private Vector2 m_startPoint = new Vector2(120,30);
    private List<Vector2> m_stepsInScatter;

    public PinkyController(GhostModeController ghostModeController, Ghost ghost) {
        super(ghostModeController, ghost);
        m_stepsInScatter = new ArrayList<Vector2>();
    }
    /**
     * Nadpisana metoda, określa zachowanie Pinky'iego w trybie pościgu
     */
    @Override
    public void chaseMode() {
        if (System.nanoTime() - m_lastUpdate > 1000000000) {
            m_steps.clear();
            moveDirection = MoveDirection.None;
            Vector2 point = find2DotsTowardsPacMan();
            m_steps = findPathToPoint(point);
            //Debug.Log("Pinky size: "+m_steps.size());
            m_lastUpdate = System.nanoTime();
        }
    }
}
