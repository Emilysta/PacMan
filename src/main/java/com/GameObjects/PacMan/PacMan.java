package com.GameObjects.PacMan;

import com.GameLoop.CollisionManager;
import com.GameObjects.GameObject;
import com.Utility.Debug;
import com.Utility.MoveDirection;
import com.Utility.Sprite;
import com.Utility.Vector2;

public class PacMan extends GameObject {
    private final PacManController controller;
    private final Thread m_controllerThread;

    private boolean m_isMoving = false;
    private MoveDirection m_moveDirection = MoveDirection.None;

    public PacMan(Sprite sprite) {
        super(sprite);
        controller = new PacManController();
        m_controllerThread = new Thread(controller);
        m_position = new Vector2(30, 30);
    }

    @Override
    protected void onStart() {
        m_controllerThread.start();
        Debug.Log("Pacman started");
    }

    @Override
    protected void onUpdate() {
        if (controller.hasDirectionChanged.get())
            tryMove(controller.moveDirection);
        if (m_isMoving)
            move();
    }

    @Override
    protected void onExit() {
        if (m_controllerThread.isAlive()) {
            try {
                controller.shouldThreadExit.set(true);
                m_controllerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tryMove(MoveDirection moveDirection) {
        if (CollisionManager.checkIfMovePossible(m_position, moveDirection)) {
            m_isMoving = true;
            m_moveDirection = moveDirection;
            controller.hasDirectionChanged.set(false);
            return true;
        } else {
            if (m_isMoving) {
                if (m_moveDirection == MoveDirection.Down || m_moveDirection == MoveDirection.Up) {
                    if (moveDirection == MoveDirection.Left || moveDirection == MoveDirection.Right) {
                        return false;
                    }
                } else if (m_moveDirection == MoveDirection.Left || m_moveDirection == MoveDirection.Right) {
                    if (moveDirection == MoveDirection.Down || moveDirection == MoveDirection.Up) {
                        return false;
                    }
                }
            } else {
                m_isMoving = false;
                m_moveDirection = MoveDirection.None;
                return false;
            }
        }
        return false;
    }

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
        } else {
            m_isMoving = false;
            m_moveDirection = MoveDirection.None;
        }
    }

}
