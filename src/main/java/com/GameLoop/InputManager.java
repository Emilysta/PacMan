package com.GameLoop;

import com.Main;
import com.Utility.Debug;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Global class managing user input
 * Implements singleton pattern
 */
public class InputManager {
    private static InputManager m_instance;

    private final Queue<KeyEvent> m_inputList;

    private int m_counter = 0;

    /**
     * Private constructor implements singleton pattern
     * Input manager adds an event filter into the main stage which takes the
     * given key and inserts it into the input list
     */
    private InputManager() {
        m_inputList = new ConcurrentLinkedQueue<>();
        Main.getInstance().getStage().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (!m_inputList.stream().anyMatch(x -> x.getCode().getCode() == e.getCode().getCode())) {
                m_inputList.add(e);
            }
        });
    }

    /**
     * @return singleton instance
     */
    public static InputManager getInstance() {
        if (m_instance == null) {
            m_instance = new InputManager();
        }
        return m_instance;
    }
    /**
     * Method ends the current frame input capturing
     */
    public void endFrame() {
        m_counter++;
        if (m_counter / 5 == 0) {
            for (KeyEvent k : m_inputList)
                Debug.Log("Key clicked: " + k.getCode());
            m_inputList.clear();
            m_counter=0;
        }
    }
    /**
     * Method allows to check whether a key was pressed during the last n frames
     * @param keyCode - key to check for
     * @return true if key was pressed, false otherwise
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return m_inputList.stream().anyMatch(x -> x.getCode() == keyCode);
    }


}
