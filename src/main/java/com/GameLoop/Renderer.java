package com.GameLoop;

import com.GameObjects.GameObject;
import javafx.scene.canvas.GraphicsContext;
/**
 * Class responsible for rendering gameObjects onto game canvas
 */
public class Renderer {
    private GraphicsContext m_graphicsContext;
    private double m_canvasWidth;
    private double m_canvasHeight;

    /**
     * Creates a new Renderer class
     * @param graphicsContext - where to render gameObjects
     * @param width - canvas width
     * @param height - canvas height
     */
    public Renderer(GraphicsContext graphicsContext, double width, double height) {
        m_graphicsContext = graphicsContext;
        m_canvasWidth = width;
        m_canvasHeight = height;
    }
    /**
     * Method clears the whole canvas of any rendered objects
     */
    public void prepareScene() {
        m_graphicsContext.clearRect(0, 0, m_canvasWidth, m_canvasHeight);
    }
    /**
     * Method draws an object in it's position
     * @param gameObject - object to render on the board
     */
    public void renderObject(GameObject gameObject) {
        m_graphicsContext.drawImage(gameObject.objectSprite.spriteImage,
                gameObject.getPosition().x, gameObject.getPosition().y,
                gameObject.objectSprite.width, gameObject.objectSprite.height
        );
    }
}
