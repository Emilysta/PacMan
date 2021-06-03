package com.GameObjects.Ghosts;

import com.GameObjects.GameObject;
import com.GameObjects.PacMan.PacManController;
import com.Utility.Sprite;
import com.Utility.Vector2;

public class Ghost extends GameObject {

    public Ghost(Sprite sprite) {
        super(sprite);
        m_position = new Vector2(90, 30);
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onUpdate() {

    }

    @Override
    protected void onExit() {

    }
}
