package com.GameObjects.PacMan;

import com.GameLoop.InputManager;
import com.Utility.Debug;
import com.Utility.MoveDirection;
import javafx.scene.input.KeyCode;


public class PacManController implements Runnable {
    private MoveDirection m_moveDirection = MoveDirection.None;
    private boolean m_hasDirectionChanged = false;

    @Override
    public void run() {
        checkKeyPressed();
        if(m_hasDirectionChanged){
            Debug.Log("Direction changed:" +m_moveDirection.toString());
            m_hasDirectionChanged=false;
        }
    }

    public void checkKeyPressed() {
        if (InputManager.getInstance().isKeyPressed(KeyCode.UP)) {
            m_moveDirection = MoveDirection.Up;
            m_hasDirectionChanged = true;
        } else if (InputManager.getInstance().isKeyPressed(KeyCode.DOWN)) {
            m_moveDirection = MoveDirection.Down;
            m_hasDirectionChanged = true;
        } else if (InputManager.getInstance().isKeyPressed(KeyCode.RIGHT)) {
            m_moveDirection = MoveDirection.Right;
            m_hasDirectionChanged = true;
        } else if (InputManager.getInstance().isKeyPressed(KeyCode.LEFT)) {
            m_moveDirection = MoveDirection.Left;
            m_hasDirectionChanged = true;
        }
    }

}
