package com.Board;


public class BoardTile {

    private Neighbourhood m_NGBH = new Neighbourhood();
    public void setTilesAround(int top, int left, int bottom, int right)
    {
        m_NGBH.m_top = top;
        m_NGBH.m_left = left;
        m_NGBH.m_bottom = bottom;
        m_NGBH.m_right = right;
    }

    public Neighbourhood getTilesAround()
    {
        return m_NGBH;
    }
}
