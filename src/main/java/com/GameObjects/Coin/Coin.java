package com.GameObjects.Coin;

import com.GameLoop.GameLoop;
import com.GameObjects.GameObject;
import com.Utility.GlobalReferenceManager;
import com.Utility.Sprite;
import com.Utility.Vector2;

/**
 * Class represents the coins that pacman can eat in the game world
 * Class extends the GameObject base class to be present in the main game loop.
 */
public class Coin extends GameObject {
    private final float CoinTakeDistance = 10;
    private boolean taken = false;

    /**
     * Creates a new coin in the game world
     * @param sprite - sprite with which to show the coin
     * @param x - x posiiton on grid
     * @param y - y position on grid
     */
    public Coin(Sprite sprite, int x, int y) {
        super(sprite);
        m_position = new Vector2((30 * x)+5 , (30 * y)+5);
    }

    @Override
    protected void onStart() {
        //Coins don't have any actions to do on start
    }

    /**
     * Method checks whether PacMan is on this coin, if so it adds the score and
     * removes itself from the game
     */
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
        //Coins don't have any action to do on exit
    }

    /**
     * Method checks whether <p>position<p> collides with this coin
     * @param position - position of second object
     * @return true if there is collision, false otherwise
     */
    private boolean checkIfCollides(Vector2 position) {
        float xDiff = position.x - m_position.x;
        float yDiff = position.y - m_position.y;

        if (xDiff < CoinTakeDistance && xDiff > -CoinTakeDistance)
            if (yDiff < CoinTakeDistance && yDiff > -CoinTakeDistance)
                return true;
        return false;
    }
}
