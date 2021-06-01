package com.GameObjects.Coin;

import com.GameLoop.GameLoop;
import com.GameObjects.GameObject;
import com.Utility.GlobalReferenceManager;
import com.Utility.Sprite;
import com.Utility.Vector2;

public class Coin extends GameObject {
    private boolean taken = false;

    public Coin(Sprite sprite, int x, int y) {
        super(sprite);
        m_position = new Vector2(30 * x, 30 * y);
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onUpdate() {
        if (checkIfCollides(GlobalReferenceManager.pacMan.getPosition()) && !taken) {
            GlobalReferenceManager.addScore(10);
            GlobalReferenceManager.Coins.remove(this);
            GameLoop.getInstance().removeListener(this);
            taken = true;
        }

    }

    @Override
    protected void onExit() {

    }

    private boolean checkIfCollides(Vector2 position) {
        float xDiff = position.x - m_position.x;
        float yDiff = position.y - m_position.y;

        if (xDiff < 5 && xDiff > -5)
            if (yDiff < 5 && yDiff > -5)
                return true;
        return false;
    }
}
