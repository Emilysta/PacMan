package com.Utility;

/**
 * Class represents a two dimensional vector
 */
public class Vector2 {
    public float x;
    public float y;

    public Vector2() {
        x = 0;
        y = 0;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        Vector2 vector = (Vector2) obj;
        if (vector.x == x && vector.y == y)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y;
    }
}
