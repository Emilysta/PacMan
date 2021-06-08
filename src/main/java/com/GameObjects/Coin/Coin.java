package com.GameObjects.Coin;

import com.GameLoop.GameLoop;
import com.GameObjects.GameObject;
import com.Utility.GlobalReferenceManager;
import com.Utility.Sprite;
import com.Utility.Vector2;

/**
 * Klasa reprezentuje obiekt ktory moze zostac zjedzony przez pacmana w swiecie
 * gry. Klasa dziedziczy po klasie GameObject aby byc dodanym do glownej petli
 * gry.
 */
public class Coin extends GameObject {
    private final static float CoinTakeDistance = 10;
    private boolean m_isTaken = false;
    private boolean m_isPowerUp = false;

    /**
     * Tworzy nowy obiekt w swiecie gry
     * 
     * @param sprite - sprite ktory nalezy uzyc
     * @param x      - pozycja x na siatce
     * @param y      - pozycja y na siatce
     */
    public Coin(Sprite sprite, int x, int y, boolean isPowerUp) {
        super(sprite);
        m_position = new Vector2((30 * x) + 5, (30 * y) + 5);
        m_isPowerUp = isPowerUp;
    }

    @Override
    protected void onStart() {

    }

    /**
     * 
     * Metoda sprawdza czy pacman jest na miejscu obiektu, jesli tak to niszczy go i
     * dodaje punkty.
     */
    @Override
    protected void onUpdate() {
        if (checkIfCollides(GlobalReferenceManager.pacMan.getPosition()) && !m_isTaken) {
            if (m_isPowerUp)
                GlobalReferenceManager.makePacmanPoweredUp();
            else
                GlobalReferenceManager.addScore(10);
            GlobalReferenceManager.Coins.remove(this);
            GameLoop.getInstance().removeListener(this);
            m_isTaken = true;
        }
    }

    @Override
    protected void onExit() {
        // Coins don't have any action to do on exit
    }

    /**
     * Metoda sprawdza czy position koliduje z aktualnym obiektem.
     * 
     * @param position - pozycja drugiego obiektu
     * @return true jesli jest kolizja, false inaczej
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
