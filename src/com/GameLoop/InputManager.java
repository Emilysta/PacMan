package com.GameLoop;

import com.Main;
import com.Utility.Debug;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InputManager {
    private static InputManager m_instance;

    private final List<KeyEvent> m_inputList;

    private InputManager() {
        m_inputList = Collections.synchronizedList(new ArrayList<>());
        Main.getInstance().getStage().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if(!m_inputList.stream().anyMatch(x-> x.getCode().getCode() == e.getCode().getCode())){
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

    public void EndFrame(){
        for(KeyEvent e : m_inputList)
            Debug.Log("Key: " + e.getCode().getCode());
        m_inputList.clear();
    }

    public boolean isKeyPressed(KeyCode keyCode){
        return m_inputList.stream().anyMatch(x-> x.getCode() == keyCode);
    }


}
