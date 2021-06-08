package com.Utility;

/**
 * Klasa reprezentuja wektor 2 wymiarowy
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

    public Vector2 multiply(float value) {
        return new Vector2(x * value, y * value);
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
