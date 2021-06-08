package com.GameObjects.Ghosts;

import com.GameLoop.CollisionManager;
import com.GameObjects.GameObject;
import com.Utility.Debug;
import com.Utility.MoveDirection;
import com.Utility.Sprite;
import com.Utility.Vector2;

/**
 * Klasa ducha będąca rozszerzeniem klasy bazowej - GameObject
 */
public class Ghost extends GameObject {

    private GhostModeController m_ghostModeController;// kontroler ruchów konkretnego ducha
    private GhostMode m_ghostMode; // tryb, w którym duch się znajduje
    private GhostController m_ghostController; // kontroler ruchów konkretnego ducha
    private final Thread m_controllerThread; // wątek uruchamiający kontroler trybu konkretnego ducha
    private final Thread m_controllerThread2; // wątek uruchamiający kontroler ruchów konkretnego ducha
    private MoveDirection m_moveDirection = MoveDirection.None; // określa kierunek ruchu
    private float m_speed = 2;

    public Vector2 homePosition; // położenie domu na mapie
    public GhostType m_ghostType; // określa typ ducha - Blinky,Inky,Clyde,Pinky

    /**
     * Konstruktor ducha, przypisuje mu odpowiednie zdjęcie oraz typ ducha, jakim
     * jest
     * 
     * @param sprite    zdjęcie, które zostanie wyrednerowane
     * @param ghostType dyo ducha - Blinky, Inky, Clyde, Pinky
     */
    public Ghost(Sprite sprite, GhostType ghostType) {
        super(sprite);
        m_ghostModeController = new GhostModeController(this);
        m_controllerThread = new Thread(m_ghostModeController);
        m_position = new Vector2(90, 30);
        homePosition = new Vector2(14, 11);
        m_ghostType = ghostType;

        switch (ghostType) {
            case Blinky: {
                m_position = new Vector2(26 * 30, 30);
                m_ghostController = new BlinkyController(m_ghostModeController, this);
                break;
            }
            case Inky: {
                m_position = new Vector2(26 * 30, 29 * 30);
                m_ghostController = new InkyController(m_ghostModeController, this);
                break;
            }
            case Clyde: {
                m_position = new Vector2(30, 29 * 30);
                m_ghostController = new ClydeController(m_ghostModeController, this);
                break;
            }
            case Pinky: {
                m_position = new Vector2(120, 30);
                m_ghostController = new PinkyController(m_ghostModeController, this);
                break;
            }
        }
        m_controllerThread2 = new Thread(m_ghostController);
    }

    /**
     * Nadpisana metoda wywołująca się na starcie gry, uruchamia wątki
     */
    @Override
    protected void onStart() {
        m_controllerThread.start();
        Debug.Log(m_ghostType + " ghost started");
        m_controllerThread2.start();
    }

    /**
     * Nadpisana metoda wywołująca się w każdej klatece gry, ustawia kierunek ruchu
     * obiektu, oraz wywołuje metodę poruszania się
     */
    @Override
    protected void onUpdate() {
        m_ghostMode = m_ghostModeController.ghostMode;
        m_moveDirection = m_ghostController.moveDirection;
        // Debug.Log("Update duszka: " + m_moveDirection);
        move();
    }

    /**
     * Nadpisana metoda wywołująca się przy zakończeniu gry, lub zniszczeniu obiektu
     * Kończy działanie wątków kontrolujących zachowanie obiektu
     */
    @Override
    protected void onExit() {
        if (m_controllerThread.isAlive() || m_controllerThread2.isAlive()) {
            try {
                m_ghostModeController.shouldThreadExit.set(true);
                m_ghostController.shouldThreadExit.set(true);
                m_controllerThread.interrupt();
                m_controllerThread2.interrupt();
                m_controllerThread.join();
                Debug.Log(m_ghostType + " mode controller dead");
                m_controllerThread2.join();
                Debug.Log(m_ghostType + " controller dead");
            } catch (InterruptedException e) {
                m_ghostModeController.shouldThreadExit.set(true);
                m_ghostController.shouldThreadExit.set(true);
                m_controllerThread.interrupt();
                m_controllerThread2.interrupt();
                Debug.Log(m_ghostType + " mode controller interrupted");
                Debug.Log(m_ghostType + " controller interrupted");
            }
        }
    }

    /**
     * Metoda przemieszcza ducha w świecie, w zalezności od ustalonego kierunku.
     * Jeśli ruch jest niedozwolony, duch zatrzymuje się w miejscu.
     */
    private void move() {
        checkSpeed();

        if (CollisionManager.checkIfMovePossible(m_position, m_moveDirection)) {
            if (m_moveDirection == MoveDirection.Up)
                m_position.y -= m_speed;
            else if (m_moveDirection == MoveDirection.Down)
                m_position.y += m_speed;
            else if (m_moveDirection == MoveDirection.Right)
                m_position.x += m_speed;
            else if (m_moveDirection == MoveDirection.Left)
                m_position.x -= m_speed;
        } else
            m_moveDirection = MoveDirection.None;
    }

    /**
     * Metoda ustawia predkosc ducha w zaleznosci od jego trybu
     */
    private void checkSpeed() {
        if (m_ghostMode == GhostMode.WanderingMode && m_speed == 2) {
            m_speed = 1;
            Debug.Log("speed = 1");
        } else if (m_ghostMode != GhostMode.WanderingMode && m_speed == 1) {
            if (m_position.x % 2 == 0 && m_position.y % 2 == 0) {
                m_speed = 2;
                Debug.Log("speed = 2");
            }
        }
    }

}
