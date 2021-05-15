package com.GameObjects;

import com.GameLoop.GameLoop;

public abstract class GameObject {

    public GameObject(){
        GameLoop.getInstance().addListener(this);
    }

    public final void start() {
        onStart();
    }

    public final void update() {
        onUpdate();
    }

    protected abstract void onStart();

    protected abstract void onUpdate();
}
