package com.GameObjects.Ghosts;

import com.Utility.GlobalReferenceManager;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import java.util.ArrayList;
import java.util.List;

public class BlinkyController extends GhostController{
    private long m_lastUpdate;
    private Vector2 m_startPoint = new Vector2(26*30,30);
    private List<Vector2> m_stepsInScatter;
    public BlinkyController(GhostModeController ghostModeController, Ghost ghost) {
        super(ghostModeController, ghost);
        m_stepsInScatter = new ArrayList<Vector2>();
    }

    @Override
    public void chaseMode() {
        if(System.nanoTime() - m_lastUpdate > 1000000000) {
            m_steps.clear();
            moveDirection= MoveDirection.None;
            m_steps = findPathToPoint(GlobalReferenceManager.pacMan.getPosition());
            m_lastUpdate =System.nanoTime();
        }
    }

    @Override
    public void distractMode() { //inaczej scatterMode
            m_steps = findPathToPoint(m_startPoint);

    }

    @Override
    public void wanderingMode() {

    }
}
