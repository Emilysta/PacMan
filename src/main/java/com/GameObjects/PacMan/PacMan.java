package com.GameObjects.PacMan;

import com.GameLoop.CollisionManager;
import com.GameObjects.GameObject;
import com.Utility.Debug;
import com.Utility.MoveDirection;
import com.Utility.Sprite;
import com.Utility.Vector2;

/**
 * Klasa reprezentuje pacMana w swiecie gry.
 * Klasa dziedzczy po klasie GameObject w celu bycia dodanym do glownej petli gry.
 */
public class PacMan extends GameObject {
    private final PacManController m_controller;
    private final Thread m_controllerThread;

    private float m_speed = 2;
    private boolean m_isMoving = false;
    private boolean m_isSuperPower = false;
    private MoveDirection m_moveDirection = MoveDirection.None;

    /**
     * Tworzy nowego pacmana
     * @param sprite - sprite jakim nalezy wyswietlac pacmana
     */
    public PacMan(Sprite sprite) {
        super(sprite);
        m_controller = new PacManController();
        m_controllerThread = new Thread(m_controller);
        m_position = new Vector2(30, 30);
    }

    /**
     * Metoda pozwwalająca uzyskać kierunek ruchu PacMan'a
     * @return kierunek ruchu PacMan'a
     */
    public MoveDirection getMoveDirection()
    {
        return m_moveDirection;
    }
    /**
     * Metoda pozwala na ustawienie zmiennej odpowiedzialnej za powerUp
     * @param value - true jesli ma byc poweredUp, false inaczej
     */
    public void setPowerUp(boolean value){
        m_isSuperPower = value;
    }

    /**
     * Metoda sprawdza czy pacMan zjadl power up
     * @return true jesli pacman jest poweredUp, false inaczej
     */
    public boolean isPacmanPoweredUp(){
        return m_isSuperPower;
    }

    /**
     * Metoda rozpoczyna watek kontrolera pacMana
     */
    @Override
    protected void onStart() {
        m_controllerThread.start();
        Debug.Log("Pacman started");
    }

    /**
     * Metoda sprawdza, czy gracz chcial zmienic kierunek ruchu, jesli tak to
     * proboje to zrobic
     */
    @Override
    protected void onUpdate() {
        if (m_controller.hasDirectionChanged.get())
            tryMove(m_controller.moveDirection);
        if (m_isMoving)
            move();
    }

    /**
     * Metoda zatrzymuje watek kontrolera przed wyjsciem
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
     * Metoda sprawdza czy dany ruch jest mozliwy do wykonania
     * @param moveDirection - kierunek ruchu
     * @return true jesli mozliwy, false inaczej
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
     * Metoda przesuwa PacMana w swiecie gry. Jesli ruch jest niedozwolony,
     * zatrzymuje pacmana w miejscu.
     */
    private void move() {
        if (CollisionManager.checkIfMovePossible(m_position, m_moveDirection)) {
            if (m_moveDirection == MoveDirection.Up)
                m_position.y -= m_speed;
            else if (m_moveDirection == MoveDirection.Down)
                m_position.y += m_speed;
            else if (m_moveDirection == MoveDirection.Right)
                m_position.x += m_speed;
            else if (m_moveDirection == MoveDirection.Left)
                m_position.x -= m_speed;
        } else {
            m_isMoving = false;
            m_moveDirection = MoveDirection.None;
        }
    }



}
