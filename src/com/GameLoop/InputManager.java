package com.GameLoop;

import com.Main;
import com.Utility.Debug;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class InputManager {
    private static InputManager m_instance;

    private final Queue<KeyEvent> m_inputList;

    private int m_counter = 0;

    private InputManager() {
        m_inputList = new ConcurrentLinkedQueue<KeyEvent>();
        Main.getInstance().getStage().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (!m_inputList.stream().anyMatch(x -> x.getCode().getCode() == e.getCode().getCode())) {
                m_inputList.add(e);
            }
        });
    }

    public static InputManager getInstance() {
        if (m_instance == null) {
            m_instance = new InputManager();
        }
        return m_instance;
    }

    public void EndFrame() {
        m_counter++;
        if (m_counter / 5 == 0) {
            for (KeyEvent k : m_inputList)
                Debug.Log("Key clicked: " + k.getCode());
            m_inputList.clear();
            m_counter=0;
        }
    }

    public boolean isKeyPressed(KeyCode keyCode) {
        return m_inputList.stream().anyMatch(x -> x.getCode() == keyCode);
    }


}
