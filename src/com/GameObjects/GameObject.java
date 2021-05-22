package com.GameObjects;

import com.GameLoop.GameLoop;
import com.Utility.Sprite;
import com.Utility.Vector2;

public abstract class GameObject {

    public Sprite objectSprite;

    protected Vector2 m_position = new Vector2(0, 0);

    public GameObject(Sprite sprite) {
        objectSprite = sprite;
        GameLoop.getInstance().addListener(this);
    }

    public final void start() {
        onStart();
    }

    public final void update() {
        onUpdate();
    }

    public final void exit() {
        onExit();
    }

    public Vector2 getPosition() {
        return m_position;
    }

    protected abstract void onStart();

    protected abstract void onUpdate();

    protected abstract void onExit();
}
