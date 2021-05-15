package com.GameLoop;

import com.GameObjects.GameObject;
import com.Utility.Debug;

import java.util.ArrayList;

/**
 * Main GameLoop class, running on a thread separate from the UI.
 * GameLoop class implements a singleton pattern to make sure it is only run
 * once in the whole project. It implements observer design pattern, notifying
 * all GameObjects when the game starts and updates every frame.
 */
public class GameLoop implements Runnable {

    private static GameLoop m_instance;
    private final int m_fps = 1;
    private final ArrayList<GameObject> m_listeners;
    private Thread m_mainThread;
    private boolean m_isGamePlaying = false;
    private InputManager inputManager;

    private GameLoop() {
        inputManager = InputManager.getInstance();
        m_listeners = new ArrayList<>();
    }

    /**
     * Method returns the singleton instance of the main GameLoop object.
     * If the instance hasn't been created yet, it invokes it's constructor
     * and return the new instance
     *
     * @return GameLoop - singleton instance
     */
    public static GameLoop getInstance() {
        if (m_instance == null) {
            m_instance = new GameLoop();
        }
        return m_instance;
    }

    /**
     * Method implements observer design pattern to allow for sync of all
     * GameObjects in the game.
     *
     * @param gameObject - gameObject to be added as a listener to MainLoop update.
     */
    public void addListener(GameObject gameObject) {
        if (!m_listeners.contains((gameObject))) {
            m_listeners.add(gameObject);
        }
    }

    /**
     * Public method created for the Runnable interface.
     * To run on separate thread, create a new Thread from the
     * GameLoop instance and invoke start() method.
     * Warning - this method runs on the main thread.
     */
    @Override
    public void run() {
        final long frameTime = 1000000000 / m_fps;
        m_isGamePlaying = true;
        startAllGameObjects();
        long second = 0;
        int fps = 0;
        while (m_isGamePlaying) {
            long lastLoopTime = System.nanoTime();
            playGameLoop(frameTime, lastLoopTime);
            second += System.nanoTime() - lastLoopTime;
            fps += 1;
            if (second >= 1000000000) {
                Debug.Log("FPS: " + fps);
                fps = 0;
                second = 0;
            }
        }
    }

    /**
     * Creates a new thread on which the gameLoop is run.
     * Use this over run() to run on separate thread.
     */
    public void start() {
        if (m_mainThread == null) {
            m_mainThread = new Thread(GameLoop.getInstance());
            m_mainThread.start();
            Debug.Log("Main GameLoop started");
        }
    }

    /**
     * Stops the running GameLoop and joins it's thread.
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
    }

    private void playGameLoop(long frameTime, long lastLoopTime) {
        while (System.nanoTime() < lastLoopTime + frameTime) ;
        updateAllGameObjects();
        inputManager.EndFrame();
    }

    private void updateAllGameObjects() {
        for (GameObject gameObject : m_listeners)
            gameObject.update();
    }

    private void startAllGameObjects() {
        for (GameObject gameObject : m_listeners)
            gameObject.start();
    }
}
