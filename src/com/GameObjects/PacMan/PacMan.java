package com.GameObjects.PacMan;

import com.GameObjects.GameObject;
import com.Utility.Debug;

public class PacMan extends GameObject {
    private Thread m_controllerThread;

    public PacMan(){
        PacManController controller = new PacManController();
        m_controllerThread = new Thread(controller);
    }

    @Override
    protected void onStart() {
        m_controllerThread.start();
        Debug.Log("Pacman started");
    }

    @Override
    protected void onUpdate() {
        m_controllerThread.start();
    }

}
