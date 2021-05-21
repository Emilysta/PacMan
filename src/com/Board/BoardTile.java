package com.Board;

public class BoardTile {
    private int m_top = 0;
    private int m_left = 0;
    private int m_bottom = 0;
    private int m_right = 0;

    public void setTilesAround(int top, int left, int bottom, int right)
    {
        m_top = top;
        m_left = left;
        m_bottom = bottom;
        m_right = right;
    }

    public void getTilesAround(int top, int left, int bottom, int right)
    {
        top = m_top;
        left = m_left;
        bottom = m_bottom;
        right = m_right; //toDo check changing values
    }
}
