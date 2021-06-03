package com.GameObjects.Ghosts;

import com.GameLoop.CollisionManager;
import com.Utility.GlobalReferenceManager;
import com.Utility.MoveDirection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Klasa kontrolująca tryb pracy ducha, sprawdza kolizję ducha z pac-manem,
 * określa czy występuje tryb po zjedzeniu specjalnego pieniążka itp.
 */

public class GhostController implements Runnable{

    public GhostMode ghostMode = GhostMode.ChaseMode;
    //public MoveDirection moveDirection = MoveDirection.None;
    public AtomicBoolean hasDirectionChanged = new AtomicBoolean();
    public AtomicBoolean shouldThreadExit = new AtomicBoolean();
    @Override
    public void run() {
        while (true){

            if(shouldThreadExit.get())
                return;
        }
    }

    public void CheckMode()
    {
        if(CollisionManager.checkIfCollisionWithPacMan(GlobalReferenceManager.pacMan.getPosition(),)) {
            ghostMode = GhostMode.DeadMode;
            return;
        }
        //if(GlobalReferenceManager.pacMan.ifAteSpecialCoin()){
            //ghostMode = GhostMode.WanderingMode;
        //}
        //toDo mode chase and distract

    }

}
