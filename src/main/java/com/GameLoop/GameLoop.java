package com.GameLoop;

import com.Board.PredefinedBoard;
import com.GameObjects.GameObject;
import com.Utility.Debug;
import javafx.scene.canvas.GraphicsContext;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Klasa odpowiada za tworzenie głównego wątku gry. Klasa implementuje wzorzec
 * projektowy singletona, tak aby w każdym miejscu można było się odwołać do
 * jednej wspólnej instancji klasy. Klasa jest również miejscem synchronizacji
 * dla wszystkich obiektów klasy GameObject. Odpowiada za wywoływanie metod co
 * klatkę w każdym obiekcie GameObject.
 */
public class GameLoop implements Runnable {

    public PredefinedBoard gameBoard;

    private static GameLoop m_instance;
    private final int m_fps = 30;
    private final Queue<GameObject> m_listeners;
    private Thread m_mainThread;
    private boolean m_isGamePlaying = false;
    private final InputManager inputManager;
    private Renderer m_mainRenderer;

    private GameLoop() {
        inputManager = InputManager.getInstance();
        m_listeners = new ConcurrentLinkedQueue<>();
    }

    public boolean isGameRunning() {
        return m_isGamePlaying;
    }

    /**
     * Metoda zwraca instancje singletona klasy GameLoop. Jeśli taka instancja nie
     * istnieje, wywołuje konstruktor i zwraca nowy obiekt.
     *
     * @return GameLoop - instancja singletonu klasy GameLoop
     */
    public static GameLoop getInstance() {
        if (m_instance == null) {
            m_instance = new GameLoop();
        }
        return m_instance;
    }

    /**
     * Metoda pozwalająca na implementacje wzorca obserwatora dla klas GameObject.
     *
     * @param gameObject - gameObject który ma zostać dodany do listy obserwatorów.
     */
    public void addListener(GameObject gameObject) {
        if (!m_listeners.contains((gameObject))) {
            m_listeners.add(gameObject);
        }
        if (m_isGamePlaying)
            gameObject.start();
    }

    /**
     * Metoda pozwala na usunięcie klasy GameObject z list obserwatorów oraz z gry.
     * 
     * @param gameObject - gameObject, który ma zostac usuniety z listy
     * @return true jesli został usuniety, false inaczej
     */

    public boolean removeListener(GameObject gameObject) {
        if (m_listeners.contains((gameObject))) {
            m_listeners.remove(gameObject);
            return true;
        }
        return false;
    }

    /**
     * Metoda ustawia graphicContext na ktorym wszystkie gameObject bedą
     * 
     * @param graphicsContext - graphicsContext canvas'u na ktorym renderowac
     *                        obiekty
     */
    public void setGraphicContext(GraphicsContext graphicsContext, double width, double height) {
        m_mainRenderer = new Renderer(graphicsContext, width, height);
    }

    /**
     * Metoda ustawia aktualna plansze na ktorej odbywa sie gra
     * 
     * @param board - aktualna plansza
     */
    public void setBoard(PredefinedBoard board) {
        gameBoard = board;
    }

    /**
     * Metoda stworzona dla interfejsu Runnable. Implementuje pelna petle gry, z
     * wywołaniem metod update oraz renderowania obiektow na scenie. Bezpośrednie
     * uruchomienie metody powoduje działanie na głownym watku. W celu poprawnego
     * uruchomienia klasy na osobnym watku nalezy uzyc metody start
     */
    @Override
    public void run() {
        final long frameTime = 1000000000 / m_fps;
        m_isGamePlaying = true;
        startAllGameObjects();
        while (m_isGamePlaying) {
            long lastLoopTime = System.nanoTime();
            playOneFrame(frameTime, lastLoopTime);
        }
    }

    /**
     * Metoda tworzy nowy watek na ktorym dziala glowna petla gry.
     */
    public void start() {
        stop();
        if (m_mainThread == null) {
            m_mainThread = new Thread(GameLoop.getInstance());
            m_mainThread.start();
            Debug.Log("Main GameLoop started");
        }
    }

    /**
     * Metoda zatrzymuje aktualnie dzialajaca petle gry, i zabija wszystkie
     * przynalezne watki.
     */
    public void stop() {
        m_isGamePlaying = false;
        if (m_mainThread != null) {
            try {
                if (m_mainThread.isAlive()) {
                    m_mainThread.join();
                    Debug.Log("Main GameLoop stopped");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopAllGameObjects();
        m_mainThread = null;
    }

    /**
     * Metoda czysci wszystkie dane, w przygotowaniu na kolejna gre
     */
    public void clearData() {
        m_listeners.clear();
        gameBoard = null;
        m_mainRenderer = null;
    }

    /**
     * Metoda wywoluje update we wszystkich gameObject'ach, nastepnie je wszystkie
     * renderuje na zadanym canvas.
     * 
     * @param frameTime    - optymalny czas jednej klatki
     * @param lastLoopTime - czas zakonczenia ostatniej klatki
     */
    private void playOneFrame(long frameTime, long lastLoopTime) {
        updateAllGameObjects();
        renderGameObjects();
        inputManager.endFrame();
        while (System.nanoTime() < lastLoopTime + frameTime)
            ;
    }

    /**
     * Metoda wywoluje start we wszystkich gameObject'ach
     */
    private void startAllGameObjects() {
        for (GameObject gameObject : m_listeners)
            gameObject.start();
    }

    /**
     * Metoda wywoluje update we wszystkich gameObject'ach
     */
    private void updateAllGameObjects() {
        for (GameObject gameObject : m_listeners)
            gameObject.update();
    }

    /**
     * Metoda wywoluje start we wszystkich gameObject'ach
     */
    private void stopAllGameObjects() {
        for (GameObject gameObject : m_listeners)
            gameObject.exit();
    }

    /**
     * Metoda renderuje wszystkie gameObjecty na scenie
     */
    private void renderGameObjects() {
        m_mainRenderer.prepareScene();
        for (GameObject gameObject : m_listeners)
            m_mainRenderer.renderObject(gameObject);
    }

}
