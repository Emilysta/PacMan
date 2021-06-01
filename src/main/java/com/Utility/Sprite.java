package com.Utility;

import javafx.scene.image.Image;

public class Sprite {
    public Image spriteImage;
    public float width;
    public float height;

    public Sprite(Image image, float width, float height){
        spriteImage = image;
        this.width = width;
        this.height = height;
    }
}
