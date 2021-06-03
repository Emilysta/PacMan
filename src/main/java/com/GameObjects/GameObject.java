package com.GameObjects;

import com.GameLoop.GameLoop;
import com.Utility.Sprite;
import com.Utility.Vector2;
/**
 * Abstract class being the base for all gameObjects in the game
 * Implements the observer pattern by adding itself to listeners list in the
 * main game loop
 */
public abstract class GameObject {

    public Sprite objectSprite;

    protected Vector2 m_position = new Vector2(0, 0);

    /**
     * Creates a new game object and adds it into the listeners list
     * @param sprite - the sprite with which the game object should render
     */
    public GameObject(Sprite sprite) {
        objectSprite = sprite;
        GameLoop.getInstance().addListener(this);
    }

    /**
     * Method gets called when the game loop starts or a new game object is
     * created 
     */
    public final void start() {
        onStart();
    }

    /**
     * Method gets called every frame
     */
    public final void update() {
        onUpdate();
    }

    /** 
     * Method gets called on game exit or when an gameObject gets destroyed
     */
    public final void exit() {
        onExit();
    }
    /**
     * Method returns the current position of the gameObject
     * @return vector2 of current position
     */
    public Vector2 getPosition() {
        return m_position;
    }
    /**
     * Method gets called on start of game loop or creation of game object
     */
    protected abstract void onStart();
    /**
     * Method gets called every frame
     */
    protected abstract void onUpdate();
    /**
     * Method gets called on game exit or when the game object is destroyed
     */
    protected abstract void onExit();
}
