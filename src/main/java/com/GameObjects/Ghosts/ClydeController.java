package com.GameObjects.Ghosts;

import com.GameLoop.GameLoop;
import com.Utility.Debug;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class ClydeController extends GhostController {
    private int randMin = 4;
    private int randMax = 6;
    private long m_lastUpdate;
    private Vector2 m_startPoint = new Vector2(30, 29 * 30);
    private List<Vector2> m_stepsInScatter;

    public ClydeController(GhostModeController ghostModeController, Ghost ghost) {
        super(ghostModeController, ghost);
        m_stepsInScatter = new ArrayList<Vector2>();
    }

    @Override
    public void chaseMode() {
        if(shouldThreadExit.get())
            return;
        if (System.nanoTime() - m_lastUpdate > 1000000000) {
            m_steps.clear();
            moveDirection = MoveDirection.None;
            Vector2 randomPoint = randPointInRange();
            m_steps = findPathToPoint(randomPoint);
            Debug.Log("Clyde x:" + randomPoint.x + " y:" + randomPoint.y);
            //Debug.Log("Clyde size: "+ m_steps.size());
            m_lastUpdate = System.nanoTime();
        }
    }

    @Override
    public void distractMode() {

    }

    @Override
    public void wanderingMode() {

    }

    private Vector2 randPointInRange(){
        Vector2 newPoint =  new Vector2();
        Vector2 myPosition = new Vector2((int)m_ghost.getPosition().x/30,(int)m_ghost.getPosition().y/30);
        int myPositionX = (int)myPosition.y;
        int myPositionY = (int)myPosition.x;
        int board[][] = GameLoop.getInstance().gameBoard.BoardsPaths;
        //(x-a)^2 + (y-b)^2 = r^2 gdzie (a,b) to środek - pozycja ducha i r to promień
        List<Vector2> localOnes = GameLoop.getInstance().gameBoard.onesList;
        int sizeOfOnesList = localOnes.size();
        int index = ThreadLocalRandom.current().nextInt(0, sizeOfOnesList);
        int x = (int)localOnes.get(index).x;
        int y = (int)localOnes.get(index).y;
        while(((Math.pow(x - myPositionX,2)+Math.pow(y - myPositionY,2))>=Math.pow(randMax,2)) &&
                ((Math.pow(x - myPositionX,2)+Math.pow(y - myPositionY,2))<=Math.pow(randMin,2)) && x==myPositionX && y==myPositionY ){
            if(shouldThreadExit.get())
                return null;
            index = ThreadLocalRandom.current().nextInt(0, sizeOfOnesList);
            x = (int)localOnes.get(index).x;
            y = (int)localOnes.get(index).y;
        }
        newPoint.x = y;
        newPoint.y = x;

        return newPoint;
    }

}
