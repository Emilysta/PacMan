package com.GameObjects.Ghosts;

import com.Utility.GlobalReferenceManager;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Kontroller zachowania ducha Blinky'iego, rozszerza kontroller ogólnego zachowania ducha
 */
public class BlinkyController extends GhostController{
    private long m_lastUpdate;
    private Vector2 m_startPoint = new Vector2(26*30,30);
    private List<Vector2> m_stepsInScatter;
    public BlinkyController(GhostModeController ghostModeController, Ghost ghost) {
        super(ghostModeController, ghost);
        m_stepsInScatter = new ArrayList<Vector2>();
    }
    /**
     * Nadpisana metoda, określa zachowanie Blinky'iego w trybie pościgu
     */
    @Override
    public void chaseMode() {
        if(System.nanoTime() - m_lastUpdate > 1000000000) {
            m_steps.clear();
            moveDirection= MoveDirection.None;
            Vector2 pacmanPosition = new Vector2(GlobalReferenceManager.pacMan.getPosition().x/30,GlobalReferenceManager.pacMan.getPosition().y/30);
            m_steps = findPathToPoint(pacmanPosition);
            m_lastUpdate =System.nanoTime();
        }
    }
}
