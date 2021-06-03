package com.GameObjects.PacMan;

import com.GameLoop.CollisionManager;
import com.GameObjects.GameObject;
import com.Utility.Debug;
import com.Utility.MoveDirection;
import com.Utility.Sprite;
import com.Utility.Vector2;

/**
 * Class represents the pac man in the game world
 * Class extend the GameObject class to be included in the main gameLoop
 */
public class PacMan extends GameObject {
    private final PacManController m_controller;
    private final Thread m_controllerThread;

    private boolean m_isMoving = false;
    private boolean m_isSuperPower = false;
    private MoveDirection m_moveDirection = MoveDirection.None;

    /**
     * Creates a new pacman with the given sprite
     * @param sprite - image to show pacman as
     */
    public PacMan(Sprite sprite) {
        super(sprite);
        m_controller = new PacManController();
        m_controllerThread = new Thread(m_controller);
        m_position = new Vector2(30, 30);
    }

    /**
     * Method checks whether pacman has eaten a special coin
     * @return true if pacman is powered up, false otherwise
     */
    public boolean isPacmanPoweredUp(){
        return m_isSuperPower;
    }

    /**
     * Method starts the separate thread by which pacman is controlled
     */
    @Override
    protected void onStart() {
        m_controllerThread.start();
        Debug.Log("Pacman started");
    }

    /**
     * Method check wheter user tried to change direction, if so it checks the
     * possibility of that move and moves the pacman in the game world if possible
     */
    @Override
    protected void onUpdate() {
        if (m_controller.hasDirectionChanged.get())
            tryMove(m_controller.moveDirection);
        if (m_isMoving)
            move();
    }

    /**
     * Method stops the controller thread before exiting
     */
    @Override
    protected void onExit() {
        if (m_controllerThread.isAlive()) {
            try {
                m_controller.shouldThreadExit.set(true);
                m_controllerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method checks wheter the move is possible 
     * @param moveDirection - direction in which to try the move
     * @return true if possible, false otherwise
     */
    private boolean tryMove(MoveDirection moveDirection) {
        if (CollisionManager.checkIfMovePossible(m_position, moveDirection)) {
            m_isMoving = true;
            m_moveDirection = moveDirection;
            m_controller.hasDirectionChanged.set(false);
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
        } else {
            m_isMoving = false;
            m_moveDirection = MoveDirection.None;
        }
    }

}
