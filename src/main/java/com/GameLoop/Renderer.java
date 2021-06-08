package com.GameLoop;

import com.GameObjects.GameObject;
import javafx.scene.canvas.GraphicsContext;
/**
 * Klasa odpowiada za renderowanie gameObjectow na canvas.
 */
public class Renderer {
    private GraphicsContext m_graphicsContext;
    private double m_canvasWidth;
    private double m_canvasHeight;

    /**
     * Tworzy nowa klase Renderer
     * @param graphicsContext - context na ktorym renderowac GameObjecty
     * @param width - szerokosc canvas
     * @param height - wysokosc canvas
     */
    public Renderer(GraphicsContext graphicsContext, double width, double height) {
        m_graphicsContext = graphicsContext;
        m_canvasWidth = width;
        m_canvasHeight = height;
    }
    /**
     * Metoda czysci canvas ze wszystkich narysowanych rzeczy
     */
    public void prepareScene() {
        m_graphicsContext.clearRect(0, 0, m_canvasWidth, m_canvasHeight);
    }
    /**
     * Metoda pozwala na rysowanie gameObjectu na jego pozycji
     * @param gameObject - obiekt do wyrenderowania
     */
    public void renderObject(GameObject gameObject) {
        m_graphicsContext.drawImage(gameObject.objectSprite.spriteImage,
                gameObject.getPosition().x, gameObject.getPosition().y,
                gameObject.objectSprite.width, gameObject.objectSprite.height
        );
    }
}
