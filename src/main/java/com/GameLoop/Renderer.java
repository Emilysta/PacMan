package com.GameLoop;

import com.GameObjects.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
    private final GraphicsContext m_graphicsContext;
    private final double m_canvasWidth;
    private final double m_canvasHeight;

    public Renderer(GraphicsContext graphicsContext, double width, double height) {
        m_graphicsContext = graphicsContext;
        m_canvasWidth = width;
        m_canvasHeight = height;
    }

    public void prepareScene() {
        m_graphicsContext.clearRect(0, 0, m_canvasWidth, m_canvasHeight);
    }

    public void renderObject(GameObject gameObject) {
        m_graphicsContext.drawImage(gameObject.objectSprite.spriteImage,
                gameObject.getPosition().x, gameObject.getPosition().y,
                gameObject.objectSprite.width, gameObject.objectSprite.height
        );
    }
}
