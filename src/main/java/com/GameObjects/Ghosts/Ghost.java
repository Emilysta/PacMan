package com.GameObjects.Ghosts;

import com.GameLoop.CollisionManager;
import com.GameObjects.GameObject;
import com.Utility.Debug;
import com.Utility.MoveDirection;
import com.Utility.Sprite;
import com.Utility.Vector2;

public class Ghost extends GameObject {

    private GhostModeController m_ghostModeController;
    private GhostMode m_ghostMode;
    private GhostController m_ghostController;
    private GhostType m_ghostType;
    private final Thread m_controllerThread;

    //private boolean m_isMoving = false;
    private MoveDirection m_moveDirection = MoveDirection.None;
    public Vector2 homePosition; //narazie jako wejście do ich domku

    public Ghost(Sprite sprite,GhostType ghostType) {
        super(sprite);
        m_ghostModeController = new GhostModeController(this);
        m_controllerThread = new Thread(m_ghostModeController);
        m_position = new Vector2(90, 30);
        homePosition = new Vector2(13*30,14*30);
        m_ghostType = ghostType;
        m_ghostController = new GhostController(m_ghostModeController,this);
        m_ghostController.run();

    }

    @Override
    protected void onStart() {
        m_controllerThread.start();
        Debug.Log("Ghost started");
    }

    @Override
    protected void onUpdate() {
        m_moveDirection = m_ghostController.moveDirection;
        move();
    }

    @Override
    protected void onExit() {
        if (m_controllerThread.isAlive()) {
            try {
                m_ghostModeController.shouldThreadExit.set(true);
                m_controllerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method moves pacman in the gameWorld according to it's current move
     * direction. If move is illegal, it stops pacman in place.
     */
    private void move() {
        if (CollisionManager.checkIfMovePossible(m_position, m_moveDirection)) {
            if (m_moveDirection == MoveDirection.Up)
                m_position.y -= 2;
            else if (m_moveDirection == MoveDirection.Down)
                m_position.y += 2;
            else if (m_moveDirection == MoveDirection.Right)
                m_position.x += 2;
            else if (m_moveDirection == MoveDirection.Left)
                m_position.x -= 2;
        } else
            m_moveDirection = MoveDirection.None;
    }
}
